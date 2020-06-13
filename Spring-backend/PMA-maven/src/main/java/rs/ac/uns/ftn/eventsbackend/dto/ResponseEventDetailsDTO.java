package rs.ac.uns.ftn.eventsbackend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEventDetailsDTO {

	private Long userId;
	private Boolean going;
	private Boolean interested;
	private List<String> goingImages;
	private List<String> interestedImages;
	private String userImage;
	private String userName;
	private Integer organizedEventsNum;
	private List<SimilarEventDTO> events;
	
}
