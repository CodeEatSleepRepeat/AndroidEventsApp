package rs.ac.uns.ftn.eventsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_USER = "user";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FB_ID = "facebookId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE_URI = "imageUri";
    public static final String COLUMN_IMAGE_HEIGHT = "imageHeight";
    public static final String COLUMN_IMAGE_WIDTH = "imageWidth";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ACTIVATED = "activatedAccount";
    public static final String COLUMN_SYNC_FB_EVENTS = "syncFacebookEvents";
    public static final String COLUMN_SYNC_FB_PROFILE = "syncFacebookProfile";

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_USER + "("
            + COLUMN_ID  + " integer primary key , "
            + COLUMN_FB_ID + " text, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_IMAGE_URI + " text, "
            + COLUMN_IMAGE_HEIGHT + " integer, "
            + COLUMN_IMAGE_WIDTH + " integer, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_PASSWORD + " text not null, "
            + COLUMN_ACTIVATED + " integer default 0 ,"
            + COLUMN_SYNC_FB_EVENTS + " integer default 0 ,"
            + COLUMN_SYNC_FB_PROFILE + " integer default 0"
            + ");";



    public UserSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
}
