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

import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotNull
	private String userName;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	private String password;
	
	@SuppressWarnings("unused")
	private String facebookToken;
	
	@OneToMany(mappedBy="requestSender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> sendRequests;
	
	@OneToMany(mappedBy="requestReceiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> receivedRequests;
	
	@OneToMany(mappedBy="invitationSender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> sendInvitations;
	
	@OneToMany(mappedBy="invitationReceiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> receivedInvitations;
	
	@OneToMany(mappedBy="author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Event> userEvents;
	
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
	
	@SuppressWarnings("unused")
	private Boolean activatedAccount;
	

	public User(Long userId, @NotNull String userName, @NotNull @Email String email, @NotNull String password,
			String facebookToken, Boolean activatedAccount) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.facebookToken = facebookToken;
		this.activatedAccount = activatedAccount;
		
		this.sendRequests = new ArrayList<>();
		this.receivedRequests = new ArrayList<>();
		this.sendInvitations = new ArrayList<>();
		this.receivedInvitations = new ArrayList<>();
		this.userEvents = new ArrayList<>();
		this.interestedEvents = new ArrayList<>();
		this.goingEvents = new ArrayList<>();
	}
	
}
