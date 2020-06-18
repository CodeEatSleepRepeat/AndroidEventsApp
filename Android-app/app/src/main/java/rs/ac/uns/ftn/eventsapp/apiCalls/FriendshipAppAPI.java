package rs.ac.uns.ftn.eventsapp.apiCalls;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rs.ac.uns.ftn.eventsapp.dtos.FriendshipDTO;

public interface FriendshipAppAPI {

    @POST("/friendship/{senderId}/{receiverId}")
    Call<FriendshipDTO> sendFriendRequest(@Path("senderId") Long senderId,
                                 @Path("receiverId") Long receiverId);

    @GET("/friendship/getAllFriendRequests/{userId}/page/{num}")
    Call<List<FriendshipDTO>> getFriendRequests(@Path("userId") Long userId, @Path("num") int num);

    @GET("/friendship/getFriendship/{user1}/{user2}")
    Call<FriendshipDTO> getFriendshipOfTwoUsers(@Path("user1") Long user1,
                                                @Path("user2") Long user2);

    @GET("/friendship/getNumberOfUserFriendRequests/{userId}")
    Call<Integer> getNumberOfUserFriendRequests(@Path("userId") Long userId);

    @PUT("/friendship/acceptRequest/{receiverId}/{requestId}")
    Call<FriendshipDTO> acceptRequest(@Path("receiverId") Long receiverId,
                                      @Path("requestId") Long requestId);
    @DELETE("/friendship/{requestId}")
    Call<FriendshipDTO> deleteRequest(@Path("requestId") Long requestId);
}
