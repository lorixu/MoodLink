package com.example.tim.moodlink;

/**
 * Created by Tim on 26/07/2016.
 */
public abstract class SensorData {

    protected int day;
    protected int month;
    protected int year;
    protected int hour;
    protected int minute;
    protected int second;

    public SensorData(TimeAndDay time) {
        this.day = time.getDay();
        this.month = time.getMonth();
        this.year = time.getYear();
        this.hour = time.getHour();
        this.minute = time.getMinute();
        this.second = time.getSecond();
    }

    public SensorData(int day, int month, int year, int hour, int minute, int second) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public TimeAndDay getTimeStamp() {
        return new TimeAndDay(day, month, year, hour, minute, second);
    }

    public void setTime(TimeAndDay time) {
        this.day = time.getDay();
        this.month = time.getMonth();
        this.year = time.getYear();
        this.hour = time.getHour();
        this.minute = time.getMinute();
        this.second = time.getSecond();
    }

    public void setTime(int day, int month, int year, int hour, int minute, int second) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

}
