package botsandbytes.dashboard.backend.response;

public class DashboardData {

	private String objectid;
	private String origin;
	private String time;
	private String target;
	private Integer value;

	private boolean moved = false;

	public DashboardData(String objectid, String origin, String component, String time, String val) {
		this.objectid = objectid;
		this.origin = origin;
		this.time = time;
		this.target = component.split("\\.")[0];
		try {
			this.value = Integer.parseInt(val);
		} catch (Exception e) {
			this.value = -1;
		}
	}

	public boolean isMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public String getObjectid() {
		return objectid;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
