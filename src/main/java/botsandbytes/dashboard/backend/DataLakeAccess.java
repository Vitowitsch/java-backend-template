package botsandbytes.dashboard.backend;

import static java.util.stream.Collectors.joining;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import botsandbytes.dashboard.backend.model.DrivenCount;
import botsandbytes.dashboard.backend.response.DashboardData;
import botsandbytes.dashboard.backend.response.InputFeature;
import botsandbytes.dashboard.backend.response.OutputFeature;


public interface DataLakeAccess {

	<T> List<T> runQuery(String sql, Function<Object, T> objCreat) throws SQLException, InterruptedException;

	Function<Object, DashboardData> dashboardDataFromRow();

	Function<Object, OutputFeature> outputFeatureFromRow();

	Function<Object, InputFeature> inputFeatureFromRow();

	Function<Object, DrivenCount> drivenCountFromRow(int threshold);

	static final String TRAIN_PREFIX = "DesiroHC_";
	static final String MILEAGE_SQL_POSTFIX = " AND sensorname = 'MIL' GROUP BY objectid, event_date ORDER BY event_date, objectid";
	static final String SELECT_INPUT_FEATURES = "select car, var, val, ts, configversion, softwareversion from db.project_features_orc";
	static final String SELECT_OUTPUT_FEATURES = "select objectid, componentid, origin, valuename, value, time from db.state";
	static final DateTimeFormatter MILEAGE_FORMAT = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	static String SELECT_DASHBOARD_DATA(String timeRange) {
		return "select objectid, origin, componentid, time, value from db.state where partitiondate in ('" + timeRange
				+ "') and processed=1 and valuename like 'HS%' order by time desc limit 200000";
	}

	static final String MILEAGE_SQL_PREFIX = "SELECT objectid, event_date, MIN(sensorvalue) AS km_min, MAX(sensorvalue) AS km_max, "
			+ "CAST(MAX(sensorvalue) as DECIMAL(9,0))-CAST(MIN(sensorvalue) as DECIMAL(9,0)) AS km_diff FROM rail_sensoric.sensor "
			+ "" + "WHERE project = 'project' AND category = 'OD-Train' AND event_date between ";

	static final String SELECT_LASTLY_DRIVEN = "SELECT objectid,  MIN(CAST(sensorvalue as bigint)) AS km_min, "
			+ "MAX(CAST(sensorvalue as bigint)) AS km_max, MAX(CAST(sensorvalue as bigint)) "
			+ "-MIN(CAST(sensorvalue as bigint)) "
			+ "AS km_diff FROM rail_sensoric.sensor WHERE project = 'project' AND category = 'OD-Train' "
			+ "AND event_date>'%s' AND sensorname = 'MIL' GROUP BY objectid";

	default String getMileageSQLInfix() {
		LocalDate end = LocalDate.now();
		LocalDate begin = end.minusDays(31);
		return "'" + begin.format(MILEAGE_FORMAT) + "' and '" + end.format(MILEAGE_FORMAT) + "'";
	}

	default List<InputFeature> getInputFeatures(String carNo, List<String> signals) throws Exception {
		String sigs = signals.stream().collect(joining("','"));
		final String sql = SELECT_INPUT_FEATURES + " where car='" + carNo + "' and var in ('" + sigs + "') and ts>'"
				+ from() + "'";
		return runQuery(sql, inputFeatureFromRow());
	}

	default List<OutputFeature> getOutputFeatures(String train, String comp, String algo)
			throws InterruptedException, SQLException {
		final String sql = SELECT_OUTPUT_FEATURES + "where componentid='" + comp + "' and origin='" + algo
				+ "' and objectid='" + train + "' order by time desc limit 200";
		return runQuery(sql, outputFeatureFromRow());
	}

	default String from() {
		return LocalDateTime.now().minusDays(14).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00"));
	}

	default Map<String, Integer> getMovements(int threshold) throws SQLException, InterruptedException {
		String sql = MILEAGE_SQL_PREFIX + getMileageSQLInfix() + MILEAGE_SQL_POSTFIX;
		List<DrivenCount> movements = runQuery(sql, drivenCountFromRow(threshold));
		return movements.stream().collect(Collectors.toMap(DrivenCount::getId, DrivenCount::getMileage));
	}

}
