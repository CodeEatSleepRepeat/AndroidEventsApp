package rs.ac.uns.ftn.eventsbackend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserLoginDTO {

	@NotNull
	@Email
	private String email;

	@NotNull
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])((?=.*[@#$%^&+=!])|(?=.*[0-9]))(?=\\S+$).{7,}$")
	private String password;

}
