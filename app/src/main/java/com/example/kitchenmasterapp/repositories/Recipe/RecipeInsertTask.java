package com.example.kitchenmasterapp.repositories.Recipe;

import android.os.AsyncTask;

import com.example.kitchenmasterapp.ApplicationController;
import com.example.kitchenmasterapp.database.Recipe;

public class RecipeInsertTask extends AsyncTask<Recipe,Void,Void> {
    OnRecipeRepositoryActionListener listener;

    RecipeInsertTask(OnRecipeRepositoryActionListener listener){
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Recipe... recipes) {
        ApplicationController.getAppDatabase().recipeDAO().insertTask(recipes[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.actionSucces();
    }
}
