package rs.ac.uns.ftn.eventsbackend.dto;

import rs.ac.uns.ftn.eventsbackend.model.Event;

/**
 * Class that contains Events and distance from coordinate center
 * 
 * @author Boris
 *
 */
public class EventDistanceDTO {

	private Event e;

	private double distance;

	public EventDistanceDTO(Event e, double distance) {
		super();
		this.e = e;
		this.distance = distance;
	}

	public EventDistanceDTO() {
	}

	public float getDistanceFloat() {
		return (float) distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Event getE() {
		return e;
	}

	public double getDistance() {
		return distance;
	}

	public void setE(Event e) {
		this.e = e;
	}

}
