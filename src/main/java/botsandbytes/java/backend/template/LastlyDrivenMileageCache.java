package botsandbytes.java.backend.template;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastlyDrivenMileageCache {

	@Autowired
	Config config;

	DataLakeAccess dataLake;

	@PostConstruct
	private void init() {
		this.dataLake = config.getDataLakeAccess();
	}

	public Map<String, Integer> update() throws SQLException, InterruptedException {
		return dataLake.getMovements(config.getMILEAGE_THRESHOLD());

	}

}
