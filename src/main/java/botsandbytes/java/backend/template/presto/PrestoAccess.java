package botsandbytes.java.backend.template.presto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.ThresholdingOutputStream;

import botsandbytes.java.backend.template.DataLakeAccess;
import botsandbytes.java.backend.template.athena.StreamHelper;
import botsandbytes.java.backend.template.athena.StreamHelper.Record;
import botsandbytes.java.backend.template.model.DrivenCount;
import botsandbytes.java.backend.template.response.DashboardData;
import botsandbytes.java.backend.template.response.OutputFeature;

public class PrestoAccess implements DataLakeAccess {

	private DriverUtil driverUtil = new DriverUtil();

	@Override
	public Function<Object, OutputFeature> outputFeatureFromRow() {
		return (Object o) -> {
			Record r = (Record) o;
			return new OutputFeature(r.getString("objectid"), r.getString("componentid"), r.getString("origin"),
					r.getString("valuename"), r.getString("value"), r.getString("time"));
		};
	}

	@Override
	public Function<Object, DashboardData> dashboardDataFromRow() {
		return (Object o) -> {
			Record r = (Record) o;
			return new DashboardData(r.getString("objectid"), r.getString("origin"), r.getString("componentid"),
					r.getString("time"), r.getString("value"));
		};
	}

	@Override
	public Function<Object, DrivenCount> drivenCountFromRow(int threshold) {
		return (Object o) -> {
			Record r = (Record) o;
			return new DrivenCount(r.getString("objectid").replaceFirst(OBJECT_PREFIX, ""), r.getString("event_date"),
					Integer.parseInt(r.getString("km_diff")), threshold);
		};
	}

	public <T> List<T> runQuery(final String sql, Function<Object, T> objCreat) throws SQLException {
		try (Connection c = driverUtil.getPresto();
				Statement s = c.createStatement();
				ResultSet resultSet = s.executeQuery(sql)) {
			Stream<Record> stream = StreamHelper.asStream(resultSet);
			return stream.map(r -> objCreat.apply(r)).collect(Collectors.toList());
		}
	}
}
