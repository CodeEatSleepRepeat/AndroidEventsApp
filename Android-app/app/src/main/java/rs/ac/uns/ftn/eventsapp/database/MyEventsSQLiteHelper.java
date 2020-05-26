package rs.ac.uns.ftn.eventsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.SyncStatus;

public class MyEventsSQLiteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDatabase;

    private static final String TABLE_MY_EVENTS = "my_events";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URI = "imageUri";
    private static final String COLUMN_EVENT_TYPE = "eventType";
    private static final String COLUMN_OPEN_FOR_ALL = "openForAll";
    private static final String COLUMN_START_TIME = "startTime";
    private static final String COLUMN_END_TIME = "endTime";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_SYNC_STATUS = "syncStatus";
    private static final String COLUMN_UPDATED_TIME = "updated_time";

    private static final String DATABASE_NAME = "my_events.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_MY_EVENTS + "("
            + COLUMN_ID + " integer primary key , "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_IMAGE_URI + " text, "
            + COLUMN_EVENT_TYPE + " text, "
            + COLUMN_OPEN_FOR_ALL + " integer default 0, "
            + COLUMN_START_TIME + " DATETIME not null, "
            + COLUMN_END_TIME + " DATETIME not null, "
            + COLUMN_LOCATION + " text,"
            + COLUMN_LATITUDE + " integer default 0, "
            + COLUMN_LONGITUDE + " integer default 0, "
            + COLUMN_SYNC_STATUS + " text, "
            + COLUMN_UPDATED_TIME + " DATETIME default CURRENT_TIMESTAMP"
            + ");";

    public MyEventsSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public void create(Event event) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, event.getEventId());
        cv.put(COLUMN_NAME, event.getEventName());
        cv.put(COLUMN_DESCRIPTION, event.getEventDescription());
        cv.put(COLUMN_IMAGE_URI, event.getEventImageURI());
        cv.put(COLUMN_EVENT_TYPE, event.getEventType().toString());
        cv.put(COLUMN_OPEN_FOR_ALL, event.getOpenForAll());
        cv.put(COLUMN_START_TIME, event.getStartTime().getTime());
        cv.put(COLUMN_END_TIME, event.getEndTime().getTime());
        cv.put(COLUMN_LOCATION, event.getLocation());
        cv.put(COLUMN_LATITUDE, event.getLatitude());
        cv.put(COLUMN_LONGITUDE, event.getLongitude());
        cv.put(COLUMN_SYNC_STATUS, event.getSyncStatus().toString());
        cv.put(COLUMN_UPDATED_TIME, event.getUpdated_time().getTime());

        mDatabase = getWritableDatabase();
        mDatabase.insert(TABLE_MY_EVENTS, null, cv);
        mDatabase.close();
    }

    public ArrayList<Event> read() {
        ArrayList<Event> loggedUserEvents = new ArrayList<>();
        mDatabase = getWritableDatabase();
        Cursor cursor = mDatabase.query(TABLE_MY_EVENTS, null, null, null, null, null, COLUMN_ID + " DESC");
        while (cursor.moveToNext()) {
            loggedUserEvents.add(new Event(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    EventType.valueOf(cursor.getString(4)),
                    cursor.getInt(5) == 1,
                    new Timestamp(cursor.getLong(6) * 1000),
                    new Timestamp(cursor.getLong(7) * 1000),
                    cursor.getString(8),
                    cursor.getLong(9),
                    cursor.getLong(10),
                    null, null, null, null,
                    SyncStatus.valueOf(cursor.getString(11)),
                    new Timestamp(cursor.getLong(12) * 1000)
            ));
        }
        cursor.close();
        mDatabase.close();

        return loggedUserEvents;
    }

    public Event update(Event event) {
        event.setUpdated_time(new Timestamp(System.currentTimeMillis()));
        event.setSyncStatus(SyncStatus.UPDATE);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, event.getEventId());
        cv.put(COLUMN_NAME, event.getEventName());
        cv.put(COLUMN_DESCRIPTION, event.getEventDescription());
        cv.put(COLUMN_IMAGE_URI, event.getEventImageURI());
        cv.put(COLUMN_EVENT_TYPE, event.getEventType().toString());
        cv.put(COLUMN_OPEN_FOR_ALL, event.getOpenForAll());
        cv.put(COLUMN_START_TIME, event.getStartTime().getTime());
        cv.put(COLUMN_END_TIME, event.getEndTime().getTime());
        cv.put(COLUMN_LOCATION, event.getLocation());
        cv.put(COLUMN_LATITUDE, event.getLatitude());
        cv.put(COLUMN_LONGITUDE, event.getLongitude());
        cv.put(COLUMN_SYNC_STATUS, event.getSyncStatus().toString());
        cv.put(COLUMN_UPDATED_TIME, event.getUpdated_time().getTime());

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

    public Event deleteLogical(Event event) {
        event.setSyncStatus(SyncStatus.DELETE);
        return update(event);
    }

    public boolean deletePhysical(Long id) {
        mDatabase = getWritableDatabase();
        boolean b = mDatabase.delete(TABLE_MY_EVENTS, COLUMN_ID + "=" + id, null) > 0;
        mDatabase.close();
        return b;
    }


}
