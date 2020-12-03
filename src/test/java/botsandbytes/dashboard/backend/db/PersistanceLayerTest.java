package botsandbytes.dashboard.backend.db;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import botsandbytes.dashboard.backend.Config;
import botsandbytes.dashboard.backend.response.Row;

@DataJdbcTest
public class PersistanceLayerTest {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	Config config;

	@Test
	public void loadDataTest() {
		insertBatch(createData());
		assertTrue(0 < 1);
	}

	private List<Row> createData() {
		List<Row> rows = new ArrayList<>();
		IntStream.range(0, 10).forEach(index -> {
			Row r = new Row("origin", "objectid", "diagid", 3, "carnumber", "bogie", "axis", "comp", "lastResultTime",
					"lastExecTime");
			rows.add(r);

		});
		return rows;
	}

	private void insertBatch(final List<Row> rows) {
		String sql = "INSERT INTO overview (origin, objectid, diagid, lastResult_HS, carnumber, bogie, axis, comp, lastResultTime, lastExecTime) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Row r = rows.get(i);

				ps.setString(1, r.getOrigin());
				ps.setString(2, r.getObjectid());
				ps.setString(3, r.getDiagid());
				ps.setLong(4, r.getLastResult_HS());
				ps.setString(5, r.getCarnumber());
				ps.setString(6, r.getBoogieWoogie());
				ps.setString(7, r.getAxis());
				ps.setString(8, r.getComp());
				ps.setString(9, r.getLastResultTime());
				ps.setString(10, r.getLastExecTime());
			}

			@Override
			public int getBatchSize() {
				return rows.size();
			}
		});
	}

}
