package com.example.tim.moodlink;


public class CameraData extends SensorData{

    private int id;
    private String path;


    public CameraData( String path, TimeAndDay time){
        super(time);
        this.path = path;
    }

    public CameraData( String path, int day, int month, int year, int hour, int minute, int second){
        super(day, month, year, hour, minute, second);
        this.path = path;
    }

    public CameraData( String path, TimeAndDay time, Boolean processed){
        super(time,processed);
        this.path = path;
    }

    public CameraData( String path, int day, int month, int year, int hour, int minute, int second, Boolean processed){
        super(day, month, year, hour, minute, second,processed);
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

    @Override
    public String toString() {
        return "\nCameraData{" +
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
