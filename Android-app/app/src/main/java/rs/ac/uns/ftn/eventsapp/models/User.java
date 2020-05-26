package rs.ac.uns.ftn.eventsapp.models;

import java.sql.Timestamp;
import java.util.List;

/*
 * Dodat je konstruktor koji zahteva userId, username, profileImageUrl
 *
 * Dodato je polje profileImageUrl: String
 * */
public class User {

    private Long id;
    private String facebookId;
    private String name;
    private String imageUri;
    private Integer imageHeight;
    private Integer imageWidth;
    private String email;
    private String password;
    private List<Long> sendRequests;
    private List<Long> receivedRequests;
    private List<Long> sendInvitations;
    private List<Long> receivedInvitations;
    private List<Long> chatMessagesSent;
    private List<Long> chatMessagesReceived;
    private List<Long> userEvents;
    private List<Long> interestedEvents;
    private List<Long> goingEvents;
    private List<Long> comments;
    private Boolean activatedAccount;
    private Boolean syncFacebookEvents;
    private Boolean syncFacebookProfile;
    private SyncStatus syncStatus;
    private Timestamp updated_time;

    public User(Long id, String facebookId, String name, String imageUri, Integer imageHeight, Integer imageWidth, String email, String password, List<Long> sendRequests, List<Long> receivedRequests, List<Long> sendInvitations, List<Long> receivedInvitations, List<Long> chatMessagesSent, List<Long> chatMessagesReceived, List<Long> userEvents, List<Long> interestedEvents, List<Long> goingEvents, List<Long> comments, Boolean activatedAccount, Boolean syncFacebookEvents, Boolean syncFacebookProfile, SyncStatus syncStatus, Timestamp updated_time) {
        this.id = id;
        this.facebookId = facebookId;
        this.name = name;
        this.imageUri = imageUri;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.email = email;
        this.password = password;
        this.sendRequests = sendRequests;
        this.receivedRequests = receivedRequests;
        this.sendInvitations = sendInvitations;
        this.receivedInvitations = receivedInvitations;
        this.chatMessagesSent = chatMessagesSent;
        this.chatMessagesReceived = chatMessagesReceived;
        this.userEvents = userEvents;
        this.interestedEvents = interestedEvents;
        this.goingEvents = goingEvents;
        this.comments = comments;
        this.activatedAccount = activatedAccount;
        this.syncFacebookEvents = syncFacebookEvents;
        this.syncFacebookProfile = syncFacebookProfile;
        this.syncStatus = syncStatus;
        this.updated_time = updated_time;
    }

    public User(Long id, String name, String email, String password, String imageUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUri = imageUri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getSendRequests() {
        return sendRequests;
    }

    public void setSendRequests(List<Long> sendRequests) {
        this.sendRequests = sendRequests;
    }

    public List<Long> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<Long> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<Long> getSendInvitations() {
        return sendInvitations;
    }

    public void setSendInvitations(List<Long> sendInvitations) {
        this.sendInvitations = sendInvitations;
    }

    public List<Long> getReceivedInvitations() {
        return receivedInvitations;
    }

    public void setReceivedInvitations(List<Long> receivedInvitations) {
        this.receivedInvitations = receivedInvitations;
    }

    public List<Long> getChatMessagesSent() {
        return chatMessagesSent;
    }

    public void setChatMessagesSent(List<Long> chatMessagesSent) {
        this.chatMessagesSent = chatMessagesSent;
    }

    public List<Long> getChatMessagesReceived() {
        return chatMessagesReceived;
    }

    public void setChatMessagesReceived(List<Long> chatMessagesReceived) {
        this.chatMessagesReceived = chatMessagesReceived;
    }

    public List<Long> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(List<Long> userEvents) {
        this.userEvents = userEvents;
    }

    public List<Long> getInterestedEvents() {
        return interestedEvents;
    }

    public void setInterestedEvents(List<Long> interestedEvents) {
        this.interestedEvents = interestedEvents;
    }

    public List<Long> getGoingEvents() {
        return goingEvents;
    }

    public void setGoingEvents(List<Long> goingEvents) {
        this.goingEvents = goingEvents;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public Boolean getActivatedAccount() {
        return activatedAccount;
    }

    public void setActivatedAccount(Boolean activatedAccount) {
        this.activatedAccount = activatedAccount;
    }

    public Boolean getSyncFacebookEvents() {
        return syncFacebookEvents;
    }

    public void setSyncFacebookEvents(Boolean syncFacebookEvents) {
        this.syncFacebookEvents = syncFacebookEvents;
    }

    public Boolean getSyncFacebookProfile() {
        return syncFacebookProfile;
    }

    public void setSyncFacebookProfile(Boolean syncFacebookProfile) {
        this.syncFacebookProfile = syncFacebookProfile;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Timestamp getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Timestamp updated_time) {
        this.updated_time = updated_time;
    }
}
