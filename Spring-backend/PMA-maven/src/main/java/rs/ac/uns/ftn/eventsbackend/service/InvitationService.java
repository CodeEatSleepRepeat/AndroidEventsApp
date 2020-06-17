package rs.ac.uns.ftn.eventsbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.model.Event;
import rs.ac.uns.ftn.eventsbackend.model.Invitation;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.repository.EventRepository;
import rs.ac.uns.ftn.eventsbackend.repository.InvitationRepository;
import rs.ac.uns.ftn.eventsbackend.repository.UserRepository;

import java.util.Optional;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public InvitationService(InvitationRepository invitationRepository, UserRepository userRepository,
                             EventRepository eventRepository){
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public Invitation createInvitation(Long senderId, Long receiverId, Long eventId) throws Exception {
        Optional<User> senderOptional = userRepository.findById(senderId);
        if(!senderOptional.isPresent())
            throw new Exception("Sender user does not exist in database");
        Optional<User> receiverOptional = userRepository.findById(receiverId);
        if(!receiverOptional.isPresent())
            throw new Exception("Receiver user does not exist in database");
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if(!eventOptional.isPresent())
            throw new Exception("Event does not exist in database");

        Invitation createdInvitation = new Invitation();
        createdInvitation.setInvitationSender(senderOptional.get());
        createdInvitation.setInvitationReceiver(receiverOptional.get());
        createdInvitation.setEvent(eventOptional.get());

        invitationRepository.save(createdInvitation);

        return createdInvitation;
    }

    public Invitation deleteInvitation(Long invitationId) throws Exception {
        Optional<Invitation> invitationOptional = invitationRepository.findById(invitationId);
        if(!invitationOptional.isPresent()){
            throw new Exception("Invitation does not exist in database");
        }

        invitationRepository.delete(invitationOptional.get());

        return invitationOptional.get();
    }
}
