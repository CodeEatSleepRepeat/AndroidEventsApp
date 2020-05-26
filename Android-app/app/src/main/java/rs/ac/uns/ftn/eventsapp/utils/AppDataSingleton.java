package rs.ac.uns.ftn.eventsapp.utils;

import android.content.Context;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ListIterator;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.database.MyEventsSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.database.UserSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.models.SyncStatus;
import rs.ac.uns.ftn.eventsapp.models.User;

public class AppDataSingleton {

    public static String IMAGE_URI = "/event/image/";
    public static String PROFILE_IMAGE_URI = "/user/image/";

    private User loggedUser;    //ovde se nalazi i user events samo sa id
    private ArrayList<Event> userEvents;


    private UserSQLiteHelper dbUserHelper;
    private MyEventsSQLiteHelper dbMyEventsHelper;


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


    //TODO: add other tables like event, friendship, my events, going,...
    public void createUser(User user) {
        if (user == null) return;
        dbUserHelper.delete();
        dbUserHelper.create(user);
        this.loggedUser = user;
    }

    public void createUserEvents(ArrayList<Event> userEvents) {
        if (userEvents == null) return;

        dbMyEventsHelper.deleteAll();
        for (Event e : userEvents) {
            e.setUpdated_time(new Timestamp(System.currentTimeMillis()));
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

    public ArrayList<Event> getUserEvents() {
        if (userEvents != null) {
            return userEvents;
        }
        userEvents = dbMyEventsHelper.read();
        return userEvents;
    }

    public Event getUserEvent(Long id) {
        ListIterator<Event> iterator = getUserEvents().listIterator();
        while (iterator.hasNext()) {
            Event next = iterator.next();
            if (next.getEventId() == id) {
                return next;
            }
        }
        return null;
    }

    public void updateUser(User user) {
        dbUserHelper.update(user);
        this.loggedUser = user;
    }

    public void updateUserEvent(Event event) {
        event = dbMyEventsHelper.update(event);
        ListIterator<Event> iterator = getUserEvents().listIterator();
        while (iterator.hasNext()) {
            Event next = iterator.next();
            if (next.getEventId() == event.getEventId()) {
                //Replace element
                iterator.set(event);
                return;
            }
        }
    }

    public ArrayList<Event> updateUserEvents(ArrayList<Event> userEvents) {
        dbMyEventsHelper.deleteAll();
        for (Event e : userEvents) {
            e.setUpdated_time(new Timestamp(System.currentTimeMillis()));
            e.setSyncStatus(SyncStatus.UPDATE);
            dbMyEventsHelper.create(e);
        }
        this.userEvents = userEvents;
        return this.userEvents;
    }

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
    public boolean deleteUserEventLogical(Event e) {
        if (dbMyEventsHelper.deleteLogical(e) != null) {
            return this.userEvents.remove(e);
        }
        return false;
    }

    public boolean isLoggedIn() {
        return loggedUser != null;
    }


}
