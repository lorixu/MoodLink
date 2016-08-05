package com.example.tim.moodlink;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLLiteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Moodlink_DB";
    private static final int DATABASE_VERSION = 1;

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

    private static final String COL_PROCESSED = "processed";


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
            COL_CATEGORY + " TEXT NOT NULL," +
            COL_PHONE + " TEXT NOT NULL, " +
            COL_EMAIL + " TEXT NOT NULL );";

    private static final String CREATE_LIGHT_TABLE = "CREATE TABLE " + LIGHT_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL," +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_LUX_VALUE + " REEL NOT NULL, " +
            COL_PROCESSED + " INTEGER );";

    private static final String CREATE_CAMERA_TABLE = "CREATE TABLE " + CAMERA_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL," +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_PIC_PATH + " TEXT NOT NULL, " +
            COL_PROCESSED + " INTEGER );";

    private static final String CREATE_PHONE_TABLE = "CREATE TABLE " + PHONE_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL," +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_REC_PATH + " TEXT NOT NULL, " +
            COL_PROCESSED + " INTEGER );";

    private static final String CREATE_ACCELEROMETER_TABLE = "CREATE TABLE " + ACCELEROMETER_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL," +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_ACCELEROMETER_X + " REEL NOT NULL," +
            COL_ACCELEROMETER_Y + " REEL NOT NULL," +
            COL_ACCELEROMETER_Z + " REEL NOT NULL, " +
            COL_PROCESSED + " INTEGER );";

    private static final String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOCATION_TABLE + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DAY + " INTEGER NOT NULL, " +
            COL_MONTH + " INTEGER NOT NULL," +
            COL_YEAR + " INTEGER NOT NULL, " +
            COL_HOUR + " INTEGER NOT NULL, " +
            COL_MINUTES + " INTEGER NOT NULL, " +
            COL_SECONDS + " INTEGER NOT NULL," +
            COL_LOCATION_LAT + " REEL NOT NULL," +
            COL_LOCATION_LONG + " REEL NOT NULL, " +
            COL_PROCESSED + " INTEGER );";

    public SQLLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    // CRUD (Create, Read, Update and Delete) Operations

    public ContentValues setContentValueSensorData(ContentValues values, SensorData data) {
        values.put(COL_DAY, data.getDay());
        values.put(COL_MONTH, data.getMonth());
        values.put(COL_YEAR, data.getYear());
        values.put(COL_HOUR, data.getHour());
        values.put(COL_MINUTES, data.getMinute());
        values.put(COL_SECONDS, data.getSecond());


        if (data.isProcessed()) {
            values.put(COL_PROCESSED, 1);
        } else {
            values.put(COL_PROCESSED, 0);
        }
        
        return values;
    }

    // Username table

    public long addUsernameTuple(UsernameData username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username.getUsername());

        // insert row

        return db.insert(USERNAME_TABLE, null, values);
    }

    public UsernameData getUsernameData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + USERNAME_TABLE + " WHERE "
                + COL_ID + " = 1";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {

            UsernameData username = new UsernameData(c.getString(c.getColumnIndex(COL_USERNAME)));
            username.setId(c.getInt(c.getColumnIndex(COL_ID)));
            c.close();
            return username;
        } else return new UsernameData("");
    }

    public int updateUsername(UsernameData usernameData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, usernameData.getUsername());

        // updating row
        return db.update(USERNAME_TABLE, values, COL_ID + " = 1", null);
    }

    public void deleteUsername() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERNAME_TABLE, COL_ID + " = 1", null);
    }

    //Contacts table

    public long addContactTuple(ContactData contactData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, contactData.getName());
        values.put(COL_CATEGORY, contactData.getCategory());
        values.put(COL_PHONE, contactData.getPhone());
        values.put(COL_EMAIL, contactData.getEmail());

        // insert row

        return db.insert(CONTACTS_TABLE, null, values);
    }

    public ContactData getContactData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE + " WHERE "
                + COL_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            ContactData contactData = new ContactData(
                    c.getString(c.getColumnIndex(COL_NAME)),
                    c.getString(c.getColumnIndex(COL_CATEGORY)),
                    c.getString(c.getColumnIndex(COL_PHONE)),
                    c.getString(c.getColumnIndex(COL_EMAIL)));
            contactData.setId(c.getInt(c.getColumnIndex(COL_ID)));
            return contactData;
        } else return null;
    }

    public List<ContactData> getAllContactDatas() {
        List<ContactData> contactDatas = new ArrayList<ContactData>();
        String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ContactData contactData = new ContactData(
                        c.getString(c.getColumnIndex(COL_NAME)),
                        c.getString(c.getColumnIndex(COL_CATEGORY)),
                        c.getString(c.getColumnIndex(COL_PHONE)),
                        c.getString(c.getColumnIndex(COL_EMAIL)));
                contactData.setId(c.getInt(c.getColumnIndex(COL_ID)));


                contactDatas.add(contactData);
            } while (c.moveToNext());
        }

        return contactDatas;
    }

    public int updateContactData(long idToUpdate, ContactData contactData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, contactData.getName());
        values.put(COL_CATEGORY, contactData.getCategory());
        values.put(COL_PHONE, contactData.getPhone());
        values.put(COL_EMAIL, contactData.getEmail());

        // updating row
        return db.update(CONTACTS_TABLE, values, COL_ID + " = " + idToUpdate, null);
    }


    public void deleteContact(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Light Data table

    public long addLightTuple(LightData lightData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_LUX_VALUE, lightData.getLuxValue());

        values = setContentValueSensorData(values, lightData);

        // insert row
        long light_data_id = db.insert(LIGHT_TABLE, null, values);

        return light_data_id;
    }

    public LightData getLightData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + LIGHT_TABLE + " WHERE "
                + COL_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        LightData lightData = new LightData(c.getInt(
                c.getColumnIndex(COL_LUX_VALUE)),
                c.getInt(c.getColumnIndex(COL_DAY)),
                c.getInt(c.getColumnIndex(COL_MONTH)),
                c.getInt(c.getColumnIndex(COL_YEAR)),
                c.getInt(c.getColumnIndex(COL_HOUR)),
                c.getInt(c.getColumnIndex(COL_MINUTES)),
                c.getInt(c.getColumnIndex(COL_SECONDS)));
        lightData.setId(c.getInt(c.getColumnIndex(COL_ID)));

        return lightData;
    }

    public List<LightData> getAllLightDatas() {
        List<LightData> lightDatas = new ArrayList<LightData>();
        String selectQuery = "SELECT  * FROM " + LIGHT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                LightData lightData = new LightData(c.getInt(
                        c.getColumnIndex(COL_LUX_VALUE)),
                        c.getInt(c.getColumnIndex(COL_DAY)),
                        c.getInt(c.getColumnIndex(COL_MONTH)),
                        c.getInt(c.getColumnIndex(COL_YEAR)),
                        c.getInt(c.getColumnIndex(COL_HOUR)),
                        c.getInt(c.getColumnIndex(COL_MINUTES)),
                        c.getInt(c.getColumnIndex(COL_SECONDS)));
                lightData.setId(c.getInt(c.getColumnIndex(COL_ID)));


                lightDatas.add(lightData);
            } while (c.moveToNext());
        }

        return lightDatas;
    }

    public int updateLightData(long idToUpdate, LightData lightData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_LUX_VALUE, lightData.getLuxValue());

        if (lightData.isProcessed()) {
            values.put(COL_PROCESSED, 1);
        } else {
            values.put(COL_PROCESSED, 0);
        }

        // updating row
        return db.update(LIGHT_TABLE, values, COL_ID + " = " + idToUpdate, null);
    }

    public void deleteLightData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LIGHT_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteAllLightData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LIGHT_TABLE, null, null);
    }

    // Accelerometer Data table

    public long addAccelerometerTuple(AccelerometerData accelerometerData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ACCELEROMETER_X, accelerometerData.getxAxisValue());
        values.put(COL_ACCELEROMETER_Y, accelerometerData.getyAxisValue());
        values.put(COL_ACCELEROMETER_Z, accelerometerData.getzAxisValue());


        values = setContentValueSensorData(values, accelerometerData);

        // insert row
        long accelerometer_data_id = db.insert(ACCELEROMETER_TABLE, null, values);

        return accelerometer_data_id;
    }

    public AccelerometerData getAccelerometerData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ACCELEROMETER_TABLE + " WHERE "
                + COL_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        AccelerometerData accelerometerData = new AccelerometerData(
                c.getInt(c.getColumnIndex(COL_ACCELEROMETER_X)),
                c.getInt(c.getColumnIndex(COL_ACCELEROMETER_Y)),
                c.getInt(c.getColumnIndex(COL_ACCELEROMETER_Z)),
                c.getInt(c.getColumnIndex(COL_DAY)),
                c.getInt(c.getColumnIndex(COL_MONTH)),
                c.getInt(c.getColumnIndex(COL_YEAR)),
                c.getInt(c.getColumnIndex(COL_HOUR)),
                c.getInt(c.getColumnIndex(COL_MINUTES)),
                c.getInt(c.getColumnIndex(COL_SECONDS)));
        accelerometerData.setId(c.getInt(c.getColumnIndex(COL_ID)));

        return accelerometerData;
    }

    public List<AccelerometerData> getAllAccelerometerDatas() {
        List<AccelerometerData> accelerometerDatas = new ArrayList<AccelerometerData>();
        String selectQuery = "SELECT  * FROM " + ACCELEROMETER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                AccelerometerData accelerometerData = new AccelerometerData(
                        c.getInt(c.getColumnIndex(COL_ACCELEROMETER_X)),
                        c.getInt(c.getColumnIndex(COL_ACCELEROMETER_Y)),
                        c.getInt(c.getColumnIndex(COL_ACCELEROMETER_Z)),
                        c.getInt(c.getColumnIndex(COL_DAY)),
                        c.getInt(c.getColumnIndex(COL_MONTH)),
                        c.getInt(c.getColumnIndex(COL_YEAR)),
                        c.getInt(c.getColumnIndex(COL_HOUR)),
                        c.getInt(c.getColumnIndex(COL_MINUTES)),
                        c.getInt(c.getColumnIndex(COL_SECONDS)));
                accelerometerData.setId(c.getInt(c.getColumnIndex(COL_ID)));


                accelerometerDatas.add(accelerometerData);
            } while (c.moveToNext());
        }

        return accelerometerDatas;
    }

    public int updateAccelerometerData(long idToUpdate, AccelerometerData accelerometerData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ACCELEROMETER_X, accelerometerData.getxAxisValue());
        values.put(COL_ACCELEROMETER_Y, accelerometerData.getyAxisValue());
        values.put(COL_ACCELEROMETER_Z, accelerometerData.getzAxisValue());


        if (accelerometerData.isProcessed()) {
            values.put(COL_PROCESSED, 1);
        } else {
            values.put(COL_PROCESSED, 0);
        }

        // updating row
        return db.update(ACCELEROMETER_TABLE, values, COL_ID + " = " + idToUpdate, null);
    }

    public void deleteAccelerometerData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCELEROMETER_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteAllAccelerometerData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCELEROMETER_TABLE, null, null);
    }

    // Location Data table

    public long addLocationTuple(LocationData locationData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_LOCATION_LAT, locationData.getLatitude());
        values.put(COL_LOCATION_LONG, locationData.getLongitude());


        values = setContentValueSensorData(values, locationData);

        // insert row
        long location_data_id = db.insert(LOCATION_TABLE, null, values);

        return location_data_id;
    }

    public LocationData getLocationData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + LOCATION_TABLE + " WHERE "
                + COL_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        LocationData locationData = new LocationData(
                c.getInt(c.getColumnIndex(COL_LOCATION_LAT)),
                c.getInt(c.getColumnIndex(COL_LOCATION_LONG)),
                c.getInt(c.getColumnIndex(COL_DAY)),
                c.getInt(c.getColumnIndex(COL_MONTH)),
                c.getInt(c.getColumnIndex(COL_YEAR)),
                c.getInt(c.getColumnIndex(COL_HOUR)),
                c.getInt(c.getColumnIndex(COL_MINUTES)),
                c.getInt(c.getColumnIndex(COL_SECONDS)));
        locationData.setId(c.getInt(c.getColumnIndex(COL_ID)));

        return locationData;
    }

    public List<LocationData> getAllLocationDatas() {
        List<LocationData> locationDatas = new ArrayList<LocationData>();
        String selectQuery = "SELECT  * FROM " + LOCATION_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                LocationData locationData = new LocationData(
                        c.getInt(c.getColumnIndex(COL_LOCATION_LAT)),
                        c.getInt(c.getColumnIndex(COL_LOCATION_LONG)),
                        c.getInt(c.getColumnIndex(COL_DAY)),
                        c.getInt(c.getColumnIndex(COL_MONTH)),
                        c.getInt(c.getColumnIndex(COL_YEAR)),
                        c.getInt(c.getColumnIndex(COL_HOUR)),
                        c.getInt(c.getColumnIndex(COL_MINUTES)),
                        c.getInt(c.getColumnIndex(COL_SECONDS)));
                locationData.setId(c.getInt(c.getColumnIndex(COL_ID)));


                locationDatas.add(locationData);
            } while (c.moveToNext());
        }

        return locationDatas;
    }

    public int updateLocationData(long idToUpdate, LocationData locationData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_LOCATION_LAT, locationData.getLatitude());
        values.put(COL_LOCATION_LONG, locationData.getLongitude());


        if (locationData.isProcessed()) {
            values.put(COL_PROCESSED, 1);
        } else {
            values.put(COL_PROCESSED, 0);
        }

        // updating row
        return db.update(LOCATION_TABLE, values, COL_ID + " = " + idToUpdate, null);
    }

    public void deleteLocationData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOCATION_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteAllLocationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOCATION_TABLE, null, null);
    }

    // Camera Data Table

    public long addCameraTuple(CameraData cameraData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PIC_PATH, cameraData.getPath());
        values = setContentValueSensorData(values, cameraData);

        // insert row
        long camera_data_id = db.insert(CAMERA_TABLE, null, values);

        return camera_data_id;
    }

    public CameraData getCameraData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CAMERA_TABLE + " WHERE "
                + COL_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        CameraData cameraData = new CameraData(
                c.getString(c.getColumnIndex(COL_PIC_PATH)),
                c.getInt(c.getColumnIndex(COL_DAY)),
                c.getInt(c.getColumnIndex(COL_MONTH)),
                c.getInt(c.getColumnIndex(COL_YEAR)),
                c.getInt(c.getColumnIndex(COL_HOUR)),
                c.getInt(c.getColumnIndex(COL_MINUTES)),
                c.getInt(c.getColumnIndex(COL_SECONDS)));
        cameraData.setId(c.getInt(c.getColumnIndex(COL_ID)));

        return cameraData;
    }

    public List<CameraData> getAllCameraDatas() {
        List<CameraData> cameraDatas = new ArrayList<CameraData>();
        String selectQuery = "SELECT  * FROM " + CAMERA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                CameraData cameraData = new CameraData(
                        c.getString(c.getColumnIndex(COL_PIC_PATH)),
                        c.getInt(c.getColumnIndex(COL_DAY)),
                        c.getInt(c.getColumnIndex(COL_MONTH)),
                        c.getInt(c.getColumnIndex(COL_YEAR)),
                        c.getInt(c.getColumnIndex(COL_HOUR)),
                        c.getInt(c.getColumnIndex(COL_MINUTES)),
                        c.getInt(c.getColumnIndex(COL_SECONDS)));
                cameraData.setId(c.getInt(c.getColumnIndex(COL_ID)));


                cameraDatas.add(cameraData);
            } while (c.moveToNext());
        }

        return cameraDatas;
    }

    public int updateCameraData(long idToUpdate, CameraData cameraData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PIC_PATH, cameraData.getPath());


        if (cameraData.isProcessed()) {
            values.put(COL_PROCESSED, 1);
        } else {
            values.put(COL_PROCESSED, 0);
        }

        // updating row
        return db.update(CAMERA_TABLE, values, COL_ID + " = " + idToUpdate, null);
    }

    public void deleteCameraData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CAMERA_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteAllCameraData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CAMERA_TABLE, null, null);
    }

    // Phone Data Table

    public long addPhoneTuple(PhoneData phoneData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_REC_PATH, phoneData.getPath());
        values = setContentValueSensorData(values, phoneData);

        // insert row
        long phone_data_id = db.insert(PHONE_TABLE, null, values);

        return phone_data_id;
    }

    public PhoneData getPhoneData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + PHONE_TABLE + " WHERE "
                + COL_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        PhoneData phoneData = new PhoneData(
                c.getString(c.getColumnIndex(COL_REC_PATH)),
                c.getInt(c.getColumnIndex(COL_DAY)),
                c.getInt(c.getColumnIndex(COL_MONTH)),
                c.getInt(c.getColumnIndex(COL_YEAR)),
                c.getInt(c.getColumnIndex(COL_HOUR)),
                c.getInt(c.getColumnIndex(COL_MINUTES)),
                c.getInt(c.getColumnIndex(COL_SECONDS)));
        phoneData.setId(c.getInt(c.getColumnIndex(COL_ID)));

        return phoneData;
    }

    public List<PhoneData> getAllPhoneDatas() {
        List<PhoneData> phoneDatas = new ArrayList<PhoneData>();
        String selectQuery = "SELECT  * FROM " + PHONE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PhoneData phoneData = new PhoneData(
                        c.getString(c.getColumnIndex(COL_REC_PATH)),
                        c.getInt(c.getColumnIndex(COL_DAY)),
                        c.getInt(c.getColumnIndex(COL_MONTH)),
                        c.getInt(c.getColumnIndex(COL_YEAR)),
                        c.getInt(c.getColumnIndex(COL_HOUR)),
                        c.getInt(c.getColumnIndex(COL_MINUTES)),
                        c.getInt(c.getColumnIndex(COL_SECONDS)));
                phoneData.setId(c.getInt(c.getColumnIndex(COL_ID)));


                phoneDatas.add(phoneData);
            } while (c.moveToNext());
        }

        return phoneDatas;
    }

    public int updatePhoneData(long idToUpdate, PhoneData phoneData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_REC_PATH, phoneData.getPath());


        if (phoneData.isProcessed()) {
            values.put(COL_PROCESSED, 1);
        } else {
            values.put(COL_PROCESSED, 0);
        }

        // updating row
        return db.update(PHONE_TABLE, values, COL_ID + " = " + idToUpdate, null);
    }

    public void deletePhoneData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHONE_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteAllPhoneData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHONE_TABLE, null, null);
    }
}