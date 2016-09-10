package com.example.tim.moodlink;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Tim on 22/07/2016.
 */
public class UserData {

    private int id;
    private String username;
    private LatLng home;
    private LatLng work;


    public UserData(String username, LatLng home, LatLng work) {
        this.username = username;
        this.home = home;
        this.work = work;
    }

    public UserData(String username, LatLng home) {
        this.username = username;
        this.home = home;
        this.work = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LatLng getHome() {
        return home;
    }

    public void setHome(LatLng home) {
        this.home = home;
    }

    public LatLng getWork() {
        return work;
    }

    public void setWork(LatLng work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
