package rs.ac.uns.ftn.eventsbackend.dto;

import java.time.ZonedDateTime;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;
import rs.ac.uns.ftn.eventsbackend.enums.SyncStatus;
import rs.ac.uns.ftn.eventsbackend.model.Event;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

	private Long id;
	private double latitude;
	private double longitude;
	private String name;
	private String place;
	private String description;
	private EventType type;
	private ZonedDateTime start_time;
	private ZonedDateTime end_time;
	private FacebookPrivacy privacy;
	private Long owner;
	private String imageUri;
	private SyncStatus syncStatus;
	private ZonedDateTime updated_time;
	private ZonedDateTime created_time;
	
	public EventDTO(Event event) {
		id = event.getId();
		latitude = event.getLatitude();
		longitude = event.getLongitude();
		name = event.getName();
		place = event.getPlace();
		description = event.getDescription();
		type = event.getType();
		start_time = event.getStart_time();
		end_time = event.getEnd_time();
		privacy = event.getPrivacy();
		owner = event.getOwner().getId();
		if(event.getCover()!=null) {
			imageUri = event.getCover().getSource();
		}
		syncStatus = event.getSyncStatus();
		updated_time = event.getUpdated_time();
		created_time = event.getCreated_time();
	}
	
}
