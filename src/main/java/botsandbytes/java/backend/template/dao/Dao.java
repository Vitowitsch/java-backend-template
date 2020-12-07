package botsandbytes.java.backend.template.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import botsandbytes.java.backend.template.builder.SqlQueryBuilder;
import botsandbytes.java.backend.template.request.ColumnVO;
import botsandbytes.java.backend.template.request.GetRowsRequest;
import botsandbytes.java.backend.template.response.GetRowsResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static botsandbytes.java.backend.template.builder.ResponseBuilder.createResponse;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

public class Dao {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	protected SqlQueryBuilder queryBuilder;
	protected String table;

	public Dao() {
	}

	public Dao(String table) {
		queryBuilder = new SqlQueryBuilder();
		this.table = table;
	}

	public GetRowsResponse getData(GetRowsRequest request) {
		Map<String, List<String>> pivotValues = getPivotValues(request.getPivotCols());
		String sql = queryBuilder.createSql(request, table, pivotValues);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		return createResponse(request, rows, pivotValues);
	}

	protected Map<String, List<String>> getPivotValues(List<ColumnVO> pivotCols) {
		return pivotCols.stream().map(ColumnVO::getField)
				.collect(toMap(pivotCol -> pivotCol, this::getPivotValues, (a, b) -> a, LinkedHashMap::new));
	}

	protected List<String> getPivotValues(String pivotColumn) {
		String sql = "SELECT DISTINCT %s FROM " + table;
		return jdbcTemplate.queryForList(format(sql, pivotColumn), String.class);
	}

}