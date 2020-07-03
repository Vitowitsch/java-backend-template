package botsandbytes.dashboard.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import botsandbytes.dashboard.athena.AthenaAccess;
import botsandbytes.dashboard.presto.PrestoAccess;

import javax.annotation.PostConstruct;

@Service
public class Config {

	private boolean cloud = false;

	@Value("${DB_BACKEND}")
	private String DB_BACKEND;

	@Value("${CACHE_ALGO_INPUT_FEATURES}")
	private Boolean CACHE_ALGO_INPUT_FEATURES;

	@Value("${CACHE_ALGO_OUTPUT_FEATURES}")
	private Boolean CACHE_ALGO_OUTPUT_FEATURES;

	@Value("${INPUT_FEATURES}")
	private String INPUT_FEATURES;

	@Value("${ATHENA_S3_LOCATION}")
	private String ATHENA_S3_LOCATION;

	public DataLakeAccess getDataLakeAccess() {
		if ("PRESTO".equals(EnvAdapt.getEnv(cloud, "DB_BACKEND", DB_BACKEND))) {
			return new PrestoAccess();
		} else {
			return new AthenaAccess(this.ATHENA_S3_LOCATION);
		}
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

	public String getATHENA_S3_LOCATION() {
		return EnvAdapt.getEnv(cloud, "ATHENA_S3_LOCATION", ATHENA_S3_LOCATION);
	}

	@PostConstruct
	private void determineRuntimeEnv() {
		if (null != System.getenv("RUNTIME")) {
			cloud = true;
		}
	}
}
