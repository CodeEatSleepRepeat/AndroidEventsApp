package rs.ac.uns.ftn.eventsapp.dtos;

import rs.ac.uns.ftn.eventsapp.models.User;

public class UserShareDTO {
    private User user;
    private Boolean checked;

    public UserShareDTO(User user, Boolean checked) {
        this.user = user;
        this.checked = checked;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public User getUser() {
        return user;
    }
}
