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
	
	public UserProfileChangeDTO(@NonNull String email, String name, String imageUri, String passwordOld,
			String passwordNew1, String passwordNew2) {
		super();
		this.email = email;
		this.name = name;
		this.imageUri = imageUri;
		this.passwordOld = passwordOld;
		this.passwordNew1 = passwordNew1;
		this.passwordNew2 = passwordNew2;
	}

	
}
