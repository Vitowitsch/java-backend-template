package botsandbytes.dashboard.athena;

import software.amazon.awssdk.services.athena.model.GetQueryResultsResponse;
import software.amazon.awssdk.services.athena.model.Row;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;

import org.springframework.stereotype.Component;

import botsandbytes.dashboard.backend.DataLakeAccess;
import botsandbytes.dashboard.backend.response.InputFeature;
import botsandbytes.dashboard.backend.response.OutputFeature;

import static java.util.stream.Collectors.joining;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AthenaAccess extends AthenaBase implements DataLakeAccess {

	public AthenaAccess(String outputLocation) {
		super(outputLocation);
	}

	@Override
	public List<OutputFeature> getOutputFeatures(String train, String comp, String algo)
			throws InterruptedException, SQLException {
		final String sql = SELECT_OUTPUT_FEATURES + "where componentid='" + comp + "' and origin='" + algo
				+ "' and objectid='" + train + "' order by time desc limit 200";
		return runQuery(sql, outputFeatureFromRow());
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
	public Function<Object, InputFeature> inputFeatureFromRow() {
		return (Object o) -> {
			Row row = (Row) o;
			return new InputFeature(row.data().get(0).varCharValue(), row.data().get(1).varCharValue(),
					row.data().get(2).varCharValue(), row.data().get(3).varCharValue(),
					row.data().get(4).varCharValue(), row.data().get(5).varCharValue());
		};
	}

	@Override
	public List<InputFeature> getInputFeatures(String carNo, List<String> signals)
			throws InterruptedException, SQLException {
		String sigs = signals.stream().collect(joining("','"));
		final String sql = SELECT_INPUT_FEATURES + " where car='" + carNo + "' and var in ('" + sigs + "') and ts>'"
				+ from() + "'";
		return runQuery(sql, inputFeatureFromRow());
	}

	@Override
	public <T> List<T> runQuery(final String sql, Function<Object, T> objCreat)
			throws SQLException, InterruptedException {
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
