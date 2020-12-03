package botsandbytes.dashboard.backend.athena;

import software.amazon.awssdk.services.athena.model.GetQueryResultsResponse;
import software.amazon.awssdk.services.athena.model.Row;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import botsandbytes.dashboard.backend.DataLakeAccess;
import botsandbytes.dashboard.backend.model.DrivenCount;
import botsandbytes.dashboard.backend.response.DashboardData;
import botsandbytes.dashboard.backend.response.InputFeature;
import botsandbytes.dashboard.backend.response.OutputFeature;


import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AthenaAccess extends AthenaBase implements DataLakeAccess {

	private Logger logger = LogManager.getLogger(this.getClass());

	public AthenaAccess(String outputLocation) {
		super(outputLocation);
	}

	@Override
	public Function<Object, OutputFeature> outputFeatureFromRow() {
		return (Object o) -> {
			Row row = (Row) o;
			return new OutputFeature(row.data().get(0).varCharValue(), row.data().get(1).varCharValue(),
					row.data().get(2).varCharValue(), row.data().get(3).varCharValue(),
					row.data().get(4).varCharValue(), row.data().get(5).varCharValue());
		};
	}

	@Override
	public Function<Object, DashboardData> dashboardDataFromRow() {
		return (Object o) -> {
			Row row = (Row) o;
			DashboardData d = null;
			return new DashboardData(row.data().get(0).varCharValue(), row.data().get(1).varCharValue(),
					row.data().get(2).varCharValue(), row.data().get(3).varCharValue(),
					row.data().get(4).varCharValue());
		};
	}

	@Override
	public Function<Object, InputFeature> inputFeatureFromRow() {
		return (Object o) -> {
			Row row = (Row) o;
			return new InputFeature(row.data().get(0).varCharValue(), row.data().get(1).varCharValue(),
					row.data().get(2).varCharValue(), row.data().get(3).varCharValue(),
					row.data().get(4).varCharValue(), row.data().get(5).varCharValue());
		};
	}

	@Override
	public Function<Object, DrivenCount> drivenCountFromRow(int threshold) {
		return (Object o) -> {
			Row row = (Row) o;
			int mileage = -1;
			try {
				mileage = Integer.parseInt(row.data().get(4).varCharValue());
			} catch (Exception e) {
				// its okay, the first line in an athena query contains the table header.
			}
			return new DrivenCount(row.data().get(0).varCharValue().replaceFirst(TRAIN_PREFIX, ""),
					row.data().get(1).varCharValue(), mileage, threshold);
		};
	}

	@Override
	public <T> List<T> runQuery(final String sql, Function<Object, T> objCreat)
			throws SQLException, InterruptedException {
		logger.info("running query: " + sql);
		GetQueryResultsIterable paginatedQueryResults = queryAthena(sql);
		List<T> result = new LinkedList<>();
		for (GetQueryResultsResponse queryResults : paginatedQueryResults) {
			Stream<Row> stream = queryResults.resultSet().rows().stream();
			List<T> features = stream.map(r -> objCreat.apply(r)).collect(Collectors.toList());
			result.addAll(features);
		}
		result.remove(0); // contains table header
		return result;
	}

}
