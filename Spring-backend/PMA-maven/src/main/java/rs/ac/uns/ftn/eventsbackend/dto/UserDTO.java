package rs.ac.uns.ftn.eventsbackend.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;
import rs.ac.uns.ftn.eventsbackend.model.User;

@Data
public class UserDTO {

	private Long id;
	private String facebookId;
	private String name;
	private String imageUri;
	private Integer imageHeight;
	private Integer imageWidth;
	private String email;
	private String password;

	private List<Long> sendRequests;
	private List<Long> receivedRequests;
	private List<Long> sendInvitations;
	private List<Long> receivedInvitations;
	private List<Long> chatMessagesSent;
	private List<Long> chatMessagesReceived;
	private List<Long> interestedEvents;
	private List<Long> goingEvents;
	private List<Long> comments;

	private Boolean activatedAccount;
	private Boolean syncFacebookEvents;
	private Boolean syncFacebookProfile;
	
	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.facebookId = user.getFacebookId();
		this.name = user.getName();
		this.imageUri = user.getImageUri();
		this.imageHeight = user.getImageHeight();
		this.imageWidth = user.getImageWidth();
		this.email = user.getEmail();
		this.password = user.getPassword();
		
		this.sendRequests = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.receivedRequests = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.sendInvitations = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.receivedInvitations = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.interestedEvents = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.goingEvents = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.chatMessagesReceived = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.chatMessagesSent = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.comments = new ArrayList<Long>();
		for (Friendship object : user.getSendRequests()) {
			sendRequests.add(object.getId());
		}
		
		this.activatedAccount = user.getActivatedAccount();
		this.syncFacebookEvents = user.getSyncFacebookEvents();
		this.syncFacebookProfile = user.getSyncFacebookProfile();
	}
	
}
