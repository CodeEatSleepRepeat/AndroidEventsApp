package rs.ac.uns.ftn.eventsapp.apiCalls;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rs.ac.uns.ftn.eventsapp.dtos.UserDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UserFBSyncChangeDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UserLoginDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UserProfileChangeDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UserProfileSyncDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UserRegisterDTO;
import rs.ac.uns.ftn.eventsapp.models.User;

public interface UserAppApi {

    @POST("/user/register")
    Call<User> register(@Body UserRegisterDTO user);

    @GET("/user/register/{accessToken}")
    Call<User> register(@Path("accessToken") String accessToken);

    @POST("/user/upload/{id}")
    Call<User> uploadImage(@Body RequestBody image, @Path("id") Long id);

    @GET("/user/login/{accessToken}")
    Call<User> login(@Path("accessToken") String accessToken);

    @POST("/user/login")
    Call<User> login(@Body UserLoginDTO user);

    @GET("/user/forgot/{email}")
    Call<Void> forgotPassword(@Path("email") String email);

    @POST("/user/update")
    Call<User> update(@Body UserProfileChangeDTO user);

    @POST("/user/unlink")
    Call<User> unlink(@Body UserLoginDTO user);

    @POST("/user/delete")
    Call<Void> delete(@Body UserLoginDTO user);

    @PUT("/user/update/events")
    Call<User> syncFBEvents(@Body UserFBSyncChangeDTO syncSettings);

    @PUT("/user/update/profile")
    Call<User> syncFBProfile(@Body UserFBSyncChangeDTO syncSettings);

    @POST("/user/sync")
    Call<User> syncUser(@Body UserProfileSyncDTO data);

    @GET("/user/friendsOf/{userId}")
    Call<List<User>> getFriendsOfUser(@Path("userId") Long userId);

    @GET("/user/containsUsername/{userId}/{username}/page/{num}")
    Call<List<User>> getUsersWhichContainsUsername(@Path("userId") Long userId,
                                                   @Path("num") int num,
                                                   @Path("username") String username);
    @GET("/user/{userId}")
    Call<User> getUser(@Path("userId") Long userId);

    @GET("/user/goingTo/{num}/{eventId}")
    Call<List<UserDTO>> getGoingToUsers(@Path("num") int num, @Path("eventId") Long eventId);

    @GET("/user/interestedIn/{num}/{eventId}")
    Call<List<UserDTO>> getInterestedInUsers(@Path("num") int num, @Path("eventId") Long eventId);

    @GET("/user/getFriendsForEventsInvite/{senderId}/{eventId}")
    Call<List<User>> getFriendsForInvitation(@Path("senderId") Long senderId,
                                             @Path("eventId") Long eventId);
}
