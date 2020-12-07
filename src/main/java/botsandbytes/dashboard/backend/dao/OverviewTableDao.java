package botsandbytes.dashboard.backend.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("overviewTableDao")
public class OverviewTableDao extends Dao {

	@Autowired
	public OverviewTableDao() {
		super("overview");
	}

	public List<Map<String, Object>> getCompResults(String algo, String comp) {
		String sql = "${REPLACE}";
		return jdbcTemplate.queryForList(sql);
	}

}