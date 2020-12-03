package botsandbytes.dashboard.backend;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastlyDrivenMileageCache {

	private Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	Config config;

	DataLakeAccess dataLake;

	@PostConstruct
	private void init() {
		this.dataLake = config.getDataLakeAccess();
	}

	public Map<String, Integer> update() throws SQLException, InterruptedException {
		logger.info("caching lastly driven mileages ...");
		long t1 = System.currentTimeMillis();
		Map<String, Integer> movements = dataLake.getMovements(config.getMILEAGE_THRESHOLD());
		long t2 = System.currentTimeMillis();
		logger.info("caching lastly driven mileages done. It took " + (t2 - t1) + " ms");
		return movements;
	}

}
