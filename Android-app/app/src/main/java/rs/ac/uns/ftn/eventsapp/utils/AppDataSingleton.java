package rs.ac.uns.ftn.eventsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.ListIterator;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.NoServerActivity;
import rs.ac.uns.ftn.eventsapp.activities.SplashScreenActivity;
import rs.ac.uns.ftn.eventsapp.database.GoingInterestedEventsSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.database.MyEventsSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.database.UserSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.GoingInterestedEventsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UpdateEventDTO;
import rs.ac.uns.ftn.eventsapp.fragments.GoingEventsListFragment;
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

    public String SERVER_IP = "";

    private UserSQLiteHelper dbUserHelper;
    private MyEventsSQLiteHelper dbMyEventsHelper;
    private GoingInterestedEventsSQLiteHelper dbGoingInterestedEventsHelper;

    //TODO: add other tables like event, friendship, my events, going,...

    private static final AppDataSingleton ourInstance = new AppDataSingleton();

    public static AppDataSingleton getInstance() {
        return ourInstance;
    }

    public void setContext(Context context) {
        //get server ip address from preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
        SERVER_IP = sharedPreferences.getString(NoServerActivity.preferenceServerIpAddress, "http://10.0.2.2:8080");

        //set uris to images on server
        if (IMAGE_URI.startsWith("/")) {
            IMAGE_URI = SERVER_IP + IMAGE_URI;
            PROFILE_IMAGE_URI = SERVER_IP + PROFILE_IMAGE_URI;
        }

        //init DB helpers
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

    public ArrayList<EventDTO> getGoingEventsDTO(){
        ArrayList<EventDTO> retList = new ArrayList<>();
        for (GoingInterestedEventsDTO event: getGoingEvents()) {
            retList.add(event.getEvent());
        }
        return retList;
    }

    public ArrayList<EventDTO> getInterestedEventsDTO(){
        ArrayList<EventDTO> retList = new ArrayList<>();
        for (GoingInterestedEventsDTO event: getInterestedEvents()) {
            retList.add(event.getEvent());
        }
        return retList;
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

    public void updateGIEvent(GoingInterestedEventsDTO event) {
        dbGoingInterestedEventsHelper.deletePhysical(event.getEvent().getId());
        dbGoingInterestedEventsHelper.create(event);

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

    public boolean isUserGoingTo(Long id){
        return dbGoingInterestedEventsHelper.exists(id, GoingInterestedStatus.GOING);
    }

    public boolean isUserInterestedTo(Long id){
        return dbGoingInterestedEventsHelper.exists(id, GoingInterestedStatus.INTERESTED);
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

    public void deleteGoingEventPhysical(Long id) {
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
    }

    public void deleteInterestedEventPhysical(Long id) {
        dbGoingInterestedEventsHelper.deletePhysical(id);

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

    public void addGIEvent(GoingInterestedEventsDTO event) {
        if (event.getStatus() == GoingInterestedStatus.GOING) {
            getGoingEvents().add(event);
        } else {
            getInterestedEvents().add(event);
        }
        dbGoingInterestedEventsHelper.create(event);
    }

    public void setEventToGoing(EventDTO event){
        if (isUserInterestedTo(event.getId())){
            updateGIEvent(new GoingInterestedEventsDTO(event, GoingInterestedStatus.GOING));
        } else {
            addGIEvent(new GoingInterestedEventsDTO(event, GoingInterestedStatus.GOING));
        }
    }

    public void setEventToInterested(EventDTO event){
        if (isUserGoingTo(event.getId())){
            updateGIEvent(new GoingInterestedEventsDTO(event, GoingInterestedStatus.INTERESTED));
        } else {
            addGIEvent(new GoingInterestedEventsDTO(event, GoingInterestedStatus.INTERESTED));
        }
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
            addGIEvent(e);
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

    public boolean isLoggedInFBUser() {
        if (loggedUser == null) return false;
        if (loggedUser.getFacebookId().equals("")){
            return false;
        }
        return true;
    }

    public ArrayList<UpdateEventDTO> getUserEventsForUpdate(Long lastSyncTime) {
        ArrayList<UpdateEventDTO> forUpdate = new ArrayList<>();
        for (EventDTO e : getUserEvents()) {
            if (e.getUpdated_time().toInstant().toEpochMilli() > lastSyncTime && e.getSyncStatus() != SyncStatus.ADD) {
                //needs sync
                forUpdate.add(new UpdateEventDTO(e.getId(), e.getLatitude(), e.getLongitude(), e.getName(),
                        e.getPlace(), e.getDescription(), e.getType(), e.getStart_time(), e.getEnd_time(),
                        e.getPrivacy(), e.getSyncStatus(), e.getUpdated_time(), e.getImageUri()));
            }
        }
        return forUpdate;
    }

    public ArrayList<GoingInterestedEventsDTO> getGoingInterestedEventsForUpdate(Long lastSyncTime) {
        return dbGoingInterestedEventsHelper.readGIEventsForUpdate(lastSyncTime);
    }
}
