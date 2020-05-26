package rs.ac.uns.ftn.eventsbackend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserFBSyncChangeDTO {

	@NonNull
	@Email
	private String email;

	@NotNull
	private Boolean sync;

	public UserFBSyncChangeDTO(@NonNull String email, @NotNull Boolean sync) {
		super();
		this.email = email;
		this.sync = sync;
	}
}
