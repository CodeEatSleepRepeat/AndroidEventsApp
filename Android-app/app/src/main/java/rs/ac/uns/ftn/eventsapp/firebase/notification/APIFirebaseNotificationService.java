package rs.ac.uns.ftn.eventsapp.firebase.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.Sender;

public interface APIFirebaseNotificationService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAANhIVkaQ:APA91bGEEuwdDAAgA_utJHIiSDVSXOMgisyWIkISwRM4_6ecAtWMTX40qwqc3XE6_ZeWBLCHCm51rskEPBNOtJO1aO9WYhHG50P8FVXsdcxcmLCcBaFk9yyfR-xnHRDzUApRhT_gpd7-"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
