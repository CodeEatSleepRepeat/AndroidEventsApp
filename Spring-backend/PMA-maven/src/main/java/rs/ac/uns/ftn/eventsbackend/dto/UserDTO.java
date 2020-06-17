package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.eventsbackend.model.User;

@Getter
@Setter
public class UserDTO {
	
	private Long id;
	private String name;
	private String imgUri;

	public UserDTO(){

	}
	public UserDTO(Long id, String name, String imgUri) {
		this.id = id;
		this.name = name;
		this.imgUri = imgUri;
	}
}
