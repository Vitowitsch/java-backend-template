package botsandbytes.dashboard.backend.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import botsandbytes.dashboard.backend.builder.SqlQueryBuilder;
import botsandbytes.dashboard.backend.request.ColumnVO;
import botsandbytes.dashboard.backend.request.GetRowsRequest;
import botsandbytes.dashboard.backend.response.GetRowsResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static botsandbytes.dashboard.backend.builder.ResponseBuilder.createResponse;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

public class Dao {

	protected JdbcTemplate template;
	protected SqlQueryBuilder queryBuilder;
	protected String table;

	public Dao(JdbcTemplate template, String table) {
		this.template = template;
		
		queryBuilder = new SqlQueryBuilder();
		this.table = table;
	}

	public GetRowsResponse getData(GetRowsRequest request) {
		Map<String, List<String>> pivotValues = getPivotValues(request.getPivotCols());
		String sql = queryBuilder.createSql(request, table, pivotValues);
		List<Map<String, Object>> rows = template.queryForList(sql);
		return createResponse(request, rows, pivotValues);
	}

	protected Map<String, List<String>> getPivotValues(List<ColumnVO> pivotCols) {
		return pivotCols.stream().map(ColumnVO::getField)
				.collect(toMap(pivotCol -> pivotCol, this::getPivotValues, (a, b) -> a, LinkedHashMap::new));
	}

	protected List<String> getPivotValues(String pivotColumn) {
		String sql = "SELECT DISTINCT %s FROM " + table;
		return template.queryForList(format(sql, pivotColumn), String.class);
	}

}