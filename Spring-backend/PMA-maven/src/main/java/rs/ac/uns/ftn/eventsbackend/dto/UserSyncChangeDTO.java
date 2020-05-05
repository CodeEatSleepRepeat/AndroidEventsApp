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

}
