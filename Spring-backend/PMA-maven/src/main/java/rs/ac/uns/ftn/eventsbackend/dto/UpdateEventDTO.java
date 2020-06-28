package rs.ac.uns.ftn.eventsbackend.dto;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDTO {
	
	private double latitude;
	private double longitude;
	
	@NotNull
	private Long id;
	@NotNull
	@Size(min = 1, max = 64)
	private String name;
	@NotNull
	@Column(length = 500)
	private String place;
	@NotNull
	@Size(min = 1, max = 10000)
	private String description;
	@NotNull
	private EventType type;
	@NotNull
	private ZonedDateTime start_time;
	@NotNull
	private ZonedDateTime end_time;
	@NotNull
	private FacebookPrivacy privacy;
	@NotNull
	private SyncStatus syncStatus;
	@NotNull
	private ZonedDateTime updated_time;
	
	private String imgUri;

}
