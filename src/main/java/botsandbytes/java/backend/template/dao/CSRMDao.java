package botsandbytes.java.backend.template.dao;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import botsandbytes.java.backend.template.response.Row;

@Repository("cSRMDao")
public class CSRMDao extends Dao {

	@Autowired
	public CSRMDao() {
		super("overview");
	}

	public List<Row> getAll() {
		List<Row> r = new LinkedList<Row>();
		String sql = "${REPLACE}";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		String train;
		while (rows.next()) {
			train = rows.getString("objectid");
			Integer state = Integer.parseInt(rows.getString("state"));
			if (0 != state) {
				r.add(new Row(rows.getString("origin"), train, rows.getString("fieldid"), state,
						rows.getString("target"), rows.getString("rootpos"), rows.getString("position"),
						rows.getString("comp"), rows.getString("lastResultTime"), rows.getString("lastExecTime")));
			}
		}
		return r;
	}

}
