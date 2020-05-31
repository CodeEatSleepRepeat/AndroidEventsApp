package rs.ac.uns.ftn.eventsapp.dtos;

public class CreateCommentDTO {

	private String text;
	private Long eventId;
	private Long userId;

	public CreateCommentDTO(){}

	public CreateCommentDTO(String text, Long eventId, Long userId) {
		this.text = text;
		this.eventId = eventId;
		this.userId = userId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
