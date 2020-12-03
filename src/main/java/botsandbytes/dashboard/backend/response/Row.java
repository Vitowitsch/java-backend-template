package botsandbytes.dashboard.backend.response;

public class Row {

	private String origin;
	private String objectid;
	private String diagid;
	private Integer lastResult_HS;
	private String carnumber;
	private String boogieWoogie;
	private String axis;
	private String comp;
	private String lastResultTime;
	private String lastExecTime;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getObjectid() {
		return objectid;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}

	public void setLastResultTime(String lastResultTime) {
		this.lastResultTime = lastResultTime;
	}

	public void setLastExecTime(String lastExecTime) {
		this.lastExecTime = lastExecTime;
	}

	public String getLastResultTime() {
		return lastResultTime;
	}

	public String getLastExecTime() {
		return lastExecTime;
	}

	public String getDiagid() {
		return diagid;
	}

	public void setDiagid(String diagid) {
		this.diagid = diagid;
	}

	public int getLastResult_HS() {
		return lastResult_HS;
	}

	public void setLastResult_HS(int lastResult_HS) {
		this.lastResult_HS = lastResult_HS;
	}

	public String getCarnumber() {
		return carnumber;
	}

	public void setCarnumber(String carnumber) {
		this.carnumber = carnumber;
	}

	public String getBoogieWoogie() {
		return boogieWoogie;
	}

	public void setBoogieWoogie(String boogieWoogie) {
		this.boogieWoogie = boogieWoogie;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public void setLastResult_HS(Integer lastResult_HS) {
		this.lastResult_HS = lastResult_HS;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public Row(String origin, String objectid, String diagid, int lastResult_HS, String carnumber, String boogieWoogie,
			String axis, String comp, String lastResultTime, String lastExecTime) {
		this.origin = origin;
		this.objectid = objectid;
		this.diagid = diagid;
		this.lastResult_HS = lastResult_HS;
		this.carnumber = carnumber;
		this.boogieWoogie = boogieWoogie;
		this.axis = axis;
		this.comp = comp;
		this.lastResultTime = lastResultTime;
		this.lastExecTime = lastExecTime;
	}
}
