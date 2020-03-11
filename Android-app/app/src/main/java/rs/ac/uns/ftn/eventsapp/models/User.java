package rs.ac.uns.ftn.eventsapp.models;

import java.util.List;

public class User {

    private Long userId;
    private String userName;
    private String email;
    private String password;
    private String facebookToken;
    private List<Friendship> sendRequests;
    private List<Friendship> receivedRequests;
    private List<Invitation> sendInvitations;
    private List<Invitation> receivedInvitations;
    private List<Event> userEvents;
    private List<Event> interestedEvents;
    private List<Event> goingEvents;
    private Boolean activatedAccount;

    public User(Long userId, String userName, String email, String password, String facebookToken, List<Friendship> sendRequests, List<Friendship> receivedRequests, List<Invitation> sendInvitations, List<Invitation> receivedInvitations, List<Event> userEvents, List<Event> interestedEvents, List<Event> goingEvents, Boolean activatedAccount) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.facebookToken = facebookToken;
        this.sendRequests = sendRequests;
        this.receivedRequests = receivedRequests;
        this.sendInvitations = sendInvitations;
        this.receivedInvitations = receivedInvitations;
        this.userEvents = userEvents;
        this.interestedEvents = interestedEvents;
        this.goingEvents = goingEvents;
        this.activatedAccount = activatedAccount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
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

    public Boolean getActivatedAccount() {
        return activatedAccount;
    }

    public void setActivatedAccount(Boolean activatedAccount) {
        this.activatedAccount = activatedAccount;
    }
}
