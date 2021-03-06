package rs.ac.uns.ftn.eventsapp.dtos;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Date;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;

public class CreateEventDTO {

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


    public CreateEventDTO() {
    }

    public CreateEventDTO(double latitude, double longitude, String name, String place, String description,
                          EventType type, Date start_time, Date end_time, FacebookPrivacy privacy, Long owner) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.place = place;
        this.description = description;
        this.type = type;
        this.start_time = getZonedTime(start_time);
        this.end_time = getZonedTime(end_time);
        this.privacy = privacy;
        this.owner = owner;
    }

    private ZonedDateTime getZonedTime(Date updated_time) {
        return ZonedDateTime.of(updated_time.getYear()+1900, updated_time.getMonth()+1, updated_time.getDate(), updated_time.getHours(), updated_time.getMinutes(), updated_time.getSeconds(), 0, ZoneId.systemDefault());
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

    public ZonedDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(ZonedDateTime start_time) {
        this.start_time = start_time;
    }

    public ZonedDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(ZonedDateTime end_time) {
        this.end_time = end_time;
    }

    public FacebookPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(FacebookPrivacy privacy) {
        this.privacy = privacy;
    }

    public Long getOwnerCreateEventDTO() {
        return owner;
    }

    public void setOwnerCreateEventDTO(Long owner) {
        this.owner = owner;
    }
}
