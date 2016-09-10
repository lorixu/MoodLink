package com.example.tim.moodlink;

/**
 * Created by Tim on 22/08/2016.
 */
public class MoodData {


    private int id;
    protected int day;
    protected int month;
    protected int year;
    private int moodValue;
    private boolean finalValue;


    public MoodData( int moodValue, TimeAndDay time){
        this.day = time.getDay();
        this.month = time.getMonth();
        this.year = time.getYear();
        this.moodValue = moodValue;
        this.finalValue = false;
    }

    public MoodData(int moodValue, int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
        this.moodValue = moodValue;
        this.finalValue = false;
    }

    public MoodData( int moodValue, TimeAndDay time, boolean finalValue){
        this.day = time.getDay();
        this.month = time.getMonth();
        this.year = time.getYear();
        this.moodValue = moodValue;
        this.finalValue = finalValue;
    }

    public MoodData(int moodValue, int day, int month, int year, boolean finalValue){
        this.day = day;
        this.month = month;
        this.year = year;
        this.moodValue = moodValue;
        this.finalValue = finalValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoodValue() {
        return moodValue;
    }

    public boolean isFinalValue() {
        return finalValue;
    }

    public void setFinalValue(boolean finalValue) {
        this.finalValue = finalValue;
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

    @Override
    public String toString() {
        return "\nMoodData{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", moodValue=" + moodValue +
                "}";
    }
}
