package rs.ac.uns.ftn.eventsapp.dtos;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Date;

import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.SyncStatus;

public class UpdateEventDTO {

    private Long id;
    private String name;
    private String description;
    private EventType type;
    private FacebookPrivacy privacy;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    private String place;
    private double latitude;
    private double longitude;
    private SyncStatus syncStatus;
    private ZonedDateTime updated_time;

    public UpdateEventDTO(Long id, double latitude, double longitude, String name, String place, String description, EventType type, Date start_time, Date end_time, FacebookPrivacy privacy, SyncStatus syncStatus, ZonedDateTime updated_time) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.place = place;
        this.description = description;
        this.type = type;
        this.start_time = getZonedTime(start_time);
        this.end_time = getZonedTime(end_time);
        this.privacy = privacy;
        this.syncStatus = syncStatus;
        this.updated_time = updated_time;
    }

    public UpdateEventDTO(Long id, double latitude, double longitude, String name, String place, String description, EventType type, ZonedDateTime start_time, ZonedDateTime end_time, FacebookPrivacy privacy, SyncStatus syncStatus, ZonedDateTime updated_time) {
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
        this.syncStatus = syncStatus;
        this.updated_time = updated_time;
    }

    private ZonedDateTime getZonedTime(Date updated_time) {
        return ZonedDateTime.of(updated_time.getYear()+1900, updated_time.getMonth()+1, updated_time.getDate(), updated_time.getHours(), updated_time.getMinutes(), updated_time.getSeconds(), 0, ZoneId.systemDefault());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public FacebookPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(FacebookPrivacy privacy) {
        this.privacy = privacy;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public ZonedDateTime getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(ZonedDateTime updated_time) {
        this.updated_time = updated_time;
    }
}
