package botsandbytes.java.backend.template.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository("carDao")
public class CarDao extends Dao {

	@Autowired
	public CarDao() {
		super("cars");
	}

	public String getCarNo(String train, String car) {
		return "${REPLACE}";
	}

	public List<String> getTrains() {
		String sql = "${REPLACE}";
		List<String> objects = new ArrayList<>();
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			objects.add(rows.getString("objectid"));
		}
		return objects;
	}

	public List<String> getFieldIds() {
		String sql = "${REPLACE}";
		List<String> fieldIds = new ArrayList<>();
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			fieldIds.add(rows.getString("fieldId"));
		}
		return fieldIds;
	}

	public List<String> getAlgos() {
		String sql = "${REPLACE}";
		List<String> algos = new ArrayList<>();
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			algos.add(rows.getString("origin"));
		}
		return algos;
	}

}
