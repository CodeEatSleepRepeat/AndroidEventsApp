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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;

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
	@Size(min = 1)
	private String description;

	@OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Cover cover;

	@NotNull
	private EventType type;

	@NotNull
	private FacebookPrivacy privacy;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "start_time", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date start_time;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "end_time", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date end_time;

	private String place;

	private float latitude;

	private float longitude;

	@NotNull
	@OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Owner owner;

	@ManyToMany(mappedBy = "interestedEvents", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<User> interested;

	@ManyToMany(mappedBy = "goingEvents", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<User> going;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitation> userInvitation;
	
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	@JsonProperty("updated_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@UpdateTimestamp
	private Date updated_time;

	@JsonProperty("created_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@CreationTimestamp
	private Date created_time;
	
	private Boolean isDeleted;

	// FB attributes
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date updated_timeFB;
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
	}
}
