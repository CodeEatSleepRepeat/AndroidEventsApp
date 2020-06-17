package rs.ac.uns.ftn.eventsbackend.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import rs.ac.uns.ftn.eventsbackend.enums.InvitationStatus;

@Entity
@Data
public class Invitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User invitationSender;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private User invitationReceiver;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private Event event;


	public Invitation(Long id, @NotNull User invitationSender, @NotNull User invitationReceiver, @NotNull Event event) {
		super();
		this.id = id;
		this.invitationSender = invitationSender;
		this.invitationReceiver = invitationReceiver;
		this.event = event;
	}

	public Invitation() {

	}

}
