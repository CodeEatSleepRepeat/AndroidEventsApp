package rs.ac.uns.ftn.eventsbackend.dto;

import rs.ac.uns.ftn.eventsbackend.model.Invitation;

public class InvitationDTO {

    private Long id;
    private UserDTO sender;
    private UserDTO reciever;
    private EventDTO event;

    public InvitationDTO(){
    }

    public InvitationDTO(Invitation invitation){
        sender = new UserDTO(invitation.getInvitationSender().getId(), invitation.getInvitationSender().getName(),
                invitation.getInvitationSender().getImageUri());
        reciever = new UserDTO(invitation.getInvitationReceiver().getId(), invitation.getInvitationReceiver().getName(),
                invitation.getInvitationReceiver().getImageUri());
        event = new EventDTO(invitation.getEvent());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getReciever() {
        return reciever;
    }

    public void setReciever(UserDTO reciever) {
        this.reciever = reciever;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }
}
