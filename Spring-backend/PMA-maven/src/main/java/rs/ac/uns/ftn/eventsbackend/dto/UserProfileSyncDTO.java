package rs.ac.uns.ftn.eventsbackend.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserProfileSyncDTO {
	@NotNull
	private String email;
	@NotNull
	private String password;
	private Long lastSyncTime;
}
