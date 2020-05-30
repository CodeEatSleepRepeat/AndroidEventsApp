package rs.ac.uns.ftn.eventsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.ZonedDateTime;

import rs.ac.uns.ftn.eventsapp.models.SyncStatus;
import rs.ac.uns.ftn.eventsapp.models.User;

public class UserSQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDatabase;

    private static final String TABLE_USER = "user";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FB_ID = "facebookId";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMAGE_URI = "imageUri";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ACTIVATED = "activatedAccount";
    private static final String COLUMN_SYNC_FB_EVENTS = "syncFacebookEvents";
    private static final String COLUMN_SYNC_FB_PROFILE = "syncFacebookProfile";
    private static final String COLUMN_SYNC_STATUS = "syncStatus";
    private static final String COLUMN_UPDATED_TIME = "updated_time";

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_USER + "("
            + COLUMN_ID + " integer primary key , "
            + COLUMN_FB_ID + " text, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_IMAGE_URI + " text, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_PASSWORD + " text not null, "
            + COLUMN_ACTIVATED + " integer default 0, "
            + COLUMN_SYNC_FB_EVENTS + " integer default 0, "
            + COLUMN_SYNC_FB_PROFILE + " integer default 0, "
            + COLUMN_SYNC_STATUS + " text, "
            + COLUMN_UPDATED_TIME + " text"
            + ");";


    public UserSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        AndroidThreeTen.init(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void create(User user) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, user.getId());
        cv.put(COLUMN_FB_ID, user.getFacebookId());
        cv.put(COLUMN_NAME, user.getName());
        cv.put(COLUMN_IMAGE_URI, user.getImageUri());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_ACTIVATED, user.getActivatedAccount());
        cv.put(COLUMN_SYNC_FB_EVENTS, user.getSyncFacebookEvents());
        cv.put(COLUMN_SYNC_FB_PROFILE, user.getSyncFacebookProfile());
        cv.put(COLUMN_SYNC_STATUS, user.getSyncStatus().toString());
        cv.put(COLUMN_UPDATED_TIME, user.getUpdated_time().toString());

        mDatabase = getWritableDatabase();
        mDatabase.insert(TABLE_USER, null, cv);
        mDatabase.close();
    }

    public User read() {
        User loggedUser = null;
        mDatabase = getReadableDatabase();
        Cursor cursor = mDatabase.query(TABLE_USER, null, null, null, null, null, COLUMN_ID + " DESC");
        while (cursor.moveToNext()) {
            loggedUser = new User(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    null, null, null, null, null, null, null, null, null, null,
                    cursor.getInt(6) == 1,
                    cursor.getInt(7) == 1,
                    cursor.getInt(8) == 1,
                    SyncStatus.valueOf(cursor.getString(9)),
                    ZonedDateTime.parse(cursor.getString(10))
            );
        }
        cursor.close();
        mDatabase.close();

        return loggedUser;
    }

    public User update(User user) {
        user.setUpdated_time(ZonedDateTime.now());
        user.setSyncStatus(SyncStatus.UPDATE);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, user.getId());
        cv.put(COLUMN_FB_ID, user.getFacebookId());
        cv.put(COLUMN_NAME, user.getName());
        cv.put(COLUMN_IMAGE_URI, user.getImageUri());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_ACTIVATED, user.getActivatedAccount());
        cv.put(COLUMN_SYNC_FB_EVENTS, user.getSyncFacebookEvents());
        cv.put(COLUMN_SYNC_FB_PROFILE, user.getSyncFacebookProfile());
        cv.put(COLUMN_SYNC_STATUS, user.getSyncStatus().toString());
        cv.put(COLUMN_UPDATED_TIME, user.getUpdated_time().toString());

        mDatabase = getWritableDatabase();
        mDatabase.update(TABLE_USER, cv, null, null);
        mDatabase.close();

        return user;
    }

    public void delete(){
        mDatabase = getWritableDatabase();
        mDatabase.delete(TABLE_USER, null, null);
        mDatabase.close();
    }
}
