package rs.ac.uns.ftn.eventsapp.models;

public class Friendship {
    private Long friendshipId;
    private User requestSender;
    private User requestReceiver;
    private FriendshipStatus status;

    public Friendship(Long friendshipId, User requestSender, User requestReceiver, FriendshipStatus status) {
        this.friendshipId = friendshipId;
        this.requestSender = requestSender;
        this.requestReceiver = requestReceiver;
        this.status = status;
    }

    public Long getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(Long friendshipId) {
        this.friendshipId = friendshipId;
    }

    public User getRequestSender() {
        return requestSender;
    }

    public void setRequestSender(User requestSender) {
        this.requestSender = requestSender;
    }

    public User getRequestReceiver() {
        return requestReceiver;
    }

    public void setRequestReceiver(User requestReceiver) {
        this.requestReceiver = requestReceiver;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}
