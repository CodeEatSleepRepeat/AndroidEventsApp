package rs.ac.uns.ftn.eventsbackend.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;
import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.Owner;

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
	private Date start_time;
	private Date end_time;
	private FacebookPrivacy privacy;
	private Owner owner;
	private String imageUri;
	
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
		owner = event.getOwner();
		imageUri = event.getCover().getSource();
	}
	
}
