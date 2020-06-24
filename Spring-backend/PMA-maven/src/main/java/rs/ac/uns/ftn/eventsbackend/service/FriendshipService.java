package rs.ac.uns.ftn.eventsbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.eventsbackend.dto.FriendshipDTO;
import rs.ac.uns.ftn.eventsbackend.enums.FriendshipStatus;
import rs.ac.uns.ftn.eventsbackend.model.Friendship;
import rs.ac.uns.ftn.eventsbackend.model.User;
import rs.ac.uns.ftn.eventsbackend.repository.FriendshipRepository;
import rs.ac.uns.ftn.eventsbackend.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

	private FriendshipRepository friendshipRepository;
    private UserRepository userRepository;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository){
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }
    public Friendship createFriendRequest(Long senderId, Long receiverId) throws Exception {
        Optional<User> requestSender = userRepository.findById(senderId);
        Optional<User> requestReceiver = userRepository.findById(receiverId);
        if(!requestSender.isPresent() || !requestReceiver.isPresent()){
            throw new Exception("Cannot create friend request cause one of users does not exist");
        }
        Optional<Friendship> existingRequestFromSenderToReceiver =
                friendshipRepository.findByRequestSenderIdAndRequestReceiverId(senderId, receiverId);
        Optional<Friendship> existingRequestFromReceiverToSender =
                friendshipRepository.findByRequestSenderIdAndRequestReceiverId(receiverId, senderId);

        if(existingRequestFromReceiverToSender.isPresent() ||
        existingRequestFromSenderToReceiver.isPresent())
            throw new Exception("Friendship request already exists!");

        Friendship newFriendship = new Friendship();
        newFriendship.setStatus(FriendshipStatus.PENDING);
        newFriendship .setRequestReceiver(requestReceiver.get());
        newFriendship .setRequestSender(requestSender.get());

        friendshipRepository.save(newFriendship);

        return newFriendship;
    }

    public List<Friendship> getAllFriendRequests(Long userId, FriendshipStatus status, Pageable pageable) {
        Page<Friendship> foundRequestsPage = friendshipRepository.findAllByRequestReceiverIdAndStatus(userId, status, pageable);
        List<Friendship> foundRequests = foundRequestsPage.getContent();
        if(foundRequests == null){
            return Collections.emptyList();
        }
        else{
            return foundRequests;
        }
    }

    public Friendship acceptRequestAndReturnStatus(Long recieverId, Long requestId) throws Exception {
        Optional<Friendship> foundFriendRequest = friendshipRepository.findById(requestId);

        if(!foundFriendRequest.isPresent())
            throw new Exception("Friend Request for accepting was not found");
        Friendship requestForAccept = foundFriendRequest.get();

        if(!requestForAccept.getRequestReceiver().getId().equals(recieverId))
            throw new Exception("Reciever id dont match!");

        if(requestForAccept.getStatus() != FriendshipStatus.PENDING)
            throw new Exception("Request status was not PENDING");

        requestForAccept.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(requestForAccept);
        return requestForAccept;
    }

    public Friendship deleteFriendshipRequest(Long requestId) throws Exception {
        Optional<Friendship> foundFriendRequest = friendshipRepository.findById(requestId);

        if(!foundFriendRequest.isPresent())
            throw new Exception("Friend Request for deleting was not found");
        Friendship requestForAccept = foundFriendRequest.get();

        friendshipRepository.delete(requestForAccept);
        return requestForAccept;
    }

    public int getNumberOfUsersFriendRequests(Long userId) {
        Optional<Integer> numberOfFriendRequestsOptional = friendshipRepository.getNumberOfUserFriendRequests(userId);

        if(numberOfFriendRequestsOptional.isPresent())
            return numberOfFriendRequestsOptional.get();
        else
            return 0;
    }

    public FriendshipDTO getFriendship(Long user1, Long user2) {
        Optional<Friendship> foundFriendship = friendshipRepository.findFriendshipOfTwoUsers(user1, user2, FriendshipStatus.ACCEPTED);

        if(foundFriendship.isPresent())
            return new FriendshipDTO(foundFriendship.get());
        else{
            FriendshipDTO friendshipDTO = new FriendshipDTO();
            friendshipDTO.setStatus("NOT EXISTS");
            return friendshipDTO;
        }

    }
}
