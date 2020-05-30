package rs.ac.uns.ftn.eventsapp.utils;

import android.content.Context;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.ListIterator;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.database.GoingInterestedEventsSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.database.MyEventsSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.database.UserSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.GoingInterestedEventsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UpdateEventDTO;
import rs.ac.uns.ftn.eventsapp.models.GoingInterestedStatus;
import rs.ac.uns.ftn.eventsapp.models.SyncStatus;
import rs.ac.uns.ftn.eventsapp.models.User;

public class AppDataSingleton {

    public static String IMAGE_URI = "/event/image/";
    public static String PROFILE_IMAGE_URI = "/user/image/";

    private User loggedUser;    //ovde se nalazi i user events samo sa id
    private ArrayList<EventDTO> userEvents;
    private ArrayList<GoingInterestedEventsDTO> goingEvents;
    private ArrayList<GoingInterestedEventsDTO> interestedEvents;

    private UserSQLiteHelper dbUserHelper;
    private MyEventsSQLiteHelper dbMyEventsHelper;
    private GoingInterestedEventsSQLiteHelper dbGoingInterestedEventsHelper;

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
        dbGoingInterestedEventsHelper = new GoingInterestedEventsSQLiteHelper(context);
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

    public ArrayList<GoingInterestedEventsDTO> getGoingEvents() {
        if (goingEvents != null) {
            return goingEvents;
        }
        goingEvents = dbGoingInterestedEventsHelper.readEventsWithStatus(GoingInterestedStatus.GOING);
        return goingEvents;
    }

    public ArrayList<GoingInterestedEventsDTO> getInterestedEvents() {
        if (interestedEvents != null) {
            return interestedEvents;
        }
        interestedEvents = dbGoingInterestedEventsHelper.readEventsWithStatus(GoingInterestedStatus.INTERESTED);
        return interestedEvents;
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

    private void updateGIEvent(GoingInterestedEventsDTO event) {
        event = dbGoingInterestedEventsHelper.update(event);

        ListIterator<GoingInterestedEventsDTO> iterator = getGoingEvents().listIterator();
        while (iterator.hasNext()) {
            GoingInterestedEventsDTO next = iterator.next();
            if (next.getEvent().getId() == event.getEvent().getId()) {
                //found event in going list
                if (event.getStatus() == GoingInterestedStatus.GOING) {
                    //Replace element
                    iterator.set(event);
                    return;
                } else {
                    //remove from here and add it to interested list
                    iterator.remove();
                    getInterestedEvents().add(event);
                    return;
                }
            }
        }

        ListIterator<GoingInterestedEventsDTO> iterator2 = getGoingEvents().listIterator();
        while (iterator2.hasNext()) {
            GoingInterestedEventsDTO next = iterator2.next();
            if (next.getEvent().getId() == event.getEvent().getId()) {
                //found event in going list
                if (event.getStatus() == GoingInterestedStatus.INTERESTED) {
                    //Replace element
                    iterator2.set(event);
                    return;
                } else {
                    //remove from here and add it to going list
                    iterator2.remove();
                    getGoingEvents().add(event);
                    return;
                }
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

    private void deleteGIEventPhysical(Long id) {
        dbGoingInterestedEventsHelper.deletePhysical(id);
        ListIterator<GoingInterestedEventsDTO> iterator = getGoingEvents().listIterator();
        while (iterator.hasNext()) {
            GoingInterestedEventsDTO next = iterator.next();
            if (next.getEvent().getId() == id) {
                //Remove element
                iterator.remove();
                return;
            }
        }

        ListIterator<GoingInterestedEventsDTO> iterator2 = getInterestedEvents().listIterator();
        while (iterator2.hasNext()) {
            GoingInterestedEventsDTO next = iterator2.next();
            if (next.getEvent().getId() == id) {
                //Remove element
                iterator2.remove();
                return;
            }
        }
    }

    public void addUserEvent(EventDTO event) {
        getUserEvents().add(event);
        dbMyEventsHelper.create(event);
    }

    private void addGIEvent(GoingInterestedEventsDTO event) {
        if (event.getStatus() == GoingInterestedStatus.GOING) {
            getGoingEvents().add(event);
        } else {
            getInterestedEvents().add(event);
        }
        dbGoingInterestedEventsHelper.create(event);
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

    public void updateGIEvents(ArrayList<GoingInterestedEventsDTO> forUpdate) {
        //remove current events
        goingEvents = new ArrayList<>();
        interestedEvents = new ArrayList<>();
        dbGoingInterestedEventsHelper.deleteAll();

        for (GoingInterestedEventsDTO e : forUpdate) {
            if (e.getStatus() == GoingInterestedStatus.GOING) {
                goingEvents.add(e);
                addGIEvent(e);
            } else if (e.getStatus() == GoingInterestedStatus.INTERESTED) {
                interestedEvents.add(e);
                addGIEvent(e);
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
        dbGoingInterestedEventsHelper.deleteAll();
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

    /**
     * This is logical deletion
     *
     * @param e
     * @return
     */
    public boolean deleteGoingEventLogical(GoingInterestedEventsDTO e) {
        if (dbGoingInterestedEventsHelper.deleteLogical(e) != null) {
            return getGoingEvents().remove(e);
        }
        return false;
    }

    /**
     * This is logical deletion
     *
     * @param e
     * @return
     */
    public boolean deleteInterestedEventLogical(GoingInterestedEventsDTO e) {
        if (dbGoingInterestedEventsHelper.deleteLogical(e) != null) {
            return getInterestedEvents().remove(e);
        }
        return false;
    }

    public boolean isLoggedIn() {
        return loggedUser != null;
    }


    public ArrayList<UpdateEventDTO> getUserEventsForUpdate(Long lastSyncTime) {
        ArrayList<UpdateEventDTO> forUpdate = new ArrayList<>();
        for (EventDTO e : getUserEvents()) {
            if (e.getUpdated_time().toInstant().toEpochMilli() > lastSyncTime && e.getSyncStatus() != SyncStatus.ADD) {
                //needs sync
                forUpdate.add(new UpdateEventDTO(e.getId(), e.getLatitude(), e.getLongitude(), e.getName(), e.getPlace(), e.getDescription(), e.getType(), e.getStart_time(), e.getEnd_time(), e.getPrivacy(), e.getSyncStatus(), e.getUpdated_time()));
            }
        }
        return forUpdate;
    }

    public ArrayList<GoingInterestedEventsDTO> getGoingInterestedEventsForUpdate(Long lastSyncTime) {
        return dbGoingInterestedEventsHelper.readGIEventsForUpdate(lastSyncTime);
    }
}
