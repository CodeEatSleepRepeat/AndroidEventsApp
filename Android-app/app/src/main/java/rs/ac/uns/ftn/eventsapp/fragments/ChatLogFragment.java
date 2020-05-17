package rs.ac.uns.ftn.eventsapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.views.MessageFromChatPartnerItem;
import rs.ac.uns.ftn.eventsapp.views.MessageToChatPartnerItem;


public class ChatLogFragment extends Fragment {

    GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<GroupieViewHolder>();
    User chatPartner;

    public ChatLogFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_log, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Postaviti na naslov ime chat partnera
        //getActivity().getActionBar().setTitle("Veljko the man");

        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerview_chat_log);
        recyclerView.setAdapter(adapter);

        getMessages(recyclerView);

        Button btnSendMessage = getActivity().findViewById(R.id.btn_send_chat_log);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        // TODO: Pokupi text sa poruke, i posalji korisniku
    }

    private void getMessages(RecyclerView recyclerView){
        // TODO: Pokupiti sve poruke izmedju naseg korisnika i prosledjenog chatPartnera i
        //  ispisati ih
        adapter.add(new MessageToChatPartnerItem("Pozdrav Veljko",
                new User(1l, "Veljko", "nema", "", "")));
        adapter.add(new MessageFromChatPartnerItem("Pozdrav Veljko!",
                new User(1l, "Veljko", "nema", "", "")));
        adapter.add(new MessageToChatPartnerItem("Sta je anketa?",
                new User(1l, "Veljko", "nema", "", "")));
        adapter.add(new MessageFromChatPartnerItem("Sta?",
                new User(1l, "Veljko", "nema", "", "")));
        adapter.add(new MessageToChatPartnerItem("STA JE A-N-K-E-T-A?",
                new User(1l, "Veljko", "nema", "", "")));
        adapter.add(new MessageFromChatPartnerItem("Pa ja ovo prvi put vidim!",
                new User(1l, "Veljko", "nema", "", "")));


        recyclerView.scrollToPosition(adapter.getItemCount()-1);

    }
}
