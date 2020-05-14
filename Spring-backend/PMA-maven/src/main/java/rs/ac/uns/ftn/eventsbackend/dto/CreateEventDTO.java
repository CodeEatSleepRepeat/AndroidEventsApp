package rs.ac.uns.ftn.eventsbackend.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.eventsbackend.enums.EventType;
import rs.ac.uns.ftn.eventsbackend.enums.FacebookPrivacy;
import rs.ac.uns.ftn.eventsbackend.model.Owner;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
	
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

}
