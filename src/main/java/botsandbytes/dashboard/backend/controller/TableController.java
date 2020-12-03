package botsandbytes.dashboard.backend.controller;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
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
import botsandbytes.dashboard.backend.dao.*;
import botsandbytes.dashboard.backend.DataLakeAccess;
import botsandbytes.dashboard.backend.DataLakeCache;
import botsandbytes.dashboard.backend.SnapshotDataCache;
import botsandbytes.dashboard.backend.dao.CarDao;
import botsandbytes.dashboard.backend.dao.OverviewTableDao;
import botsandbytes.dashboard.backend.request.GetRowsRequest;
import botsandbytes.dashboard.backend.request.GetSigalRequest;
import botsandbytes.dashboard.backend.response.DashboardData;
import botsandbytes.dashboard.backend.response.GetRowsResponse;
import botsandbytes.dashboard.backend.response.InputFeature;
import botsandbytes.dashboard.backend.response.OutputFeature;
import botsandbytes.dashboard.backend.response.Row;
import botsandbytes.dashboard.backend.storage.Expectancy;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

@RestController
public class TableController {

	private CarDao carDao;

	private OverviewTableDao overviewTableDao;

	@Autowired
	DataLakeCache dataLakeCache;

	@Autowired
	Expectancy expectancy;

	@Autowired
	SnapshotDataCache mySqlCache;

	DataLakeAccess dataLake;

	@Autowired
	Config config;

	private Logger logger = LogManager.getLogger(this.getClass());

	@PostConstruct
	public void init() {
		this.dataLake = config.getDataLakeAccess();
	}

	@Autowired
	public TableController(@Qualifier("carDao") CarDao carDao,
			@Qualifier("overviewTableDao") OverviewTableDao overviewTableDao) {
		this.carDao = carDao;
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

	@RequestMapping(method = RequestMethod.GET, value = "/expectancy")
	@ResponseBody
	@CrossOrigin
	public String getExpectancy() {
		return expectancy.get(config.isS3Mocked());
	}

	@RequestMapping(value = "/history")
	@ResponseBody
	@CrossOrigin
	public List<OutputFeature> getOutputFeatureHistory(@RequestParam(value = "train", required = true) String train,
			@RequestParam(value = "comp", required = true) String comp,
			@RequestParam(value = "algo", required = true) String algo) throws InterruptedException, SQLException {
		logger.info("get state history");
		Map<String, List<OutputFeature>> outputFeatureCache = dataLakeCache.getOutputFeatureCache();
		List<OutputFeature> result = new LinkedList<>();
		final String key = train + comp + algo;
		if (!config.getCACHE_ALGO_OUTPUT_FEATURES()) {
			result = dataLake.getOutputFeatures(train, comp, algo);
		} else if (outputFeatureCache.containsKey(key)) {
			result = outputFeatureCache.get(key);
		} else {
			logger.info("output feature " + key + " not found in cache");
		}
		return result;
	}

	@RequestMapping(value = "/dashboard")
	@ResponseBody
	@CrossOrigin
	public List<DashboardData> getDashboardData() throws InterruptedException, SQLException {
		logger.info("get dashboard data");
		return dataLakeCache.getDashboardData();
	}

	@RequestMapping(method = POST, value = "/signals")
	@ResponseBody
	@CrossOrigin
	public List<InputFeature> getInputFeatureHistory(@RequestBody GetSigalRequest req) throws Exception {
		final String train = req.getTrain();
		final String car = req.getCar();
		String carUIC = carDao.getCarNo(train, car);
		List<String> toBeRetrieved = req.getSignals();
		List<InputFeature> result = new ArrayList<>();
		Map<String, List<InputFeature>> inputFeatureCache = dataLakeCache.getInputFeatureCache();
		if (config.getCACHE_ALGO_INPUT_FEATURES()) {
			for (String feature : toBeRetrieved) {
				logger.info("loading " + feature + " from cache");
				String key = carUIC + feature;
				if (inputFeatureCache.containsKey(key)) {
					result.addAll(inputFeatureCache.get(key));
				} else {
					logger.info("input feature " + key + " not found in cache");
				}
			}
		} else {
			result = dataLake.getInputFeatures(carUIC, toBeRetrieved);
		}
		return result;
	}

}