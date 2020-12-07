package botsandbytes.java.backend.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import botsandbytes.java.backend.template.athena.AthenaAccess;
import botsandbytes.java.backend.template.presto.PrestoAccess;

import javax.annotation.PostConstruct;

@Service
public class Config {

	private boolean cloud = false;

	@Value("${DB_BACKEND}")
	private String DB_BACKEND;

	@Value("${MOCK_S3}")
	private Boolean MOCK_S3;

	@Value("${CACHE_ALGO_INPUT_FEATURES}")
	private Boolean CACHE_ALGO_INPUT_FEATURES;

	@Value("${CACHE_ALGO_OUTPUT_FEATURES}")
	private Boolean CACHE_ALGO_OUTPUT_FEATURES;

	@Value("${INPUT_FEATURES}")
	private String INPUT_FEATURES;

	@Value("${MILEAGE_THRESHOLD}")
	private Integer MILEAGE_THRESHOLD;

	@Value("${ATHENA_S3_LOCATION}")
	private String ATHENA_S3_LOCATION;

	@Value("${EXPECTANCY_S3_BUCKET}")
	private String EXPECTANCY_S3_BUCKET;

	@Value("${EXPECTANCY_S3_KEY}")
	private String EXPECTANCY_S3_KEY;

	public DataLakeAccess getDataLakeAccess() {
		if ("PRESTO".equals(EnvAdapt.getEnv(cloud, "DB_BACKEND", DB_BACKEND))) {
			return new PrestoAccess();
		} else {
			return new AthenaAccess(this.ATHENA_S3_LOCATION);
		}
	}

	public Boolean isS3Mocked() {
		return EnvAdapt.getEnv(cloud, "MOCK_S3", MOCK_S3);
	}

	public Boolean getCACHE_ALGO_INPUT_FEATURES() {
		return EnvAdapt.getEnv(cloud, "CACHE_ALGO_INPUT_FEATURES", CACHE_ALGO_INPUT_FEATURES);
	}

	public Boolean getCACHE_ALGO_OUTPUT_FEATURES() {
		return EnvAdapt.getEnv(cloud, "CACHE_ALGO_OUTPUT_FEATURES", CACHE_ALGO_OUTPUT_FEATURES);
	}

	public String getINPUT_FEATURES() {
		return EnvAdapt.getEnv(cloud, "INPUT_FEATURES", INPUT_FEATURES);
	}

	public String getEXPECTANCY_S3_BUCKET() {
		return EnvAdapt.getEnv(cloud, "EXPECTANCY_S3_BUCKET", EXPECTANCY_S3_BUCKET);
	}

	public String getEXPECTANCY_S3_KEY() {
		return EnvAdapt.getEnv(cloud, "EXPECTANCY_S3_KEY", EXPECTANCY_S3_KEY);
	}

	public String getATHENA_S3_LOCATION() {
		return EnvAdapt.getEnv(cloud, "ATHENA_S3_LOCATION", ATHENA_S3_LOCATION);
	}

	public Integer getMILEAGE_THRESHOLD() {
		return EnvAdapt.getEnv(cloud, "MILEAGE_THRESHOLD", MILEAGE_THRESHOLD);
	}

	@PostConstruct
	private void determineRuntimeEnv() {
		if (null != System.getenv("RUNTIME")) {
			cloud = true;
		}
	}
}
