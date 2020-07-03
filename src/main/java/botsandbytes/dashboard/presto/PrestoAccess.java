package botsandbytes.dashboard.presto;

import static java.util.stream.Collectors.joining;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import botsandbytes.dashboard.athena.StreamHelper;
import botsandbytes.dashboard.athena.StreamHelper.Record;
import botsandbytes.dashboard.backend.DataLakeAccess;
import botsandbytes.dashboard.backend.response.InputFeature;
import botsandbytes.dashboard.backend.response.OutputFeature;

public class PrestoAccess implements DataLakeAccess {

	private DriverUtil driverUtil = new DriverUtil();

	@Override
	public List<OutputFeature> getOutputFeatures(String train, String comp, String algo) throws SQLException {
		String sql = SELECT_OUTPUT_FEATURES + " where componentid='" + comp + "' and origin='" + algo
				+ "' and objectid='" + train + "' order by time desc limit 200";
		return runQuery(sql, outputFeatureFromRow());
	}

	@Override
	public Function<Object, OutputFeature> outputFeatureFromRow() {
		return (Object o) -> {
			Record r = (Record) o;
			return new OutputFeature(r.getString("objectid"), r.getString("componentid"), r.getString("origin"),
					r.getString("valuename"), r.getString("value"), r.getString("time"));
		};
	}

	@Override
	public Function<Object, InputFeature> inputFeatureFromRow() {
		return (Object o) -> {
			Record r = (Record) o;
			return new InputFeature(r.getString("car"), r.getString("var"), r.getString("val"), r.getString("ts"),
					r.getString("configversion"), r.getString("softwareversion"));
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

	@Override
	public List<InputFeature> getInputFeatures(String carNo, List<String> signals) throws SQLException {
		String sigs = signals.stream().collect(joining("','"));
		String sql = SELECT_INPUT_FEATURES + " where car='" + carNo + "' and var in ('" + sigs + "') and ts>'" + from()
				+ "'";
		return runQuery(sql, inputFeatureFromRow());
	}

}
