package botsandbytes.dashboard.backend.model;

/*holds the mileage difference driven within a configurable time span*/
public class DrivenCount {

	private String train;
	private Integer mileage;
	private String eventDate;
	private int threshold;

	public String getTrain() {
		return train;
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
		return this.train + " " + this.eventDate;
	}

	public DrivenCount(String train, String eventDate, Integer mileage, int threshold) {
		this.train = train;
		this.eventDate = eventDate;
		this.mileage = mileage;
		this.threshold = threshold;
	}

}
