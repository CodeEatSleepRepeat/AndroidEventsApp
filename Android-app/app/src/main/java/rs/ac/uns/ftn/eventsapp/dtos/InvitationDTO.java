package rs.ac.uns.ftn.eventsapp.dtos;

public class InvitationDTO {

    private Long id;
    private UserDTO sender;
    private UserDTO reciever;
    private EventDTO event;

    public InvitationDTO(){
    }

    public InvitationDTO(Long id, UserDTO sender, UserDTO reciever, EventDTO event) {
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
