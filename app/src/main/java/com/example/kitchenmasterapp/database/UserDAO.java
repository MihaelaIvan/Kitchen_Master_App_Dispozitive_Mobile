package com.example.kitchenmasterapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.kitchenmasterapp.database.User;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM Users WHERE user_email =:email and password = :password")
    User getUser(String email, String password);

    @Insert
    void insertAll(User... users);

    @Insert
    void insertTask(User user);

    @Query("SELECT * FROM Users WHERE user_email =:email")
    User getUserByEmail(String email);

    @Query("SELECT password FROM Users WHERE user_email =:email")
    String getPasswordByEmail(String email);

    @Query("SELECT user_name FROM Users WHERE user_id =:userId")
    String getUserNameById(int userId);

}
