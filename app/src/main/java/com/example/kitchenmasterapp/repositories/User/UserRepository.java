package com.example.kitchenmasterapp.repositories.User;

import android.content.Context;

import com.example.kitchenmasterapp.ApplicationController;
import com.example.kitchenmasterapp.database.MyAppDatabase;
import com.example.kitchenmasterapp.database.User;
import com.example.kitchenmasterapp.repositories.User.InsertTask;
import com.example.kitchenmasterapp.repositories.User.OnUserRepositoryActionListener;

public class UserRepository {

    private MyAppDatabase appDatabase;

    public UserRepository(Context context){
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertTask(final User user, final OnUserRepositoryActionListener listener)   {

        new InsertTask(listener).execute(user);

    }

    public User getUser(String email, String password) {
        return appDatabase.userDAO().getUser(email, password);
    }

    public  User getUserByEmailString(String email)
    {
        return  appDatabase.userDAO().getUserByEmail(email);
    }

    public String getUserPassword(String email)
    {
        return appDatabase.userDAO().getPasswordByEmail(email);
    }

    public String getUserNameById(int userId)
    {
        return appDatabase.userDAO().getUserNameById(userId);
    }
}
