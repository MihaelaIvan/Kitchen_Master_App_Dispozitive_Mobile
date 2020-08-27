package com.example.kitchenmasterapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Users")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int id;

    @ColumnInfo(name = "user_name")
    private  String userName;

    @ColumnInfo(name = "user_email")
    private String email;

    public String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String userName, String password, String email){
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

}
