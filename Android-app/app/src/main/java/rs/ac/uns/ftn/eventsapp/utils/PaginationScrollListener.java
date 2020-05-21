package rs.ac.uns.ftn.eventsapp.utils;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    FloatingActionButton fab;
    FloatingActionButton fabMap;

    public PaginationScrollListener(LinearLayoutManager layoutManager, FloatingActionButton fab, FloatingActionButton fabMap){
        this.layoutManager = layoutManager;
        this.fab = fab;
        this.fabMap = fabMap;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if(fab!=null) {
            if (dy > 0 || dy < 0 && fab.isShown()) {
                fab.hide();
            }
        }
        if(fabMap!=null) {
            if (dy > 0 || dy < 0 && fabMap.isShown()) {
                fabMap.hide();
            }
        }
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if(!isLoading()){
            if((visibleItemCount + firstVisibleItemPosition)>=totalItemCount && firstVisibleItemPosition>=0){
                Log.d("USAO", ""+totalItemCount);
                loadMoreItems();
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && fab!=null)
            fab.show();
        if (newState == RecyclerView.SCROLL_STATE_IDLE && fabMap!=null)
            fabMap.show();
        super.onScrollStateChanged(recyclerView, newState);
    }

    protected abstract void loadMoreItems();
    public abstract boolean isLoading();
}
