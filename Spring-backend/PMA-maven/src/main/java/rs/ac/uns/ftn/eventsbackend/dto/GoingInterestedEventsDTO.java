package rs.ac.uns.ftn.eventsbackend.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.eventsbackend.enums.GoingInterestedStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoingInterestedEventsDTO {
	@NotNull
    private EventDTO event;
	@NotNull
    private GoingInterestedStatus status;
    
}
