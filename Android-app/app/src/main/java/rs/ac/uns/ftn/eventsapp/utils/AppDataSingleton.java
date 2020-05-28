package rs.ac.uns.ftn.eventsapp.utils;

import android.content.Context;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.ListIterator;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.database.MyEventsSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.database.UserSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.models.SyncStatus;
import rs.ac.uns.ftn.eventsapp.models.User;

public class AppDataSingleton {

    public static String IMAGE_URI = "/event/image/";
    public static String PROFILE_IMAGE_URI = "/user/image/";

    private User loggedUser;    //ovde se nalazi i user events samo sa id
    private ArrayList<EventDTO> userEvents;
    private ArrayList<EventDTO> goingEvents;

    private UserSQLiteHelper dbUserHelper;
    private MyEventsSQLiteHelper dbMyEventsHelper;
    //TODO: add other tables like event, friendship, my events, going,...

    private static final AppDataSingleton ourInstance = new AppDataSingleton();

    public static AppDataSingleton getInstance() {
        return ourInstance;
    }

    public void setContext(Context context) {
        if (IMAGE_URI.startsWith("/")) {
            IMAGE_URI = context.getString(R.string.localhost_uri) + IMAGE_URI;
            PROFILE_IMAGE_URI = context.getString(R.string.localhost_uri) + PROFILE_IMAGE_URI;
        }
        dbUserHelper = new UserSQLiteHelper(context);
        dbMyEventsHelper = new MyEventsSQLiteHelper(context);
    }

    private AppDataSingleton() {
    }


    public void createUser(User user) {
        if (user == null) return;
        dbUserHelper.delete();
        dbUserHelper.create(user);
        this.loggedUser = user;
    }

    public void createUserEvents(ArrayList<EventDTO> userEvents) {
        if (userEvents == null) return;

        dbMyEventsHelper.deleteAll();
        for (EventDTO e : userEvents) {
            e.setUpdated_time(ZonedDateTime.now());
            dbMyEventsHelper.create(e);
        }
        this.userEvents = userEvents;
    }

    public User getLoggedUser() {
        if (loggedUser != null) {
            return loggedUser;
        }
        loggedUser = dbUserHelper.read();
        return loggedUser;
    }

    public ArrayList<EventDTO> getUserEvents() {
        if (userEvents != null) {
            return userEvents;
        }
        userEvents = dbMyEventsHelper.read();
        return userEvents;
    }

    public EventDTO getUserEvent(Long id) {
        ListIterator<EventDTO> iterator = getUserEvents().listIterator();
        while (iterator.hasNext()) {
            EventDTO next = iterator.next();
            if (next.getId() == id) {
                return next;
            }
        }
        return null;
    }

    public void updateUser(User user) {
        dbUserHelper.update(user);
        this.loggedUser = user;
    }

    private void updateUserEvent(EventDTO event) {
        event = dbMyEventsHelper.update(event);
        ListIterator<EventDTO> iterator = getUserEvents().listIterator();
        while (iterator.hasNext()) {
            EventDTO next = iterator.next();
            if (next.getId() == event.getId()) {
                //Replace element
                iterator.set(event);
                return;
            }
        }
    }

    private void deleteUserEventPhysical(Long id) {
        dbMyEventsHelper.deletePhysical(id);
        ListIterator<EventDTO> iterator = getUserEvents().listIterator();
        while (iterator.hasNext()) {
            EventDTO next = iterator.next();
            if (next.getId() == id) {
                //Remove element
                iterator.remove();
                return;
            }
        }
    }

    public void addUserEvent(EventDTO event) {
        getUserEvents().add(event);
        dbMyEventsHelper.create(event);
    }

    public void updateUserEvents(ArrayList<EventDTO> userEvents) {
        for (EventDTO e : userEvents) {
            if (e.getSyncStatus() == SyncStatus.DELETE) {
                deleteUserEventPhysical(e.getId());
            } else {
                if (dbMyEventsHelper.exists(e.getId())) {
                    updateUserEvent(e);
                } else {
                    addUserEvent(e);
                }
            }
        }
    }

    /**
     * This is for logout to remove all data from SQLite
     */
    public void deleteAllPhysical() {
        loggedUser = null;
        userEvents = new ArrayList<>();

        dbUserHelper.delete();
        dbMyEventsHelper.deleteAll();
        //TODO: ovde dodati brisanje ostalih tabela (klasa) - ovo je za loggout
    }

    /**
     * This is logical deletion
     *
     * @param e
     * @return
     */
    public boolean deleteUserEventLogical(EventDTO e) {
        if (dbMyEventsHelper.deleteLogical(e) != null) {
            return this.userEvents.remove(e);
        }
        return false;
    }

    public boolean isLoggedIn() {
        return loggedUser != null;
    }


}
