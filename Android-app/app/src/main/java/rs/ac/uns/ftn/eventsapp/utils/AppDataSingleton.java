package rs.ac.uns.ftn.eventsapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.database.UserSQLiteHelper;
import rs.ac.uns.ftn.eventsapp.models.User;

public class AppDataSingleton {

    public static String IMAGE_URI = "/event/image/";
    public static String PROFILE_IMAGE_URI = "/user/image/";

    private User loggedUser;
    //TODO: ovde dodati ostale klase koje se cuvaju (npr. lista korisnikovih eventova)

    private SQLiteDatabase mDatabase;
    private UserSQLiteHelper dbUserHelper;

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
        mDatabase = dbUserHelper.getWritableDatabase();
    }

    private AppDataSingleton() {
    }

    public void create(User user) {
        if (user == null) {
            return;
        }

        deleteAll();

        //edit user table
        ContentValues cv = new ContentValues();
        cv.put(UserSQLiteHelper.COLUMN_ID, user.getId());
        cv.put(UserSQLiteHelper.COLUMN_FB_ID, user.getFacebookId());
        cv.put(UserSQLiteHelper.COLUMN_NAME, user.getName());
        cv.put(UserSQLiteHelper.COLUMN_IMAGE_URI, user.getImageUri());
        cv.put(UserSQLiteHelper.COLUMN_IMAGE_HEIGHT, user.getImageHeight());
        cv.put(UserSQLiteHelper.COLUMN_IMAGE_WIDTH, user.getImageWidth());
        cv.put(UserSQLiteHelper.COLUMN_EMAIL, user.getEmail());
        cv.put(UserSQLiteHelper.COLUMN_PASSWORD, user.getPassword());
        cv.put(UserSQLiteHelper.COLUMN_ACTIVATED, user.getActivatedAccount());
        cv.put(UserSQLiteHelper.COLUMN_SYNC_FB_EVENTS, user.getSyncFacebookEvents());
        cv.put(UserSQLiteHelper.COLUMN_SYNC_FB_PROFILE, user.getSyncFacebookProfile());
        mDatabase.insert(UserSQLiteHelper.TABLE_USER, null, cv);

        //TODO: edit other tables like event, friendship, my events, going,...

        loggedUser = user;
    }

    public User read() {
        if (loggedUser != null) {
            return loggedUser;
        }
        Cursor cursor = mDatabase.query(UserSQLiteHelper.TABLE_USER, null, null, null, null, null, UserSQLiteHelper.COLUMN_ID + " DESC");
        while (cursor.moveToNext()) {
            loggedUser = new User(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    null, null, null, null, null, null, null, null, null, null,
                    cursor.getInt(8) == 1,
                    cursor.getInt(9) == 1,
                    cursor.getInt(10) == 1);
        }
        cursor.close();
        return loggedUser;
    }

    public void update(User user) {
        ContentValues cv = new ContentValues();
        cv.put(UserSQLiteHelper.COLUMN_ID, user.getId());
        cv.put(UserSQLiteHelper.COLUMN_FB_ID, user.getFacebookId());
        cv.put(UserSQLiteHelper.COLUMN_NAME, user.getName());
        cv.put(UserSQLiteHelper.COLUMN_IMAGE_URI, user.getImageUri());
        cv.put(UserSQLiteHelper.COLUMN_IMAGE_HEIGHT, user.getImageHeight());
        cv.put(UserSQLiteHelper.COLUMN_IMAGE_WIDTH, user.getImageWidth());
        cv.put(UserSQLiteHelper.COLUMN_EMAIL, user.getEmail());
        cv.put(UserSQLiteHelper.COLUMN_PASSWORD, user.getPassword());
        cv.put(UserSQLiteHelper.COLUMN_ACTIVATED, user.getActivatedAccount());
        cv.put(UserSQLiteHelper.COLUMN_SYNC_FB_EVENTS, user.getSyncFacebookEvents());
        cv.put(UserSQLiteHelper.COLUMN_SYNC_FB_PROFILE, user.getSyncFacebookProfile());
        mDatabase.update(UserSQLiteHelper.TABLE_USER, cv, null, null);

        this.loggedUser = user;
    }

    public void deleteAll() {
        loggedUser = null;

        mDatabase.delete(UserSQLiteHelper.TABLE_USER, null, null);
        //TODO: ovde dodati brisanje ostalih tabela (klasa) - ovo je za loggout
    }

    public boolean isLoggedIn() {
        return loggedUser != null;
    }

    public User getLoggedUser() {
        return loggedUser;
    }


}
