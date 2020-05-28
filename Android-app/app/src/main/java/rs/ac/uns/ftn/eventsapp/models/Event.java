package rs.ac.uns.ftn.eventsapp.models;

import org.threeten.bp.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class Event {

    private Long id;
    private String name;
    private String description;
    private String imageUri;
    private EventType type;
    private FacebookPrivacy privacy;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    private String place;
    private Long latitude;
    private Long longitude;
    private User owner;
    private List<User> interested;
    private List<User> going;
    private List<Invitation> userInvitation;
    private List<Comment> comments;
    private SyncStatus syncStatus;
    private ZonedDateTime updated_time;
    private ZonedDateTime created_time;

    public Event(Long id, String name, String description, String imageUri, EventType type, FacebookPrivacy privacy, ZonedDateTime start_time, ZonedDateTime end_time, String place, Long latitude, Long longitude, User owner, List<User> interested, List<User> going, List<Invitation> userInvitation, List<Comment> comments, SyncStatus syncStatus, ZonedDateTime updated_time, ZonedDateTime created_time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
        this.type = type;
        this.privacy = privacy;
        this.start_time = start_time;
        this.end_time = end_time;
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
        this.owner = owner;
        this.interested = interested;
        this.going = going;
        this.userInvitation = userInvitation;
        this.comments = comments;
        this.syncStatus = syncStatus;
        this.updated_time = updated_time;
        this.created_time = created_time;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
}
