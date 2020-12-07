package botsandbytes.dashboard.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import botsandbytes.dashboard.backend.Config;
import botsandbytes.dashboard.backend.DataLakeAccess;
import botsandbytes.dashboard.backend.DataLakeCache;
import botsandbytes.dashboard.backend.SnapshotDataCache;
import botsandbytes.dashboard.backend.dao.CarDao;
import botsandbytes.dashboard.backend.dao.OverviewTableDao;
import botsandbytes.dashboard.backend.request.GetRowsRequest;
import botsandbytes.dashboard.backend.response.DashboardData;
import botsandbytes.dashboard.backend.response.GetRowsResponse;
import botsandbytes.dashboard.backend.response.OutputFeature;
import botsandbytes.dashboard.backend.response.Row;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

@RestController
public class TableController {

	private OverviewTableDao overviewTableDao;

	@Autowired
	DataLakeCache dataLakeCache;

	@Autowired
	SnapshotDataCache mySqlCache;

	DataLakeAccess dataLake;

	@Autowired
	Config config;

	@PostConstruct
	public void init() {
		this.dataLake = config.getDataLakeAccess();
	}

	@Autowired
	public TableController(@Qualifier("carDao") CarDao carDao,
			@Qualifier("overviewTableDao") OverviewTableDao overviewTableDao) {
		this.overviewTableDao = overviewTableDao;
	}

	@RequestMapping(method = POST, value = "/overview")
	@ResponseBody
	@CrossOrigin
	public GetRowsResponse getOverview(@RequestBody GetRowsRequest request) {
		return overviewTableDao.getData(request);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/overview_full")
	@ResponseBody
	@CrossOrigin
	public List<Row> getFullOverview() {
		return mySqlCache.getRows();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/movements")
	@ResponseBody
	@CrossOrigin
	public Map<String, Integer> getMovements() {
		return dataLakeCache.getMovements();
	}

	@RequestMapping(value = "/history")
	@ResponseBody
	@CrossOrigin
	public List<OutputFeature> getOutputFeatureHistory(@RequestParam(value = "object", required = true) String object,
			@RequestParam(value = "comp", required = true) String comp,
			@RequestParam(value = "algo", required = true) String algo) throws InterruptedException, SQLException {
		Map<String, List<OutputFeature>> outputFeatureCache = dataLakeCache.getOutputFeatureCache();
		return outputFeatureCache.get(object + comp + algo);

	}

	@RequestMapping(value = "/dashboard")
	@ResponseBody
	@CrossOrigin
	public List<DashboardData> getDashboardData() throws InterruptedException, SQLException {
		return dataLakeCache.getDashboardData();
	}

}