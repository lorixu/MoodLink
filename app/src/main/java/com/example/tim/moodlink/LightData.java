package com.example.tim.moodlink;

public class LightData extends SensorData{

    private int id;
    private int luxValue;


    public LightData( int luxValue, TimeAndDay time){
        super(time);
        this.luxValue = luxValue;
    }

    public LightData(int luxValue, int day, int month, int year, int hour, int minute, int second){
        super(day, month, year, hour, minute, second);
        this.luxValue = luxValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLuxValue() {
        return luxValue;
    }

    @Override
    public String toString() {
        return "\nLightData{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                ", luxValue=" + luxValue +
                "}";
    }
}
