package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	
	private Long id;
	private String name;
	private String imgUri;
	private String email;

	public UserDTO(){

	}
	public UserDTO(Long id, String name, String imgUri, String email) {
		this.id = id;
		this.name = name;
		this.imgUri = imgUri;
		this.email = email;
	}
}
