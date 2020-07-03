package botsandbytes.dashboard.backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("stateDao")
public class StateDao extends Dao {

	@Autowired
	public StateDao(JdbcTemplate template) {
		super(template, "state");
	}
}