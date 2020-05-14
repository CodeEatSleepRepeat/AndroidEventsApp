package rs.ac.uns.ftn.eventsapp.dtos;

import java.util.Date;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;

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
    //private Owner owner;

    public EventDTO() {
    }

    public EventDTO(Long id, double latitude, double longitude, String name, String place, String description, EventType type, Date start_time, Date end_time, FacebookPrivacy privacy) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.place = place;
        this.description = description;
        this.type = type;
        this.start_time = start_time;
        this.end_time = end_time;
        this.privacy = privacy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public FacebookPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(FacebookPrivacy privacy) {
        this.privacy = privacy;
    }
}