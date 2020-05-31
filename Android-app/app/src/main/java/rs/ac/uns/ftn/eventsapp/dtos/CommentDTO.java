package rs.ac.uns.ftn.eventsapp.dtos;

import rs.ac.uns.ftn.eventsapp.models.Comment;

public class CommentDTO {
	
	private String text;
	private String imePrezime;
	private String imgUri;

	public CommentDTO(){};

	public CommentDTO(String text, String imePrezime, String imgUri) {
		this.text = text;
		this.imePrezime = imePrezime;
		this.imgUri = imgUri;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImePrezime() {
		return imePrezime;
	}

	public void setImePrezime(String imePrezime) {
		this.imePrezime = imePrezime;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}
}
