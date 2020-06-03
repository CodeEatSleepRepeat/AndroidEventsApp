package rs.ac.uns.ftn.eventsapp.dtos;

public class SimilarEventDTO {

	private String eventName;
	private String eventImg;

	public SimilarEventDTO(){}

	public SimilarEventDTO(String eventName, String eventImg) {
		this.eventName = eventName;
		this.eventImg = eventImg;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventImg() {
		return eventImg;
	}

	public void setEventImg(String eventImg) {
		this.eventImg = eventImg;
	}
}
