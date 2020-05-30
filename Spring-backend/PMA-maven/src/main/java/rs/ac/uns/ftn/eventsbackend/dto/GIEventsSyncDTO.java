package rs.ac.uns.ftn.eventsbackend.dto;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GIEventsSyncDTO {
	@NotNull
	private String email;
	@NotNull
	private String password;
	private ArrayList<GoingInterestedEventsDTO> eventsForUpdate;
}
