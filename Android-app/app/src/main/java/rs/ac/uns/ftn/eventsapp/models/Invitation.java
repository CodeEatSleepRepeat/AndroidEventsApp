package rs.ac.uns.ftn.eventsapp.models;

public class Invitation {

    private Long invitationId;
    private User invitationSender;
    private User invitationReceiver;
    private Event event;
    private InvitationStatus status;

    public Invitation(Long invitationId, User invitationSender, User invitationReceiver, Event event, InvitationStatus status) {
        this.invitationId = invitationId;
        this.invitationSender = invitationSender;
        this.invitationReceiver = invitationReceiver;
        this.event = event;
        this.status = status;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public User getInvitationSender() {
        return invitationSender;
    }

    public void setInvitationSender(User invitationSender) {
        this.invitationSender = invitationSender;
    }

    public User getInvitationReceiver() {
        return invitationReceiver;
    }

    public void setInvitationReceiver(User invitationReceiver) {
        this.invitationReceiver = invitationReceiver;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
}
