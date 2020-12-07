package botsandbytes.java.backend.template.storage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Expectancy {

	Logger logger = LogManager.getLogger(this.getClass());

	private S3Access acc;
	private String bucket;
	private String key;

	public Expectancy(Boolean mockingMode, String bucket, String key) {
		this.bucket = bucket;
		this.key = key;
		if (mockingMode) {
			String testDataDir = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/test/resources";
			this.acc = new LocalFsAccess(false, testDataDir);
		} else {
			this.acc = new S3Access();
		}
	}

	public String get() {
		byte[] bytes = acc.get(this.bucket, this.key);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public void stop() {
		acc.stop();
	}
}
