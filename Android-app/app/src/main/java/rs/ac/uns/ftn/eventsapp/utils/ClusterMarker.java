package rs.ac.uns.ftn.eventsapp.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;
    private String imgUri;
    private Long tag;

    public ClusterMarker(LatLng position, String title, String snippet, String imgUri, Long tag) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.imgUri = imgUri;
        this.tag = tag;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }
}
