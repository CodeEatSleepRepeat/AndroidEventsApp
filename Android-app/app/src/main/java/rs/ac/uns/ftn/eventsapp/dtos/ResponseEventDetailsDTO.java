package rs.ac.uns.ftn.eventsapp.dtos;

import java.util.List;

public class ResponseEventDetailsDTO {

	private Long userId;
	private Boolean going;
	private Boolean interested;
	private List<String> goingImages;
	private List<String> interestedImages;
	private String userImage;
	private String userName;
	private String userEmail;
	private Integer organizedEventsNum;
	private List<SimilarEventDTO> events;

	public ResponseEventDetailsDTO() {
	}

	public ResponseEventDetailsDTO(Long userId, Boolean going, Boolean interested, List<String> goingImages, List<String> interestedImages, String userImage, String userName, String userEmail, Integer organizedEventsNum, List<SimilarEventDTO> events) {
		this.userId = userId;
		this.going = going;
		this.interested = interested;
		this.goingImages = goingImages;
		this.interestedImages = interestedImages;
		this.userImage = userImage;
		this.userName = userName;
		this.userEmail = userEmail;
		this.organizedEventsNum = organizedEventsNum;
		this.events = events;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getGoing() {
		return going;
	}

	public void setGoing(Boolean going) {
		this.going = going;
	}

	public Boolean getInterested() {
		return interested;
	}

	public void setInterested(Boolean interested) {
		this.interested = interested;
	}

	public List<String> getGoingImages() {
		return goingImages;
	}

	public void setGoingImages(List<String> goingImages) {
		this.goingImages = goingImages;
	}

	public List<String> getInterestedImages() {
		return interestedImages;
	}

	public void setInterestedImages(List<String> interestedImages) {
		this.interestedImages = interestedImages;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getOrganizedEventsNum() {
		return organizedEventsNum;
	}

	public void setOrganizedEventsNum(Integer organizedEventsNum) {
		this.organizedEventsNum = organizedEventsNum;
	}

	public List<SimilarEventDTO> getEvents() {
		return events;
	}

	public void setEvents(List<SimilarEventDTO> events) {
		this.events = events;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
