package rs.ac.uns.ftn.eventsapp.models;

public class ChatMessage {

    private Long id;
    private String text;
    private Long fromId;
    private Long toId;
    private Long timestamp;

    public ChatMessage(Long id, String text, Long fromId, Long toId, Long timestamp){
        this.id = id;
        this.text = text;
        this.fromId = fromId;
        this.toId = toId;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
