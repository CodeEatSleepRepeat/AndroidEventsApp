package rs.ac.uns.ftn.eventsbackend.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;

@Entity
@Data
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long eventId;
	
	@NotNull
	private String eventName;
	
	@NotNull
	private String eventDescription;
	
	@SuppressWarnings("unused")
	private String eventImageURI;
	
	@NotNull
	private EventType eventType;
	
	@NotNull
	private Boolean openForAll;
	
	@NotNull
	@Column(name = "startTime", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "endTime", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@SuppressWarnings("unused")
	private String location;
	
	@SuppressWarnings("unused")
	private Long latitude;
	
	@SuppressWarnings("unused")
	private Long longitude;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private User author;
	
	@ManyToMany(mappedBy="interestedEvents", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<User> interested;
	
	@ManyToMany(mappedBy="goingEvents", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<User> going;
	
	@OneToMany(mappedBy="event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Invitation> userInvitation;
	

	public Event(Long eventId, @NotNull String eventName, @NotNull String eventDescription, String eventImageURI,
			@NotNull EventType eventType, @NotNull Boolean openForAll, @NotNull Date startTime, Date endTime, String location, Long latitude,
			Long longitude, @NotNull User author) {
		super();
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventImageURI = eventImageURI;
		this.eventType = eventType;
		this.openForAll = openForAll;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.author = author;
		
		this.interested = new ArrayList<>();
		this.going = new ArrayList<>();
		this.userInvitation = new ArrayList<>();
	}
}
