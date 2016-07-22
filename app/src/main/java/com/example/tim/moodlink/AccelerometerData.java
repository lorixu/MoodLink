package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class AccelerometerData {

    private int id;
    private TimeAndDay timeStamp;
    private double xAxisValue;
    private double yAxisValue;
    private double zAxisValue;


    public AccelerometerData(TimeAndDay time, double xAxisValue, double yAxisValue, double zAxisValue){
        this.timeStamp = time;
        this.xAxisValue = xAxisValue;
        this.yAxisValue = yAxisValue;
        this.zAxisValue = zAxisValue;
    }

    public AccelerometerData(int day, int month, int year, int hour, int minute, int second, double xAxisValue, double yAxisValue, double zAxisValue){
        this.timeStamp = new TimeAndDay(day, month, year, hour, minute, second);

        this.xAxisValue = xAxisValue;
        this.yAxisValue = yAxisValue;
        this.zAxisValue = zAxisValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TimeAndDay getTimeStamp() {
        return timeStamp;
    }

    public void setTime(TimeAndDay time) {
        this.timeStamp = time;
    }

    public void setTime(int day, int month, int year, int hour, int minute, int second) {
        this.timeStamp = new TimeAndDay(day, month, year, hour, minute, second);
    }

    public double getxAxisValue() {
        return xAxisValue;
    }

    public void setxAxisValue(double xAxisValue) {
        this.xAxisValue = xAxisValue;
    }

    public double getyAxisValue() {
        return yAxisValue;
    }

    public void setyAxisValue(double yAxisValue) {
        this.yAxisValue = yAxisValue;
    }

    public double getzAxisValue() {
        return zAxisValue;
    }

    public void setzAxisValue(double zAxisValue) {
        this.zAxisValue = zAxisValue;
    }

    @Override
    public String toString() {
        return "AccelerometerData{" +
                "id=" + id +
                ", timeStamp=" + timeStamp +
                ", xAxisValue=" + xAxisValue +
                ", yAxisValue=" + yAxisValue +
                ", zAxisValue=" + zAxisValue +
                '}';
    }
}
