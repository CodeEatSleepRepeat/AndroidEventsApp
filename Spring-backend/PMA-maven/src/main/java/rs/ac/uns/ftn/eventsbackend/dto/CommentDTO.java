package rs.ac.uns.ftn.eventsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
	
	private String text;
	private String imePrezime;
	private String imgUri;

}
