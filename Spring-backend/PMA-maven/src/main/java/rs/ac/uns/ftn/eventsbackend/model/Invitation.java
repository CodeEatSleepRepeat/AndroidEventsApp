package rs.ac.uns.ftn.eventsbackend.model;

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
	private Long invitationId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private User invitationSender;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private User invitationReceiver;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Event event;

	@NotNull
	private InvitationStatus status;

	public Invitation(Long invitationId, @NotNull User invitationSender, @NotNull User invitationReceiver,
			@NotNull Event event, @NotNull InvitationStatus status) {
		super();
		this.invitationId = invitationId;
		this.invitationSender = invitationSender;
		this.invitationReceiver = invitationReceiver;
		this.event = event;
		this.status = status;
	}

	public Invitation() {

	}

}
