package rs.ac.uns.ftn.eventsbackend.dto;

import rs.ac.uns.ftn.eventsbackend.model.Invitation;

public class InvitationDTO {

    private Long id;
    private UserInvitationDTO sender;
    private UserInvitationDTO reciever;
    private EventDTO event;

    public InvitationDTO(){
    }

    public InvitationDTO(Invitation invitation){
        id = invitation.getId();
        sender = new UserInvitationDTO(invitation.getInvitationSender().getId(), invitation.getInvitationSender().getName(),
                invitation.getInvitationSender().getImageUri(), invitation.getInvitationSender().getEmail());
        reciever = new UserInvitationDTO(invitation.getInvitationReceiver().getId(), invitation.getInvitationReceiver().getName(),
                invitation.getInvitationReceiver().getImageUri(), invitation.getInvitationReceiver().getEmail());
        event = new EventDTO(invitation.getEvent());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserInvitationDTO getSender() {
        return sender;
    }

    public void setSender(UserInvitationDTO sender) {
        this.sender = sender;
    }

    public UserInvitationDTO getReciever() {
        return reciever;
    }

    public void setReciever(UserInvitationDTO reciever) {
        this.reciever = reciever;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }
}
