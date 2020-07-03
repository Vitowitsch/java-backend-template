package botsandbytes.dashboard.backend.response;

public class Row {

	private String origin;
	private String objectid;
	private String diagid;
	private Integer lastResult_HS;
	private String lastResult_RUL;
	private String carnumber;
	private String bogie;
	private String axis;
	private String comp;
	private int level_0;
	private int level_1;
	private int level_2;
	private int level_3;

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

	public String getLastResult_RUL() {
		return lastResult_RUL;
	}

	public void setLastResult_RUL(String lastResult_RUL) {
		this.lastResult_RUL = lastResult_RUL;
	}

	public String getCarnumber() {
		return carnumber;
	}

	public void setCarnumber(String carnumber) {
		this.carnumber = carnumber;
	}

	public String getBogie() {
		return bogie;
	}

	public void setBogie(String bogie) {
		this.bogie = bogie;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public int getLevel_0() {
		return level_0;
	}

	public void setLevel_0(int level_0) {
		this.level_0 = level_0;
	}

	public int getLevel_1() {
		return level_1;
	}

	public void setLevel_1(int level_1) {
		this.level_1 = level_1;
	}

	public int getLevel_2() {
		return level_2;
	}

	public void setLevel_2(int level_2) {
		this.level_2 = level_2;
	}

	public int getLevel_3() {
		return level_3;
	}

	public void setLevel_3(int level_3) {
		this.level_3 = level_3;
	}

	public Row(String origin, String objectid, String diagid, int lastResult_HS, String lastResult_RUL, String carnumber,
			String bogie, String axis, String comp, int level_0, int level_1, int level_2, int level_3) {
		super();
		this.origin = origin;
		this.objectid = objectid;
		this.diagid = diagid;
		this.lastResult_HS = lastResult_HS;
		this.lastResult_RUL = lastResult_RUL;
		this.carnumber = carnumber;
		this.bogie = bogie;
		this.axis = axis;
		this.comp = comp;
		this.level_0 = level_0;
		this.level_1 = level_1;
		this.level_2 = level_2;
		this.level_3 = level_3;
	}

}
