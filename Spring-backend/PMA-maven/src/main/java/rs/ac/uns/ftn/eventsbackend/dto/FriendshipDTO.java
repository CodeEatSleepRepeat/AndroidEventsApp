package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;

@Data
public class FriendshipDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String status;


    public FriendshipDTO(){
    }

    public FriendshipDTO(Friendship friendship){
        this.id = friendship.getId();
        this.senderId = friendship.getRequestSender().getId();
        this.receiverId = friendship.getRequestReceiver().getId();
        this.status = String.valueOf(friendship.getStatus());
    }
}
