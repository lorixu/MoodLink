package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class LocationData extends SensorData {

    private int id;
    private double latitude;
    private double longitude;


    public LocationData(double latitude, double longitude, TimeAndDay time){
        super(time);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationData(double latitude, double longitude,int day, int month, int year, int hour, int minute, int second){
        super(day, month, year, hour, minute, second);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "\nLocationData{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
