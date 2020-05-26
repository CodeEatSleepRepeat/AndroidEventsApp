package rs.ac.uns.ftn.eventsbackend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserRegisterDTO {

	@NonNull
	@Email
	private String email;

	@NonNull
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])((?=.*[@#$%^&+=!])|(?=.*[0-9]))(?=\\S+$).{7,}$")
	private String password;

	@NonNull
	@Pattern(regexp = "^\\p{L}+[\\p{L} .'-]{2,64}$")
	private String name;
}
