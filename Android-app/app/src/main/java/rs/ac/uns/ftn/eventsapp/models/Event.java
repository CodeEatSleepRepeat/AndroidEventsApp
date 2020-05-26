package rs.ac.uns.ftn.eventsapp.models;

import java.util.Date;
import java.util.List;

public class Event {

    private Long eventId;
    private String eventName;
    private String eventDescription;
    private String eventImageURI;
    private EventType eventType;
    private Boolean openForAll;
    private Date startTime;
    private Date endTime;
    private String location;
    private Long latitude;
    private Long longitude;
    private User author;
    private List<User> interested;
    private List<User> going;
    private List<Invitation> userInvitation;
    private SyncStatus syncStatus;
    private Date updated_time;

    public Event(Long eventId, String eventName, String eventDescription, String eventImageURI, EventType eventType, Boolean openForAll, Date startTime, Date endTime, String location, Long latitude, Long longitude, User author, List<User> interested, List<User> going, List<Invitation> userInvitation, SyncStatus syncStatus, Date updated_time) {
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
        this.interested = interested;
        this.going = going;
        this.userInvitation = userInvitation;
        this.syncStatus = syncStatus;
        this.updated_time = updated_time;
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

    public Boolean getOpenForAll() {
        return openForAll;
    }

    public void setOpenForAll(Boolean openForAll) {
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

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<User> getInterested() {
        return interested;
    }

    public void setInterested(List<User> interested) {
        this.interested = interested;
    }

    public List<User> getGoing() {
        return going;
    }

    public void setGoing(List<User> going) {
        this.going = going;
    }

    public List<Invitation> getUserInvitation() {
        return userInvitation;
    }

    public void setUserInvitation(List<Invitation> userInvitation) {
        this.userInvitation = userInvitation;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        this.updated_time = updated_time;
    }
}
