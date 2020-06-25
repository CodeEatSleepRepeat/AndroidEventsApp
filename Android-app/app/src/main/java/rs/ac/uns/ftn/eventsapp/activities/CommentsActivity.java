package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.CommentsArrayAdapter;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.CommentDTO;
import rs.ac.uns.ftn.eventsapp.dtos.CreateCommentDTO;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.models.Comment;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class CommentsActivity extends AppCompatActivity {

    private static final int PAGE_START = 0;
    List<CommentDTO> comments = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;
    private Long eventId = null;
    private Button button;
    private EditText newComment;

    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        newComment = findViewById(R.id.commentEditText);
        button = findViewById(R.id.commentBtn);
        RecyclerView recyclerView = findViewById(R.id.commentsListView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentsArrayAdapter(comments, this);
        recyclerView.setAdapter(adapter);

        if(AppDataSingleton.getInstance().getLoggedUser()==null){
            button.setAlpha(.5f);
            button.setClickable(false);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), R.string.notLoggedInComment, Toast.LENGTH_LONG).show();
                }
            });
        }else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addComment();
                }
            });
        }

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, null, null) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                getComments(currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        addNoInternetListener();
    }

    /**
     * Ovo je za proveru konekcije interneta i odmah reaguje na promene, ali ne radi bas ako se vracam iz aktivnosti bez interneta
     */
    private void addNoInternetListener() {
        noInternetDialog = new NoInternetDialog.Builder(this)
                .setBgGradientStart(getResources().getColor(R.color.colorPrimary)) // Start color for background gradient
                .setBgGradientCenter(getResources().getColor(R.color.colorPrimaryDark)) // Center color for background gradient
                .setWifiLoaderColor(R.color.colorBlack) // Set custom color for wifi loader
                .setCancelable(true)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noInternetDialog != null)
            noInternetDialog.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        eventId = Long.valueOf(getIntent().getStringExtra("EventId"));
        comments.clear();
        currentPage = PAGE_START;
        getComments(PAGE_START);
    }

    private void getComments(int num) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<CommentDTO>> cs = e.getComments(eventId, num);
        cs.enqueue(new Callback<List<CommentDTO>>() {
            @Override
            public void onResponse(Call<List<CommentDTO>> call, Response<List<CommentDTO>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                isLoading = false;
                comments.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CommentDTO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    private void addComment(){
        if(newComment.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.blankFieldError, Toast.LENGTH_LONG).show();
        }else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                    .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                    .build();
            EventsAppAPI e = retrofit.create(EventsAppAPI.class);
            Call<CommentDTO> s = e.addComment(new CreateCommentDTO(newComment.getText().toString(), eventId, AppDataSingleton.getInstance().getLoggedUser().getId()));
            s.enqueue(new Callback<CommentDTO>() {
                @Override
                public void onResponse(Call<CommentDTO> call, Response<CommentDTO> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    comments.add(response.body());
                    adapter.notifyDataSetChanged();
                    newComment.setText(null);
                    Toast.makeText(getApplicationContext(), R.string.commentAdded, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<CommentDTO> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
