package rs.ac.uns.ftn.eventsbackend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	private String facebookId;
	
	@NotNull
	@Size(min = 1, max = 64)
	private String userName;

	private String userImageURI;
	
	private Integer imageHeight;
	
	private Integer imageWidth;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	private String password;	//TODO: add password validation annotation
	
	@OneToMany(mappedBy="requestSender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> sendRequests;
	
	@OneToMany(mappedBy="requestReceiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> receivedRequests;
	
	@OneToMany(mappedBy="invitationSender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> sendInvitations;
	
	@OneToMany(mappedBy="invitationReceiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> receivedInvitations;
	
	@OneToMany(mappedBy="sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<ChatMessage> chatMessagesSent;
	
	@OneToMany(mappedBy="recipient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<ChatMessage> chatMessagesReceived;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable (
        name = "InterestedEvents", 
        joinColumns = { @JoinColumn(name = "userId") }, 
        inverseJoinColumns = { @JoinColumn(name = "eventId") }
    )
	private List<Event> interestedEvents;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "GoingEvents", 
        joinColumns = { @JoinColumn(name = "userId") }, 
        inverseJoinColumns = { @JoinColumn(name = "eventId") }
    )
	private List<Event> goingEvents;
	
	private Boolean activatedAccount;
	
	private Boolean useFacebookProfile;
	
	/**
	 * Za register preko emaila i passworda
	 * @param userName
	 * @param email
	 * @param password
	 */
	public User(@NotNull String userName, @NotNull @Email String email, @NotNull String password) {
		super();
		this.userId = null;
		this.facebookId = "";
		this.userName = userName;
		this.userImageURI = "";
		this.imageHeight = null;
		this.imageWidth = null;
		this.email = email;
		this.password = password;
		this.sendRequests = new ArrayList<Friendship>();
		this.receivedRequests = new ArrayList<Friendship>();
		this.sendInvitations = new ArrayList<Invitation>();
		this.receivedInvitations = new ArrayList<Invitation>();
		this.interestedEvents = new ArrayList<Event>();
		this.goingEvents = new ArrayList<Event>();
		this.activatedAccount = false;
		this.useFacebookProfile = false;
	}
	
	public User() {
		
	}
	
}
