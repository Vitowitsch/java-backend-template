package botsandbytes.dashboard.backend.response;

public class Row {

	private String origin;
	private String objectid;
	private String fieldId;
	private Integer state;
	private String target;
	private String rootPos;
	private String position;
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

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String f) {
		this.fieldId = f;
	}

	public int getState() {
		return state;
	}

	public void setState(int s) {
		this.state = s;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String t) {
		this.target = t;
	}

	public String getRootPos() {
		return rootPos;
	}

	public String getPosition() {
		return position;
	}

	public void setRootPos(String p) {
		this.position = p;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public Row(String origin, String objectid, String fieldId, int state, String target, String rootPos, String pos,
			String comp, String lastResultTime, String lastExecTime) {
		this.origin = origin;
		this.objectid = objectid;
		this.fieldId = fieldId;
		this.state = state;
		this.target = target;
		this.rootPos = rootPos;
		this.position = pos;
		this.comp = comp;
		this.lastResultTime = lastResultTime;
		this.lastExecTime = lastExecTime;
	}
}
