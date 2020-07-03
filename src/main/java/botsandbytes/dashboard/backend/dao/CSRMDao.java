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
	public CSRMDao(JdbcTemplate template) {
		super(template, "overview");
	}

	public List<Row> getAll() {
		List<Row> r = new LinkedList<Row>();
		String sql = "select origin, objectid, diagid, lastResult_HS, lastResult_RUL, carnumber, bogie, axis, comp, level_0, level_1, level_2, level_3 from overview order by objectid, origin, carnumber;";
		SqlRowSet rows = template.queryForRowSet(sql);
		while (rows.next()) {
			r.add(new Row(rows.getString("origin"), rows.getString("objectid"), rows.getString("diagid"),
					Integer.parseInt(rows.getString("lastResult_HS")), rows.getString("lastResult_RUL"), rows.getString("carnumber"),
					rows.getString("bogie"), rows.getString("axis"), rows.getString("comp"), rows.getInt("level_0"),
					rows.getInt("level_1"), rows.getInt("level_2"), rows.getInt("level_3")));
		}
		return r;
	}

}
