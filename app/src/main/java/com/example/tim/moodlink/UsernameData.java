package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class UsernameData {

    private int id;
    private String username;


    public UsernameData(String username){
        this.username = username;
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

    @Override
    public String toString() {
        return "UsernameData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
