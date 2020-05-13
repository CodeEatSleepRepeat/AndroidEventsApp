package rs.ac.uns.ftn.eventsapp.dtos;

import java.util.Date;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;

public class CreateEventDTO {

    private float latitude;
    private float longitude;
    private String name;
    private String place;
    private String description;
    private EventType type;
    private String start_time;
    private String end_time;
    private FacebookPrivacy privacy;
    //private Owner owner;


    public CreateEventDTO() {
    }

    public CreateEventDTO(float latitude, float longitude, String name, String place, String description, EventType type, String start_time, String end_time, FacebookPrivacy privacy) {
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public FacebookPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(FacebookPrivacy privacy) {
        this.privacy = privacy;
    }
}
