package rs.ac.uns.ftn.eventsapp.dtos;

import java.util.Date;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.dtos.OwnerCreateEventDTO;

public class CreateEventDTO {

    private double latitude;
    private double longitude;
    private String name;
    private String place;
    private String description;
    private EventType type;
    private String start_time;
    private String end_time;
    private FacebookPrivacy privacy;
    private OwnerCreateEventDTO owner;


    public CreateEventDTO() {
    }

    public CreateEventDTO(double latitude, double longitude, String name, String place, String description,
                          EventType type, String start_time, String end_time, FacebookPrivacy privacy, OwnerCreateEventDTO owner) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.place = place;
        this.description = description;
        this.type = type;
        this.start_time = start_time;
        this.end_time = end_time;
        this.privacy = privacy;
        this.owner = owner;
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

    public OwnerCreateEventDTO getOwnerCreateEventDTO() {
        return owner;
    }

    public void setOwnerCreateEventDTO(OwnerCreateEventDTO owner) {
        this.owner = owner;
    }
}
