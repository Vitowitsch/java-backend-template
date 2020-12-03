package botsandbytes.dashboard.backend.dao;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import botsandbytes.dashboard.backend.response.Row;

@Repository("cSRMDao")
public class CSRMDao extends Dao {

	@Autowired
	public CSRMDao() {
		super("overview");
	}

	public List<Row> getAll() {
		List<Row> r = new LinkedList<Row>();
		String sql = "select origin, objectid, diagid, lastResult_HS, lastResult_RUL, carnumber, boogie_woogie, axis, comp, lastExecTime, lastResultTime from overview order by objectid, origin, carnumber;";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		String train;
		while (rows.next()) {
			train = rows.getString("objectid");
			Integer lastResult_HS = Integer.parseInt(rows.getString("lastResult_HS"));
			// TODO: according to the engineeers these shall not be delivered to frontend
			if (0 != lastResult_HS) {
				r.add(new Row(rows.getString("origin"), train, rows.getString("diagid"), lastResult_HS,
						rows.getString("carnumber"), rows.getString("boogie_woogie"), rows.getString("axis"),
						rows.getString("comp"), rows.getString("lastResultTime"), rows.getString("lastExecTime")));
			}
		}
		return r;
	}

}
