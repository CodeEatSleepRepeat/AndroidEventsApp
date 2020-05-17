package rs.ac.uns.ftn.eventsapp.apiCalls;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UserRegisterDTO;
import rs.ac.uns.ftn.eventsapp.models.User;

public interface UserAppApi {

    @POST("/user/register")
    Call<User> register(@Body UserRegisterDTO user);

    @POST("/user/upload/{id}")
    Call<User> uploadImage(@Body RequestBody image, @Path("id") Long id);

}
