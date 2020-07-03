package botsandbytes.dashboard.backend;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import botsandbytes.dashboard.backend.response.InputFeature;
import botsandbytes.dashboard.backend.response.OutputFeature;

public interface DataLakeAccess {

	static final String SELECT_INPUT_FEATURES = "select car, var, val, ts, configversion, softwareversion from bogie_diagnostic.rrx_features_orc";
	static final String SELECT_OUTPUT_FEATURES = "select objectid, componentid, origin, valuename, value, time from bogie_diagnostic.state";

	List<InputFeature> getInputFeatures(String carNo, List<String> signals) throws SQLException, InterruptedException;

	List<OutputFeature> getOutputFeatures(String train, String comp, String algo)
			throws SQLException, InterruptedException;

	Function<Object, OutputFeature> outputFeatureFromRow();

	Function<Object, InputFeature> inputFeatureFromRow();

	default String from() {
		return LocalDateTime.now().minusDays(14).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00"));
	}

	<T> List<T> runQuery(String sql, Function<Object, T> objCreat) throws SQLException, InterruptedException;

}
