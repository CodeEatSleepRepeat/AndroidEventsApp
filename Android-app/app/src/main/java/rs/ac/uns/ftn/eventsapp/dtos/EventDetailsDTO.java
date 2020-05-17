package rs.ac.uns.ftn.eventsapp.dtos;

import java.io.Serializable;
import java.util.Date;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;

public class EventDetailsDTO implements Serializable {

    private Long eventId;
    private String eventName;
    private String eventDescription;
    private String eventImageURI;
    private EventType eventType;
    private FacebookPrivacy openForAll;
    private Date startTime;
    private Date endTime;
    private String location;
    private double latitude;
    private double longitude;
    private Long author;

    public EventDetailsDTO(Long eventId, String eventName, String eventDescription,
                           String eventImageURI, EventType eventType, FacebookPrivacy openForAll, Date startTime, Date endTime,
                           String location, double latitude, double longitude, Long author) {
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
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImageURI() {
        return eventImageURI;
    }

    public void setEventImageURI(String eventImageURI) {
        this.eventImageURI = eventImageURI;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public FacebookPrivacy getOpenForAll() {
        return openForAll;
    }

    public void setOpenForAll(FacebookPrivacy openForAll) {
        this.openForAll = openForAll;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }
}
