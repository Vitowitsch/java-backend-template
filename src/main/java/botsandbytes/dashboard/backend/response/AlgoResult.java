package botsandbytes.dashboard.backend.response;

public class AlgoResult {
	private String car;
	private String hs;
	private String asset;
	private String okz;


	public String getOkz() {
		return okz;
	}

	public void setOkz(String okz) {
		this.okz = okz;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public AlgoResult(String car, String okz, String asset, String hs) {
		this.okz = okz;
		this.car = car;
		this.hs = hs;
		this.asset = asset;
	}

}
