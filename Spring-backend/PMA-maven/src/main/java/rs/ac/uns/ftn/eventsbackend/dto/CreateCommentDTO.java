package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDTO {

	private String text;
	private Long eventId;
	private Long userId;
	
}
