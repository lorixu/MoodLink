package com.example.tim.moodlink;

import java.util.Calendar;

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

    public TimeAndDay(Calendar c){
        this.day = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH)+1; // January is 0
        this.year = c.get(Calendar.YEAR);
        this.hour = c.get(Calendar.HOUR_OF_DAY);
        this.minute = c.get(Calendar.MINUTE);
        this.second = c.get(Calendar.SECOND);
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
