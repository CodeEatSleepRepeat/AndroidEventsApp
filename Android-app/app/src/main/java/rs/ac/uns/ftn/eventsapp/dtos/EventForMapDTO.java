package rs.ac.uns.ftn.eventsapp.dtos;

import java.io.Serializable;

public class EventForMapDTO implements Serializable {

    private Long eventId;
    private String eventName;
    private double latitude;
    private double longitude;
    private String eventImageURI;

    public EventForMapDTO(Long eventId, String eventName, double latitude, double longitude, String eventImageURI) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventImageURI = eventImageURI;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getEventImageURI() {
        return eventImageURI;
    }

    public void setEventImageURI(String eventImageURI) {
        this.eventImageURI = eventImageURI;
    }
}
