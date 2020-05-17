package rs.ac.uns.ftn.eventsapp.models;

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
    private List<Friendship> sendRequests;
    private List<Friendship> receivedRequests;
    private List<Invitation> sendInvitations;
    private List<Invitation> receivedInvitations;
    private List<ChatMessage> chatMessagesSent;
    private List<ChatMessage> chatMessagesReceived;
    private List<Event> userEvents; //added, not on backend User model
    private List<Event> interestedEvents;
    private List<Event> goingEvents;
    private List<Comment> comments;
    private Boolean activatedAccount;
    private Boolean syncFacebookEvents;
    private Boolean syncFacebookProfile;


    public User(Long id, String facebookId, String name, String imageUri, Integer imageHeight, Integer imageWidth, String email, String password, List<Friendship> sendRequests, List<Friendship> receivedRequests, List<Invitation> sendInvitations, List<Invitation> receivedInvitations, List<ChatMessage> chatMessagesSent, List<ChatMessage> chatMessagesReceived, List<Event> userEvents, List<Event> interestedEvents, List<Event> goingEvents, List<Comment> comments, Boolean activatedAccount, Boolean syncFacebookEvents, Boolean syncFacebookProfile) {
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

    public List<Friendship> getSendRequests() {
        return sendRequests;
    }

    public void setSendRequests(List<Friendship> sendRequests) {
        this.sendRequests = sendRequests;
    }

    public List<Friendship> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<Friendship> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<Invitation> getSendInvitations() {
        return sendInvitations;
    }

    public void setSendInvitations(List<Invitation> sendInvitations) {
        this.sendInvitations = sendInvitations;
    }

    public List<Invitation> getReceivedInvitations() {
        return receivedInvitations;
    }

    public void setReceivedInvitations(List<Invitation> receivedInvitations) {
        this.receivedInvitations = receivedInvitations;
    }

    public List<ChatMessage> getChatMessagesSent() {
        return chatMessagesSent;
    }

    public void setChatMessagesSent(List<ChatMessage> chatMessagesSent) {
        this.chatMessagesSent = chatMessagesSent;
    }

    public List<ChatMessage> getChatMessagesReceived() {
        return chatMessagesReceived;
    }

    public void setChatMessagesReceived(List<ChatMessage> chatMessagesReceived) {
        this.chatMessagesReceived = chatMessagesReceived;
    }

    public List<Event> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(List<Event> userEvents) {
        this.userEvents = userEvents;
    }

    public List<Event> getInterestedEvents() {
        return interestedEvents;
    }

    public void setInterestedEvents(List<Event> interestedEvents) {
        this.interestedEvents = interestedEvents;
    }

    public List<Event> getGoingEvents() {
        return goingEvents;
    }

    public void setGoingEvents(List<Event> goingEvents) {
        this.goingEvents = goingEvents;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
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
}
