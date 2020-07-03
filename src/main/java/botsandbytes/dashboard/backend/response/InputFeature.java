package botsandbytes.dashboard.backend.response;

import java.util.function.Supplier;

public class InputFeature {

	private String car;
	private String var;
	private String val;
	private String ts;
	private String configVersion;
	private String softwareVersion;

	public Supplier<String> getKey() {
		return () -> this.car + this.var;
	};

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public InputFeature(String car, String var, String val, String ts, String configVersion, String softwareVersion) {
		super();
		this.car = car;
		this.var = var;
		this.val = val;
		this.ts = ts;
		this.configVersion = configVersion;
		this.softwareVersion = softwareVersion;
	}

}
