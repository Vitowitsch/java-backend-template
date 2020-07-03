package botsandbytes.dashboard.backend;

import java.sql.SQLException;
import java.time.LocalDate;
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

import botsandbytes.dashboard.backend.response.InputFeature;
import botsandbytes.dashboard.backend.response.OutputFeature;

@Service
public class DataLakeCache {

	private Logger logger = LogManager.getLogger(this.getClass());

	private static final int OUTPUT_FEAT_CACH_SIZ = 1000000;
	private static final int INPUT_FEAT_CACH_SIZ = 1000000;

	@Autowired
	Config config;

	Map<String, List<OutputFeature>> outputFeatureCache = null;
	Map<String, List<InputFeature>> inputFeatureCache = null;

	DataLakeAccess dataLake;

	@PostConstruct
	private void init() {
		this.dataLake = config.getDataLakeAccess();
	}

	public Map<String, List<OutputFeature>> getOutputFeatureCache() {
		return outputFeatureCache;
	}

	public Map<String, List<InputFeature>> getInputFeatureCache() {
		return inputFeatureCache;
	}

	private String getInputFeatureList() {
		return "'" + config.getINPUT_FEATURES().replace(",", "','") + "'";
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

	@Scheduled(fixedRate = 70000000)
	private void updateOutputFeatureCache() throws SQLException, InterruptedException {
		if (!config.getCACHE_ALGO_OUTPUT_FEATURES()) {
			logger.info("cache turned off...");
			return;
		}
		logger.info("caching algo-output features ...");
		this.outputFeatureCache = new HashMap<>();
		long t1 = System.currentTimeMillis();
		final String sql = DataLakeAccess.SELECT_OUTPUT_FEATURES + " where partitiondate in ('" + getTimeRange4Year()
				+ "') order by time desc limit " + OUTPUT_FEAT_CACH_SIZ;
		logger.info(sql);
		Stream<OutputFeature> stream = dataLake.runQuery(sql, dataLake.outputFeatureFromRow()).stream();
		stream.forEach(f -> addToCache(f, f.getKey(), outputFeatureCache));
		long t2 = System.currentTimeMillis();
		logger.info("caching algo-output features done. It took " + (t2 - t1) + " ms for " + outputFeatureCache.size()
				+ " elements.");
	}

	@Scheduled(fixedRate = 70000000)
	private void updateInputFeatureCache() throws SQLException, InterruptedException {
		if (!config.getCACHE_ALGO_INPUT_FEATURES()) {
			logger.info("cache turned off...");
			return;
		}
		logger.info("caching algo-input features ...");
		this.inputFeatureCache = new HashMap<>();
		long t1 = System.currentTimeMillis();
		final String sql = DataLakeAccess.SELECT_INPUT_FEATURES + " where eventdate in ('" + getTimeRange4Month()
				+ "') and var in (" + getInputFeatureList() + ") order by ts desc limit " + INPUT_FEAT_CACH_SIZ;
		logger.info(sql);
		Stream<InputFeature> stream = dataLake.runQuery(sql, dataLake.inputFeatureFromRow()).stream();
		stream.forEach(f -> addToCache(f, f.getKey(), inputFeatureCache));
		long t2 = System.currentTimeMillis();
		logger.info("caching algo-input features done. It took " + (t2 - t1) + " ms for " + inputFeatureCache.size()
				+ " elements.");
	}

	private String formatMonth(int month) {
		String str = month + "";
		int len = str.length();
		if (len < 2) {
			str = "0" + str;
		}
		return str;
	}

	private String getTimeRange4Month() {
		LocalDate now = LocalDate.now();
		String dateKey = now.getYear() + "-" + formatMonth(now.getMonth().getValue());
		if (now.getDayOfMonth() < 14) {
			if (now.getMonthValue() > 1) {
				dateKey += "','" + now.getYear() + "-" + formatMonth(now.getMonth().getValue() - 1);
			} else {
				dateKey += "','" + (now.getYear() - 1) + "-" + (12);
			}
		}
		return dateKey;
	}

	private String getTimeRange4Year() {
		LocalDate now = LocalDate.now();
		String dateKey = now.getYear() + "";
		if (now.getMonthValue() < 2) {
			dateKey += "','" + (now.getYear() - 1);
		}
		return dateKey;
	}

}
