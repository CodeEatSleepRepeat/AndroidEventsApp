package rs.ac.uns.ftn.eventsbackend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String facebookId;

	@NotNull
	@Size(min = 1, max = 64)
	private String name;

	@Column(length = 500)
	private String imageUri;

	private Integer imageHeight;

	private Integer imageWidth;

	@NotNull
	@Email
	private String email;

	@NotNull
	private String password; // TODO: add password validation annotation

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "requestSender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> sendRequests;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "requestReceiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> receivedRequests;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "invitationSender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> sendInvitations;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "invitationReceiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> receivedInvitations;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> chatMessagesSent;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> chatMessagesReceived;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "InterestedEvents", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = {
			@JoinColumn(name = "eventId") })
	private List<Event> interestedEvents;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "GoingEvents", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = {
			@JoinColumn(name = "eventId") })
	private List<Event> goingEvents;
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Event> userEvents;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	private Boolean activatedAccount;

	private Boolean syncFacebookEvents;

	private Boolean syncFacebookProfile;

	/**
	 * Za registraciju preko email-a, imena i passworda
	 * 
	 * @param userName
	 * @param email
	 * @param password
	 */
	public User(@NotNull String userName, @NotNull @Email String email, @NotNull String password) {
		super();
		this.id = null;
		this.facebookId = "";
		this.name = userName;
		this.imageUri = "";
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
		this.chatMessagesReceived = new ArrayList<ChatMessage>();
		this.chatMessagesSent = new ArrayList<ChatMessage>();
		this.comments = new ArrayList<Comment>();
		this.activatedAccount = false;
		this.syncFacebookEvents = false;
		this.syncFacebookProfile = false;
	}

	public User() {
		super();
		this.sendRequests = new ArrayList<Friendship>();
		this.receivedRequests = new ArrayList<Friendship>();
		this.sendInvitations = new ArrayList<Invitation>();
		this.receivedInvitations = new ArrayList<Invitation>();
		this.interestedEvents = new ArrayList<Event>();
		this.goingEvents = new ArrayList<Event>();
		this.chatMessagesReceived = new ArrayList<ChatMessage>();
		this.chatMessagesSent = new ArrayList<ChatMessage>();
		this.comments = new ArrayList<Comment>();
	}
}
