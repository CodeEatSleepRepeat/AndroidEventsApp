package rs.ac.uns.ftn.eventsbackend.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
public class EventsSyncDTO {
	@NotNull
	private String email;
	@NotNull
	private String password;
	
	private Long lastSyncTime;
	
	private ArrayList<UpdateEventDTO> eventsForUpdate;
	
	public ZonedDateTime getLastSyncTime() {
		Instant instant = Instant.ofEpochMilli(lastSyncTime);
		ZonedDateTime time = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
		return time;
	}
}
