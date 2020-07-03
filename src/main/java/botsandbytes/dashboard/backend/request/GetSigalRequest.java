package botsandbytes.dashboard.backend.request;

import java.io.Serializable;
import java.util.List;

public class GetSigalRequest implements Serializable {

	private static final long serialVersionUID = -8552229226375305413L;

	private String train;
	public String getTrain() {
		return train;
	}

	public void setTrain(String train) {
		this.train = train;
	}

	private String car;

	private List<String> signals;

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public List<String> getSignals() {
		return signals;
	}

	public void setSignals(List<String> signals) {
		this.signals = signals;
	}

	public GetSigalRequest(String train, String car, List<String> signals) {
		this.train = train;
		this.car = car;
		this.signals = signals;
	}

}