package botsandbytes.dashboard.backend;

public class EnvAdapt {

	private EnvAdapt() {
	}

	public static String getEnv(boolean isCloud, String name, String value) {
		return isCloud ? System.getenv(name) : value;
	}

	public static Integer getEnv(boolean isCloud, String name, Integer value) {
		return isCloud ? Integer.parseInt(System.getenv(name)) : value;
	}

	public static Boolean getEnv(boolean isCloud, String name, Boolean value) {
		return isCloud ? Boolean.parseBoolean(System.getenv(name)) : value;
	}

}
