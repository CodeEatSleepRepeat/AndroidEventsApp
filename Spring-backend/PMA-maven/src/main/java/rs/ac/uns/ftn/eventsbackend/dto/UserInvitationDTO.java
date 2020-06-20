package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserInvitationDTO {
    private Long id;
    private String name;
    private String imgUri;
    private String email;

    public UserInvitationDTO(){

    }
    public UserInvitationDTO(Long id, String name, String imgUri, String email) {
        this.id = id;
        this.name = name;
        this.imgUri = imgUri;
        this.email = email;
    }

}
