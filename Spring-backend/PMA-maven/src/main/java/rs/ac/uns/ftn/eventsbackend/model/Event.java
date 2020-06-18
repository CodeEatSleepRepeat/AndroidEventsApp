package rs.ac.uns.ftn.eventsbackend.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.dto.CreateEventDTO;
import rs.ac.uns.ftn.eventsbackend.dto.UpdateEventDTO;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;

@Entity
@Data
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String facebookId;

	@NotNull
	@Size(min = 1, max = 64)
	private String name;

	@NotNull
	@Size(min = 1, max = 10000)
	private String description;

	@OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Cover cover;

	@NotNull
	private EventType type;

	@NotNull
	private FacebookPrivacy privacy;

	@NotNull
	private ZonedDateTime start_time;

	@NotNull
	private ZonedDateTime end_time;

	@Column(length = 500)
	private String place;

	private double latitude;

	private double longitude;

	//@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User owner;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@ManyToMany(mappedBy = "interestedEvents", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<User> interested;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@ManyToMany(mappedBy = "goingEvents", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<User> going;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<Invitation> userInvitation;
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<Comment> comments;

	@UpdateTimestamp
	private ZonedDateTime updated_time;

	@CreationTimestamp
	private ZonedDateTime created_time;
	
	@NotNull
	private SyncStatus syncStatus;

	// FB attributes
	private ZonedDateTime updated_timeFB;
	private Long attending_countFB;
	private Boolean guest_list_enabledFB;
	private Long declined_count;
	private Boolean can_guests_invite;
	private Long maybe_count;
	private Boolean is_canceled;
	private String timezone;
	private Long interested_count;
	private Boolean is_online;

	public Event() {
		super();
		comments = new ArrayList<Comment>();
		going = new ArrayList<User>();
		interested = new ArrayList<User>();
		userInvitation = new ArrayList<Invitation>();
		this.syncStatus = SyncStatus.UPDATE;
		this.updated_time = ZonedDateTime.now();
	}
	
	public Event(CreateEventDTO dto) {
		latitude = dto.getLatitude();
		longitude = dto.getLongitude();
		name = dto.getName();
		place = dto.getPlace();
		description = dto.getDescription();
		type = dto.getType();
		start_time = dto.getStart_time();
		end_time = dto.getEnd_time();
		privacy = dto.getPrivacy();
		this.syncStatus = SyncStatus.ADD;
		this.updated_time = ZonedDateTime.now();
	}

	public void update(@Valid UpdateEventDTO androidEvent) {
		latitude = androidEvent.getLatitude();
		longitude = androidEvent.getLongitude();
		name = androidEvent.getName();
		place = androidEvent.getPlace();
		description = androidEvent.getDescription();
		type = androidEvent.getType();
		start_time = androidEvent.getStart_time();
		end_time = androidEvent.getEnd_time();
		privacy = androidEvent.getPrivacy();
		this.syncStatus = androidEvent.getSyncStatus();
		this.updated_time = ZonedDateTime.now();
	}
}
