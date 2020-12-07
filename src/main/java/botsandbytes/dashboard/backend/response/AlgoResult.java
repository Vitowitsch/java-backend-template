package botsandbytes.dashboard.backend.response;

public class AlgoResult {
	private String target;
	private String state;
	private String asset;
	private String position;

	public String setPosition() {
		return position;
	}

	public void setPosition(String okz) {
		this.position = okz;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String car) {
		this.target = car;
	}

	public String getState() {
		return state;
	}

	public void setState(String hs) {
		this.state = hs;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public AlgoResult(String target, String position, String asset, String state) {
		this.position = position;
		this.target = target;
		this.state = state;
		this.asset = asset;
	}

}
