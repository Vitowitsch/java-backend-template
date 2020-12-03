package botsandbytes.dashboard.backend.db;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import botsandbytes.dashboard.backend.builder.SqlQueryBuilder;
import botsandbytes.dashboard.backend.filter.ColumnFilter;
import botsandbytes.dashboard.backend.filter.SetColumnFilter;
import botsandbytes.dashboard.backend.request.ColumnVO;
import botsandbytes.dashboard.backend.request.GetRowsRequest;
import botsandbytes.dashboard.backend.request.SortModel;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

public class ServerSideRowModelTest {

	@Test
	public void grouping() {
		GetRowsRequest request = new GetRowsRequest();
		request.setStartRow(0);
		request.setEndRow(100);

		request.setRowGroupCols(singletonList(new ColumnVO("objectid", "objectid", "objectid", "")));
		request.setValueCols(asList(new ColumnVO("myobject", "myobject", "myobject", "sum")));

		String sql = new SqlQueryBuilder().createSql(request, "overview", emptyMap());

		assertEquals("SELECT objectid, sum(myobject) as myobject FROM overview GROUP BY objectid LIMIT 0,100", sql);
	}

	@Test
	public void groupPlusKey() {
		GetRowsRequest request = new GetRowsRequest();
		request.setStartRow(100);
		request.setEndRow(200);
		request.setRowGroupCols(asList(new ColumnVO("objectid", "objectid", "objectid", "")));
		request.setValueCols(asList(new ColumnVO("GOLD", "Gold", "GOLD", "sum")));
		request.setGroupKeys(singletonList("object_key"));

		String sql = new SqlQueryBuilder().createSql(request, "overview", emptyMap());

		assertEquals("SELECT * FROM overview WHERE objectid = 'object_key' LIMIT 100,200", sql);
	}

	@Test
	public void singleGroupWithFilteringAndSorting() {
		GetRowsRequest request = new GetRowsRequest();
		request.setStartRow(100);
		request.setEndRow(200);
		request.setRowGroupCols(asList(new ColumnVO("objectid", "objectid", "objectid", "")));
		request.setValueCols(asList(new ColumnVO("GOLD", "Gold", "GOLD", "sum")));
		request.setGroupKeys(singletonList("object_key"));

		request.setFilterModel(new HashMap<String, ColumnFilter>() {
			{
				put("attribute", new SetColumnFilter(singletonList("attribute_val")));
			}
		});
		request.setSortModel(singletonList(new SortModel("objectid", "asc")));

		String sql = new SqlQueryBuilder().createSql(request, "overview", emptyMap());

		assertEquals(
				"SELECT * FROM overview WHERE objectid = 'object_key' AND attribute IN ('attribute_val') ORDER BY objectid asc LIMIT 100,200",
				sql);
	}
}