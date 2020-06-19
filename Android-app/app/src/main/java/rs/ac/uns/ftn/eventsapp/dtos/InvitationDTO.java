package rs.ac.uns.ftn.eventsapp.dtos;

public class InvitationDTO {

    private Long id;
    private UserInvitationDTO sender;
    private UserInvitationDTO reciever;
    private EventDTO event;

    public InvitationDTO(){
    }

    public InvitationDTO(Long id, UserInvitationDTO sender, UserInvitationDTO reciever, EventDTO event) {
        this.id = id;
        this.sender = sender;
        this.reciever = reciever;
        this.event = event;
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
