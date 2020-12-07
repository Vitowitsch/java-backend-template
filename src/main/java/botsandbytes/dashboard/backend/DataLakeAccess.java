package botsandbytes.dashboard.backend;

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
import botsandbytes.dashboard.backend.response.OutputFeature;

public interface DataLakeAccess {

	<T> List<T> runQuery(String sql, Function<Object, T> objCreat) throws SQLException, InterruptedException;

	Function<Object, DashboardData> dashboardDataFromRow();

	Function<Object, OutputFeature> outputFeatureFromRow();

	Function<Object, DrivenCount> drivenCountFromRow(int threshold);

	static final String OBJECT_PREFIX = "${REPLACE}";
	static final String MILEAGE_SQL_POSTFIX = " ${REPLACE}";
	static final String SELECT_OUTPUT_FEATURES = "${REPLACE}";
	static final String SELECT_DASHBOARD_DATA = "${REPLACE}";
	static final DateTimeFormatter MILEAGE_FORMAT = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	static final String MILEAGE_SQL_PREFIX = "${REPLACE}";

	static final String SELECT_LASTLY_DRIVEN = "${REPLACE}";

	default String getMileageSQLInfix() {
		LocalDate end = LocalDate.now();
		LocalDate begin = end.minusDays(31);
		return "'" + begin.format(MILEAGE_FORMAT) + "' and '" + end.format(MILEAGE_FORMAT) + "'";
	}

	default List<OutputFeature> getOutputFeatures(String train, String comp, String algo)
			throws InterruptedException, SQLException {
		return runQuery(SELECT_OUTPUT_FEATURES, outputFeatureFromRow());
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
