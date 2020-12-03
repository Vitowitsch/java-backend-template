package botsandbytes.dashboard.backend.storage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import botsandbytes.dashboard.backend.Config;

@Service
public class Expectancy {

	@Autowired
	Config config;

	private Logger logger = LogManager.getLogger(this.getClass());

	public String get(Boolean mocked) {
		byte[] bytes;
		if (mocked) {
			String testDataDir = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/test/resources";
			LocalFsAccess a = new LocalFsAccess(false, testDataDir);
			bytes = a.get("bucket", "expected_outcome.json");
			a.stop();

		} else {
			String fileLocation = config.getEXPECTANCY_S3_BUCKET() + config.getEXPECTANCY_S3_KEY();
			logger.info("downloading " + fileLocation);
			bytes = new S3Access().get(config.getEXPECTANCY_S3_BUCKET(), config.getEXPECTANCY_S3_KEY());
		}
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
