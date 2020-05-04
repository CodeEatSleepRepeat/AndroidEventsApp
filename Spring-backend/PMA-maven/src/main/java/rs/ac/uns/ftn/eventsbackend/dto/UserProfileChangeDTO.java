package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserProfileChangeDTO {
	@NonNull
	private String email;
	
	private String name;
	private String imageUri;
	
	private String passwordOld;
	private String passwordNew1;
	private String passwordNew2;
	

}
