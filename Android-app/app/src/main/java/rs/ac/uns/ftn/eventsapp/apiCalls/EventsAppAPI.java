package rs.ac.uns.ftn.eventsapp.apiCalls;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;

public interface EventsAppAPI {

    //String BASE_URL = "http://localhost:8080/";

    @POST("/event")
    Call<CreateEventDTO> createEvent(@Body CreateEventDTO dto);
}
