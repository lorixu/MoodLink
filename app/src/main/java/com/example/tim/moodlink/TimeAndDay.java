package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class TimeAndDay {

    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private int second;

    public TimeAndDay(int day, int month, int year, int hour, int minute, int second){
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

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
