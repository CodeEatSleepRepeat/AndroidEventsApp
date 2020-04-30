package rs.ac.uns.ftn.eventsbackend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
	
	@NotNull
    private String text;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
    private User sender;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
    private User recipient;
    
    @JsonProperty("created_time")
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
	@CreationTimestamp
    private Date created_time;
    
    @NotNull
    private Boolean seen;

	public ChatMessage(Long messageId, @NotNull String text, @NotNull User sender, @NotNull User recipient,
			Date created_time, @NotNull Boolean seen) {
		super();
		this.messageId = messageId;
		this.text = text;
		this.sender = sender;
		this.recipient = recipient;
		this.created_time = created_time;
		this.seen = seen;
	}
    
    public ChatMessage() {

    }

}
