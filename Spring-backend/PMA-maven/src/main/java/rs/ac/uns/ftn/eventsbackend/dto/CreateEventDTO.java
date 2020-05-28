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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
	
	private double latitude;
	private double longitude;
	
	@NotNull
	@Size(min = 1, max = 64)
	private String name;
	@NotNull
	private String place;
	@NotNull
	private String description;
	@NotNull
	private EventType type;
	@NotNull
	@Column(name = "start_time", columnDefinition = "DATETIME")
	private ZonedDateTime start_time;
	@NotNull
	private ZonedDateTime end_time;
	@NotNull
	private FacebookPrivacy privacy;
	@NotNull
	private Long owner;

}
