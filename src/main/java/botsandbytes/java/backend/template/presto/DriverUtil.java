package botsandbytes.java.backend.template.presto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

@Component
public class DriverUtil {

	private static final String PRESTO_DRV = "com.facebook.presto.jdbc.PrestoDriver";

	Logger logger = LogManager.getLogger(this.getClass());

	private static volatile boolean init = false;

	public DriverUtil() {
		if (!init) {
			loadDriver();
			init = true;
		}
	}

	private void loadDriver() {
		try {
			Object dr = Class.forName(PRESTO_DRV).newInstance();
			DriverManager.registerDriver((Driver) dr);
		} catch (Exception e) {
			logger.error("error connecting to hive: " + e.getMessage(), e);
		}
	}

	public Connection getConnection(String con, String user, String password) throws Exception {
		return DriverManager.getConnection(con, user, password);
	}

	public Connection getPresto() {
		try {
			return getConnection("jdbc:presto://localhost:150/metastore", "***",
					"");
		} catch (Exception e) {
			logger.error("could not get presto con: " + e.getMessage(), e);
		}
		return null;
	}

}
