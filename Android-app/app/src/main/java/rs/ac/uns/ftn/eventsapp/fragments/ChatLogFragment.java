package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import java.util.Calendar;
import java.util.Date;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.views.MessageFromChatPartnerItem;
import rs.ac.uns.ftn.eventsapp.views.MessageToChatPartnerItem;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;


public class ChatLogFragment extends Fragment {

    GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<GroupieViewHolder>();
    FirebaseUserDTO chatPartner;
    FirebaseUserDTO currentUser;
    DatabaseReference latestMessageOfThisChatLog;

    public ChatLogFragment() {
    }

    public static ChatLogFragment newInstance(String param1, String param2) {
        ChatLogFragment fragment = new ChatLogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_log, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerview_chat_log);
        recyclerView.setAdapter(adapter);

        getChatPartnerFromFirebase();
        getCurrentUserAndChatLogMessagesFromFirebase(recyclerView);
        getMessages(recyclerView);
        setLatestMessageOfUserToSeen();

        Button btnSendMessage = getActivity().findViewById(R.id.btn_send_chat_log);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void getCurrentUserAndChatLogMessagesFromFirebase(final RecyclerView recyclerView) {
        String uid = FirebaseAuth.getInstance().getUid();
        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();
        currentUser = new FirebaseUserDTO(uid, loggedUser.getName(), loggedUser.getImageUri(),
                loggedUser.getEmail());
//        DatabaseReference loggedUserRef =
//                FirebaseDatabase.getInstance().getReference("/users/" + uid);
//        loggedUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                currentUser = dataSnapshot.getValue(FirebaseUserDTO.class);
//
//                getMessages(recyclerView);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void getChatPartnerFromFirebase() {
        Intent chatPartnerInfoInIntent = getActivity().getIntent();
        String uidOfUser =
                chatPartnerInfoInIntent.getStringExtra(UserSimpleItem.EXTRA_USER_FIREBASE_UID);
        String userUsername =
                chatPartnerInfoInIntent.getStringExtra(UserSimpleItem.EXTRA_USER_NAME);
        String userEmail =
                chatPartnerInfoInIntent.getStringExtra(UserSimpleItem.EXTRA_USER_EMAIL);
        String userImageUrl =
                chatPartnerInfoInIntent.getStringExtra(UserSimpleItem.EXTRA_USER_IMAGE_PATH);
        chatPartner = new FirebaseUserDTO(uidOfUser, userUsername, userImageUrl, userEmail);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(chatPartner.getUsername());
    }

    private void getMessages(final RecyclerView recyclerView){
        String uidOfLoggedUser = FirebaseAuth.getInstance().getUid();
        String uidOfChatPartner = chatPartner.getUid();

        DatabaseReference userMessagesRef = FirebaseDatabase.getInstance().getReference("/user-messages/"
                + uidOfLoggedUser + "/" + uidOfChatPartner);

        userMessagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);

                if(chatMessage != null){
                    if(chatMessage.getFromId().equals(FirebaseAuth.getInstance().getUid())){
                        adapter.add(new MessageToChatPartnerItem(chatMessage.getText(), currentUser));
                    }
                    else{
                        adapter.add(new MessageFromChatPartnerItem(chatMessage.getText(), chatPartner));
                    }
                }

                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage() {

        EditText edittextText = getActivity().findViewById(R.id.text_input_message_chat_log);
        String text = edittextText.getText().toString();
        String fromId = currentUser.getUid();
        String toId = chatPartner.getUid();
        Long date = System.currentTimeMillis();

        if(fromId == null) return;

        //pravimo cvorove u firebase za current usera i chatpartnera
        DatabaseReference messageReferenceCurrentUser = FirebaseDatabase.getInstance().getReference(
                "/user-messages/" + fromId + "/" + toId).push();

        DatabaseReference messageReferenceChatPartner = FirebaseDatabase.getInstance().getReference(
                "/user-messages/" + toId + "/" + fromId).push();

        ChatMessage chatMessage = new ChatMessage(messageReferenceCurrentUser.getKey(),
                text, fromId, toId, date, false);

        messageReferenceCurrentUser.setValue(chatMessage)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        EditText editTextMessage =
                                getActivity().findViewById(R.id.text_input_message_chat_log);
                        editTextMessage.setText("");
                    }
                });
        messageReferenceChatPartner.setValue(chatMessage);

        /*
        * Sada azuriramo poslednju poruku koja je razmenjena izmedju korisnika
        * */
        FirebaseDatabase.getInstance().getReference("/latest-messages/"+fromId+"/"+toId)
        .setValue(chatMessage);
        FirebaseDatabase.getInstance().getReference("/latest-messages/"+toId+"/"+fromId)
                .setValue(chatMessage);

    }

    private void setLatestMessageOfUserToSeen(){
        latestMessageOfThisChatLog= FirebaseDatabase.getInstance().getReference(
                "/latest" +
                "-messages/"+currentUser.getUid()+"/"+chatPartner.getUid());
        latestMessageOfThisChatLog.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latestMessageOfThisChatLog.removeEventListener(this);
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                chatMessage.setSeen(true);
                latestMessageOfThisChatLog.setValue(chatMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
