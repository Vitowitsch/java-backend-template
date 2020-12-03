package botsandbytes.dashboard.backend.dao;

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
		String carNo = "";
		String carStr;
		switch (car) {
		case "EWA":
			carStr = "0";
			break;
		case "EWB":
			carStr = "1";
			break;
		case "MWC":
			carStr = "2";
			break;
		case "MWD":
			carStr = "3";
			break;
		default:
			carStr = "NO_CAR_MAPPED";
		}
		String sql = "select carnumber from  cars where objectId='" + train + "' and car='" + carStr + "'";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		if (rows.next()) {
			carNo = rows.getString("carnumber");
		}
		return carNo;
	}

	public List<String> getTrains() {
		String sql = "select distinct objectid from cars";
		List<String> trains = new ArrayList<>();
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			trains.add(rows.getString("objectid"));
		}
		return trains;
	}

	public List<String> getDiagIds() {
		String sql = "select distinct diagid from configurations";
		List<String> diagIds = new ArrayList<>();
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			diagIds.add(rows.getString("diagid"));
		}
		return diagIds;
	}

	public List<String> getAlgos() {
		String sql = "select distinct origin from bill_board";
		List<String> algos = new ArrayList<>();
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
		while (rows.next()) {
			algos.add(rows.getString("origin"));
		}
		return algos;
	}

}
