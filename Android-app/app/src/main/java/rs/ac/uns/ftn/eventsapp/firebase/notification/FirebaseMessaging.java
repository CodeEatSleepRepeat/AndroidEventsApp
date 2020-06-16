package rs.ac.uns.ftn.eventsapp.firebase.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DisplayContext;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.fragments.ChatLogFragment;

@SuppressLint({"MissingFirebaseInstanceTokenRefresh", "Registered"})
public class FirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //String loggedUser = FirebaseAuth.getInstance().getUid();

        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        boolean isMessageAssignedToMe = firebaseUser != null && sent.equals(firebaseUser.getUid());
        if(isMessageAssignedToMe){
            if(!firebaseUser.getUid().equals(user)){
                //is Android OREO or above android system version
                sendNotification(remoteMessage);
            }
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        assert user != null;
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        PendingIntent pIntent = null;
        //TODO: Ovde se moze kreirati razlicit intent u zavisnosti koja je notifikacija
        assert title != null;
        if(title.contains(getResources().getString(R.string.invited_by))){
            //TODO: Napraviti intent koji ide ka invitacijama
        }
        else if(title.contains(getResources().getString(R.string.friend_requests))){
            //TODO: Napravi intent koji ide ka friend requestovima
        }
        else{
            //TODO: Napravi intent koji ide ka chat logu
            Intent intent = new Intent(this, ChatLogFragment.class);
            Bundle bundle = new Bundle();
            bundle.putString("chatPartnerID", user);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pIntent = PendingIntent.getActivity(this, i, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendOAndAboveVersionNotification(icon, body, title, defSoundUri,
                    pIntent, i);
        }
        else{
            sendNormalNotification(icon, body, title, defSoundUri,
                    pIntent, i);
        }
    }

    private void sendNormalNotification(String icon, String body, String title, Uri defSoundUri,
                                        PendingIntent pIntent, Integer i) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int j = 0;
        if(i>0) {
            j=i;
        }
        notificationManager.notify(j, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOAndAboveVersionNotification(String icon, String body, String title, Uri defSoundUri,
                                                  PendingIntent pIntent, Integer i) {

        OreoAndAboveVersionNotification notification1 = new OreoAndAboveVersionNotification(this);
        Notification.Builder builder = notification1.getONotifications(title, body,pIntent,defSoundUri,
                icon);

        int j = 0;
        if(i>0) {
            j=i;
        }
        notification1.getManager().notify(j, builder.build());
    }
}
