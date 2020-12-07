package botsandbytes.dashboard.backend.model;

/*holds the mileage difference driven within a configurable time span*/
public class DrivenCount {

	private String objectId;
	private Integer mileage;
	private String eventDate;
	private int threshold;

	public String getObjectId() {
		return objectId;
	}

	public String getEventDate() {
		return eventDate;
	}

	public Boolean wasMoved() {
		return mileage > threshold;
	}

	public Integer getMileage() {
		return mileage;
	}

	public String getId() {
		return this.objectId + " " + this.eventDate;
	}

	public DrivenCount(String objectId, String eventDate, Integer mileage, int threshold) {
		this.objectId = objectId;
		this.eventDate = eventDate;
		this.mileage = mileage;
		this.threshold = threshold;
	}

}
