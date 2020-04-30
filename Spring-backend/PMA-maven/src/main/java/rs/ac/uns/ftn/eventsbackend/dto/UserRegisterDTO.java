package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserRegisterDTO {
	@NonNull
	private String email;
	@NonNull
	private String password;
	@NonNull
	private String name;
	
}
