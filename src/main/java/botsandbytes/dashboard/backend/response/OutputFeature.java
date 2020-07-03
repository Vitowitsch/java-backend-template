package botsandbytes.dashboard.backend.response;

import java.util.function.Supplier;

public class OutputFeature {

	private String train;
	private String component;
	private String algo;
	private String value;
	private String valueName;
	private String timeStamp;

	public OutputFeature(String train, String component, String algo, String valueName, String value,
			String timeStamp) {
		this.train = train;
		this.component = component;
		this.algo = algo;
		this.value = value;
		this.valueName = valueName;
		this.timeStamp = timeStamp;
	}

	public Supplier<String> getKey() {
		return () -> this.train + this.component + this.algo;
	};

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTrain() {
		return train;
	}

	public void setTrain(String train) {
		this.train = train;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getAlgo() {
		return algo;
	}

	public void setAlgo(String algo) {
		this.algo = algo;
	}

	@Override
	public String toString() {
		return "State [train=" + train + ", component=" + component + ", algo=" + algo + ", value=" + value
				+ ", valueName=" + valueName + ", timeStamp=" + timeStamp + "]";
	}

}
