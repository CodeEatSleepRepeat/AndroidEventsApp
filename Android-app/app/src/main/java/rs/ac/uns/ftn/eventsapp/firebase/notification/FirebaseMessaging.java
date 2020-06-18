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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ChatLogActivity;
import rs.ac.uns.ftn.eventsapp.activities.FriendRequestsActivity;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.NotificationTypeEnum;
import rs.ac.uns.ftn.eventsapp.fragments.ChatLogFragment;

import static rs.ac.uns.ftn.eventsapp.views.UserSimpleItem.EXTRA_USER_EMAIL;
import static rs.ac.uns.ftn.eventsapp.views.UserSimpleItem.EXTRA_USER_FIREBASE_UID;
import static rs.ac.uns.ftn.eventsapp.views.UserSimpleItem.EXTRA_USER_IMAGE_PATH;
import static rs.ac.uns.ftn.eventsapp.views.UserSimpleItem.EXTRA_USER_NAME;

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
        String notificationType = remoteMessage.getData().get("type");


        assert user != null;
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //kreiranje razlicitog intenta u zavisnosti tipa notifikacije
        assert notificationType != null;
        if(notificationType.equals(NotificationTypeEnum.INVITATION.toString())){
            //TODO: Napraviti intent koji ide ka invitacijama
        }
        else if(notificationType.equals(NotificationTypeEnum.FRIEND_REQUEST.toString())){
            makeIntentToFriendRequestThenMakeNotification(icon, body, title, defSoundUri, i);
        }
        else if(notificationType.equals(NotificationTypeEnum.MESSAGE.toString())){
            getChatPartnerInfoThenMakeIntentAndNotification(user, icon, body, title, defSoundUri,
                    i);
        }
    }

    private void makeIntentToFriendRequestThenMakeNotification(String icon, String body, String title, Uri defSoundUri, int i) {
        Intent intent = new Intent(getApplicationContext(), FriendRequestsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), i, intent,
                PendingIntent.FLAG_ONE_SHOT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendOAndAboveVersionNotification(icon, body, title, defSoundUri,
                    pIntent, i);
        }
        else{
            sendNormalNotification(icon, body, title, defSoundUri,
                    pIntent, i);
        }
    }

    private void getChatPartnerInfoThenMakeIntentAndNotification(String chatPartnerUid, final String icon,
                                                                 final String body, final String title, final Uri defSoundUri, final int i) {
        final DatabaseReference chatPartnerReference = FirebaseDatabase.getInstance().getReference().child("users").child(chatPartnerUid);
        chatPartnerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUserDTO chatPartner = dataSnapshot.getValue(FirebaseUserDTO.class);
                if(chatPartner != null){
                    chatPartnerReference.removeEventListener(this);
                    Intent intent = new Intent(getApplicationContext(), ChatLogActivity.class);
                    intent.putExtra(EXTRA_USER_FIREBASE_UID, chatPartner.getUid());
                    intent.putExtra(EXTRA_USER_NAME, chatPartner.getUsername());
                    intent.putExtra(EXTRA_USER_IMAGE_PATH, chatPartner.getProfileImageUrl());
                    intent.putExtra(EXTRA_USER_EMAIL, chatPartner.getEmail());

                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), i, intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        sendOAndAboveVersionNotification(icon, body, title, defSoundUri,
                                pIntent, i);
                    }
                    else{
                        sendNormalNotification(icon, body, title, defSoundUri,
                                pIntent, i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
