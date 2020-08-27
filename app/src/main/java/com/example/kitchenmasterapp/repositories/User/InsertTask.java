package com.example.kitchenmasterapp.repositories.User;

import android.os.AsyncTask;

import com.example.kitchenmasterapp.ApplicationController;
import com.example.kitchenmasterapp.database.User;
import com.example.kitchenmasterapp.repositories.User.OnUserRepositoryActionListener;

public class InsertTask extends AsyncTask<User, Void, Void> {

    OnUserRepositoryActionListener listener;

    InsertTask(OnUserRepositoryActionListener listener) {

        this.listener = listener;

    }

    @Override

    protected Void doInBackground(User... users) {

        ApplicationController.getAppDatabase().userDAO().insertTask(users[0]);

        return null;

    }

    @Override

    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);

        listener.actionSucces();

    }

}