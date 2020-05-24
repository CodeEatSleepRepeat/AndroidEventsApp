package rs.ac.uns.ftn.eventsbackend.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSyncChangeDTO {

	@NonNull
	private String email;

	@NotNull
	private Boolean sync;

	public UserSyncChangeDTO(@NonNull String email, @NotNull Boolean sync) {
		super();
		this.email = email;
		this.sync = sync;
	}
}
