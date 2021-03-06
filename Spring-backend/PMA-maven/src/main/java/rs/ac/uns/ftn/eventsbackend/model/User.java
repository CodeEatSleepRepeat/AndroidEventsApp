package rs.ac.uns.ftn.eventsbackend.model;

import java.time.ZonedDateTime;
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
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 500)
	private String facebookId;
	
	@Column(length = 500)
	private String facebookToken;

	@NotNull
	@Pattern(regexp = "^\\p{L}+[\\p{L} .'-]{2,64}$")
	private String name;

	@Column(length = 500)
	private String imageUri;

	@NotNull
	@Email
	private String email;

	@NotNull
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])((?=.*[@#$%^&+=!])|(?=.*[0-9]))(?=\\S+$).{7,}$")
	private String password;

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
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "InterestedEvents", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = {
			@JoinColumn(name = "eventId") })
	private List<Event> interestedEvents;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
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
	
	private SyncStatus syncStatus;
	
	@UpdateTimestamp
	private ZonedDateTime updated_time;

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
		this.facebookToken = "";
		this.name = userName;
		this.imageUri = "";
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
		this.syncStatus = SyncStatus.UPDATE;
		this.updated_time = ZonedDateTime.now();
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
		this.syncStatus = SyncStatus.UPDATE;
		this.updated_time = ZonedDateTime.now();
	}
}
