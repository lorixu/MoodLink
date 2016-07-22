package com.example.tim.moodlink;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


        // TODO : CRUD (Create, Read, Update and Delete) Operations

public class SQLLiteDBHelper extends SQLiteOpenHelper {

    private static final String USERNAME_TABLE = "username_table";
    private static final String CONTACTS_TABLE = "contacts_table";
    private static final String LIGHT_TABLE = "light_data_table";
    private static final String CAMERA_TABLE = "camera_data_table";
    private static final String PHONE_TABLE = "phone_data_table";
    private static final String LOCATION_TABLE = "location_data_table";
    private static final String ACCELEROMETER_TABLE = "accelerometer_data_table";

    // Common columns
    private static final String COL_ID = "id";

    // Username Table only columns
    private static final String COL_USERNAME = "username";

    // Contacts Table only columns
    private static final String COL_NAME = "name";
    private static final String COL_CATEGORY = "category";
    private static final String COL_PHONE = "phone";
    private static final String COL_EMAIL = "email";

    // Data Tables only columns
    private static final String COL_DAY = "day";
    private static final String COL_MONTH = "month";
    private static final String COL_YEAR = "year";
    private static final String COL_HOUR = "hour";
    private static final String COL_MINUTES = "minutes";
    private static final String COL_SECONDS = "seconds";

    // Light Data Table only columns
    private static final String COL_LUX_VALUE = "lux_value";

    // Light Data Table only columns
    private static final String COL_PIC_PATH = "picture_path";

    // Phone Data Table only columns
    private static final String COL_REC_PATH = "recording_path";

    // Accelerometer Data Table only columns
    private static final String COL_ACCELEROMETER_X = "x_axis_value";
    private static final String COL_ACCELEROMETER_Y = "y_axis_value";
    private static final String COL_ACCELEROMETER_Z = "z_axis_value";

    // Location Data Table only columns
    private static final String COL_LOCATION_LONG = "longitude_value";
    private static final String COL_LOCATION_LAT = "latitude_value";


    // Tables' CREATE statements
    private static final String CREATE_USERNAME_TABLE = "CREATE TABLE " + USERNAME_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USERNAME + " TEXT NOT NULL );";

    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT NOT NULL, " +
            COL_CATEGORY + " TEXT NOT NULL" +
            COL_PHONE + " TEXT NOT NULL, " +
            COL_EMAIL + " TEXT NOT NULL );";

    private static final String CREATE_LIGHT_TABLE = "CREATE TABLE " + LIGHT_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL" +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_LUX_VALUE + " REEL NOT NULL );";

    private static final String CREATE_CAMERA_TABLE = "CREATE TABLE " + CAMERA_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL" +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_PIC_PATH + " TEXT NOT NULL );";

    private static final String CREATE_PHONE_TABLE = "CREATE TABLE " + PHONE_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL" +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_REC_PATH + " TEXT NOT NULL );";

    private static final String CREATE_ACCELEROMETER_TABLE = "CREATE TABLE " + ACCELEROMETER_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL" +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_ACCELEROMETER_X + " REEL NOT NULL," +
            COL_ACCELEROMETER_Y + " REEL NOT NULL," +
            COL_ACCELEROMETER_Z + " REEL NOT NULL);";

    private static final String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOCATION_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL" +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_LOCATION_LAT + " REEL NOT NULL," +
            COL_LOCATION_LONG + " REEL NOT NULL);";

    public SQLLiteDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERNAME_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_LIGHT_TABLE);
        db.execSQL(CREATE_ACCELEROMETER_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_CAMERA_TABLE);
        db.execSQL(CREATE_PHONE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + USERNAME_TABLE + ";");
        db.execSQL("DROP TABLE " + CONTACTS_TABLE + ";");
        db.execSQL("DROP TABLE " + LIGHT_TABLE + ";");
        db.execSQL("DROP TABLE " + ACCELEROMETER_TABLE + ";");
        db.execSQL("DROP TABLE " + LOCATION_TABLE + ";");
        db.execSQL("DROP TABLE " + CAMERA_TABLE + ";");
        db.execSQL("DROP TABLE " + PHONE_TABLE + ";");

        onCreate(db);
    }

}
