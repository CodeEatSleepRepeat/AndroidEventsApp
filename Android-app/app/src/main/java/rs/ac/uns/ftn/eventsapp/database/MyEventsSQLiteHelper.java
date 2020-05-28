package rs.ac.uns.ftn.eventsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.ZonedDateTime;
import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.SyncStatus;

public class MyEventsSQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDatabase;

    private static final String TABLE_MY_EVENTS = "my_events";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URI = "imageUri";
    private static final String COLUMN_EVENT_TYPE = "eventType";
    private static final String COLUMN_PRIVACY = "privacy";
    private static final String COLUMN_START_TIME = "startTime";
    private static final String COLUMN_END_TIME = "endTime";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_SYNC_STATUS = "syncStatus";
    private static final String COLUMN_UPDATED_TIME = "updated_time";
    private static final String COLUMN_CREATED_TIME = "created_time";
    private static final String COLUMN_OWNER = "owner";

    private static final String DATABASE_NAME = "my_events.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_MY_EVENTS + "("
            + COLUMN_ID + " integer primary key , "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_IMAGE_URI + " text, "
            + COLUMN_EVENT_TYPE + " text, "
            + COLUMN_PRIVACY + " text, "
            + COLUMN_START_TIME + " text, "
            + COLUMN_END_TIME + " text, "
            + COLUMN_PLACE + " text,"
            + COLUMN_LATITUDE + " integer default 0, "
            + COLUMN_LONGITUDE + " integer default 0, "
            + COLUMN_SYNC_STATUS + " text, "
            + COLUMN_UPDATED_TIME + " text, "
            + COLUMN_CREATED_TIME + " text, "
            + COLUMN_OWNER + " integer default 0"
            + ");";

    public MyEventsSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        AndroidThreeTen.init(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_EVENTS);
        onCreate(db);
    }

    public void create(EventDTO event) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, event.getId());
        cv.put(COLUMN_NAME, event.getName());
        cv.put(COLUMN_DESCRIPTION, event.getDescription());
        cv.put(COLUMN_IMAGE_URI, event.getImageUri());
        cv.put(COLUMN_EVENT_TYPE, event.getType().toString());
        cv.put(COLUMN_PRIVACY, event.getPrivacy().toString());
        cv.put(COLUMN_START_TIME, event.getStart_time().toString());
        cv.put(COLUMN_END_TIME, event.getEnd_time().toString());
        cv.put(COLUMN_PLACE, event.getPlace());
        cv.put(COLUMN_LATITUDE, event.getLatitude());
        cv.put(COLUMN_LONGITUDE, event.getLongitude());
        cv.put(COLUMN_SYNC_STATUS, event.getSyncStatus().toString());
        cv.put(COLUMN_UPDATED_TIME, event.getUpdated_time().toString());
        cv.put(COLUMN_CREATED_TIME, event.getCreated_time().toString());
        cv.put(COLUMN_OWNER, event.getOwner());

        mDatabase = getWritableDatabase();
        mDatabase.insert(TABLE_MY_EVENTS, null, cv);
        mDatabase.close();
    }

    public ArrayList<EventDTO> read() {
        ArrayList<EventDTO> loggedUserEvents = new ArrayList<>();
        mDatabase = getReadableDatabase();
        Cursor cursor = mDatabase.query(TABLE_MY_EVENTS, null, null, null, null, null, COLUMN_ID + " DESC");
        while (cursor.moveToNext()) {
            EventDTO e = new EventDTO(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    EventType.valueOf(cursor.getString(4)),
                    FacebookPrivacy.valueOf(cursor.getString(5)),
                    ZonedDateTime.parse(cursor.getString(6)),
                    ZonedDateTime.parse(cursor.getString(7)),
                    cursor.getString(8),
                    cursor.getDouble(9),
                    cursor.getDouble(10),
                    SyncStatus.valueOf(cursor.getString(11)),
                    ZonedDateTime.parse(cursor.getString(12)),
                    ZonedDateTime.parse(cursor.getString(13)),
                    cursor.getLong(14)
            );
            loggedUserEvents.add(e);
        }
        cursor.close();
        mDatabase.close();

        return loggedUserEvents;
    }

    public EventDTO update(EventDTO event) {
        event.setUpdated_time(ZonedDateTime.now());
        event.setSyncStatus(SyncStatus.UPDATE);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, event.getId());
        cv.put(COLUMN_NAME, event.getName());
        cv.put(COLUMN_DESCRIPTION, event.getDescription());
        cv.put(COLUMN_IMAGE_URI, event.getImageUri());
        cv.put(COLUMN_EVENT_TYPE, event.getType().toString());
        cv.put(COLUMN_PRIVACY, event.getPrivacy().toString());
        cv.put(COLUMN_START_TIME, event.getStart_time().toString());
        cv.put(COLUMN_END_TIME, event.getEnd_time().toString());
        cv.put(COLUMN_PLACE, event.getPlace());
        cv.put(COLUMN_LATITUDE, event.getLatitude());
        cv.put(COLUMN_LONGITUDE, event.getLongitude());
        cv.put(COLUMN_SYNC_STATUS, event.getSyncStatus().toString());
        cv.put(COLUMN_UPDATED_TIME, event.getUpdated_time().toString());
        cv.put(COLUMN_CREATED_TIME, event.getCreated_time().toString());
        cv.put(COLUMN_OWNER, event.getOwner());

        mDatabase = getWritableDatabase();
        mDatabase.update(TABLE_MY_EVENTS, cv, null, null);
        mDatabase.close();

        return event;
    }

    public void deleteAll() {
        mDatabase = getWritableDatabase();
        mDatabase.delete(TABLE_MY_EVENTS, null, null);
        mDatabase.close();
    }

    public EventDTO deleteLogical(EventDTO event) {
        event.setSyncStatus(SyncStatus.DELETE);
        return update(event);
    }

    public boolean deletePhysical(Long id) {
        mDatabase = getWritableDatabase();
        boolean b = mDatabase.delete(TABLE_MY_EVENTS, COLUMN_ID + "=" + id, null) > 0;
        mDatabase.close();
        return b;
    }

    public boolean exists(Long id) {
        String Query = "Select * from " + TABLE_MY_EVENTS + " where " + COLUMN_ID + " = " + id;
        mDatabase = getReadableDatabase();
        Cursor cursor = mDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
