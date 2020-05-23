package rs.ac.uns.ftn.eventsapp.models;

import java.time.LocalDateTime;
import java.util.Date;

public class ChatMessage {

    private String id;
    private String text;
    private String fromId;
    private String toId;
    private Long date;
    private Boolean seen;

    public ChatMessage(){}

    public ChatMessage(String id, String text, String fromId, String toId, Long date, Boolean seen){
        this.id = id;
        this.text = text;
        this.fromId = fromId;
        this.toId = toId;
        this.date = date;
        this.seen = seen;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
