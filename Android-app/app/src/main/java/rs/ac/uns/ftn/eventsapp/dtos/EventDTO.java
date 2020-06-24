package rs.ac.uns.ftn.eventsapp.dtos;

import org.threeten.bp.ZonedDateTime;

import java.io.Serializable;

import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.SyncStatus;

public class EventDTO implements Serializable {

    private Long id;
    private double latitude;
    private double longitude;
    private String name;
    private String place;
    private String description;
    private EventType type;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    private FacebookPrivacy privacy;
    private String imageUri;
    private Long owner;
    private SyncStatus syncStatus;
    private ZonedDateTime updated_time;
    private ZonedDateTime created_time;
    private Double distance;
    private Boolean expired;

    public EventDTO() {
    }

    public EventDTO(Long id, String name, String description, String imageUri, EventType type, FacebookPrivacy privacy, ZonedDateTime start_time, ZonedDateTime end_time, String place, double latitude, double longitude, SyncStatus syncStatus, ZonedDateTime updated_time, ZonedDateTime created_time, Long owner) {
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
        this.imageUri = imageUri;
        this.owner = owner;
        this.syncStatus = syncStatus;
        this.updated_time = updated_time;
        this.created_time = created_time;
    }

    public EventDTO(Event e){
        this.id = e.getId();
        this.latitude = e.getLatitude();
        this.longitude = e.getLongitude();
        this.name = e.getName();
        this.place = e.getPlace();
        this.description = e.getDescription();
        this.type = e.getType();
        this.start_time = e.getStart_time();
        this.end_time = e.getEnd_time();
        this.privacy = e.getPrivacy();
        this.imageUri = e.getImageUri();
        this.owner = e.getOwner().getId();
        this.syncStatus = e.getSyncStatus();
        this.updated_time = e.getUpdated_time();
        this.created_time = e.getCreated_time();
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
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

    public ZonedDateTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(ZonedDateTime created_time) {
        this.created_time = created_time;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
}
