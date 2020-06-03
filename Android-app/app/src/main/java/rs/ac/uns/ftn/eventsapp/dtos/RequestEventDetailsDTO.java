package rs.ac.uns.ftn.eventsapp.dtos;

public class RequestEventDetailsDTO {

	private Long userId;
	private Long eventId;

	public RequestEventDetailsDTO() {
	}

	public RequestEventDetailsDTO(Long userId, Long eventId) {
		this.userId = userId;
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
}
