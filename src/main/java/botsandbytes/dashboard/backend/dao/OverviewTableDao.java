package botsandbytes.dashboard.backend.dao;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository("overviewTableDao")
public class OverviewTableDao extends Dao {

	@Autowired
	public OverviewTableDao(JdbcTemplate template) {
		super(template, "overview");
	}

	public List<Map<String, Object>> getCompResults(String algo, String comp) {
		String sql = "select level_0, level_1, level_2, level_3, diagid, lastResult_RUL, lastResult_HS, log_message, log_type, objectid, carnumber  from overview where origin='"
				+ algo + "' and diagid='" + comp + "'";
		return template.queryForList(sql);
	}

}