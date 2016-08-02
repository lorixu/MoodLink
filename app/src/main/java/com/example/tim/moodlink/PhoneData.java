package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class PhoneData extends SensorData{

    private int id;
    private String path;


    public PhoneData(String path, TimeAndDay time){
        super(time);
        this.path = path;
    }

    public PhoneData(String path, int day, int month, int year, int hour, int minute, int second){
        super(day, month, year, hour, minute, second);
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "\nPhoneData{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                ", path='" + path + '\'' +
                '}';
    }
}
