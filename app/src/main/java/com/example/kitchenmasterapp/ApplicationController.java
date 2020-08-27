package com.example.kitchenmasterapp;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.kitchenmasterapp.database.MyAppDatabase;
import com.example.kitchenmasterapp.utils.Constants;

public class ApplicationController extends Application {

    private static ApplicationController mInstance;

    private static MyAppDatabase mAppDatabase;

    public static ApplicationController getInstance(){
        return  mInstance;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mInstance  = this;

        mAppDatabase = Room.databaseBuilder(getApplicationContext(),
                MyAppDatabase.class, Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public static MyAppDatabase getAppDatabase(){
        return mAppDatabase;
    }
}
