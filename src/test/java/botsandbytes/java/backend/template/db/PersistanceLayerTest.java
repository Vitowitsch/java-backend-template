package botsandbytes.java.backend.template.db;

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

import botsandbytes.java.backend.template.Config;
import botsandbytes.java.backend.template.response.Row;

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
			Row r = new Row("origin", "objectid", "fieldid", 3, "target", "rootPos", "position", "comp",
					"lastResultTime", "lastExecTime");
			rows.add(r);

		});
		return rows;
	}

	private void insertBatch(final List<Row> rows) {
		String sql = "INSERT INTO overview (origin, objectid, fieldid, state, target, rootpos, position, comp, lastResultTime, lastExecTime) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Row r = rows.get(i);

				ps.setString(1, r.getOrigin());
				ps.setString(2, r.getObjectid());
				ps.setString(3, r.getFieldId());
				ps.setLong(4, r.getState());
				ps.setString(5, r.getTarget());
				ps.setString(6, r.getRootPos());
				ps.setString(7, r.getPosition());
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
