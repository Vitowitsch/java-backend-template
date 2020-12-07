package botsandbytes.dashboard.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import botsandbytes.dashboard.backend.response.DashboardData;
import botsandbytes.dashboard.backend.response.OutputFeature;

@Service
public class DataLakeCache {

	private Logger logger = LogManager.getLogger(this.getClass());

	private static final int OUTPUT_FEAT_CACH_SIZ = 1000000;

	@Autowired
	Config config;

	Map<String, List<OutputFeature>> outputFeatureCache = null;
	Map<String, Integer> movements = null;

	@Autowired
	LastlyDrivenMileageCache mileageRepo;

	List<DashboardData> dashboardData;

	DataLakeAccess dataLake;

	@PostConstruct
	private void init() {
		this.dataLake = config.getDataLakeAccess();
	}

	public Map<String, List<OutputFeature>> getOutputFeatureCache() {
		return outputFeatureCache;
	}

	public Map<String, Integer> getMovements() {
		return movements;
	}

	public List<DashboardData> getDashboardData() {
		return dashboardData;
	}

	<T> void addToCache(T toBeAdded, Supplier<String> keySupp, Map<String, List<T>> cache) {
		final String key = keySupp.get();
		List<T> currentlyCached = cache.get(key);
		if (null == currentlyCached) {
			currentlyCached = new LinkedList<>();
		}
		currentlyCached.add(0, toBeAdded);
		cache.put(key, currentlyCached);
	}

	@Scheduled(fixedRate = 35000000)
	private void updateOutputFeatureCache() throws Exception {
		final String sql = DataLakeAccess.SELECT_OUTPUT_FEATURES + "  order by time desc limit " + OUTPUT_FEAT_CACH_SIZ;
		Stream<OutputFeature> stream = dataLake.runQuery(sql, dataLake.outputFeatureFromRow()).stream();
		this.outputFeatureCache = new HashMap<>();
		stream.forEach(f -> addToCache(f, f.getKey(), outputFeatureCache));
	}

	@Scheduled(fixedRate = 35000000)
	private void updateDashboardData() throws Exception {
		Stream<DashboardData> stream = dataLake
				.runQuery(DataLakeAccess.SELECT_DASHBOARD_DATA, dataLake.dashboardDataFromRow()).stream();
		this.dashboardData = new ArrayList<DashboardData>();
		stream.forEach(f -> {
			dashboardData.add(f);
		});
	}

	public String toString(Map<?, ?> m) {
		StringBuilder sb = new StringBuilder("[");
		String sep = "";
		for (Object object : m.keySet()) {
			sb.append(sep).append(object.toString()).append("->").append(m.get(object).toString());
			sep = "\n";
		}
		return sb.append("]").toString();
	}

}
