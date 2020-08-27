package com.example.kitchenmasterapp.repositories.RecipePhots;

import android.os.AsyncTask;

import com.example.kitchenmasterapp.ApplicationController;
import com.example.kitchenmasterapp.database.RecipePhoto;

public class RecipePhotoInsertTask extends AsyncTask<RecipePhoto, Void, Void> {
    OnRecipePhotoRepositoryActionListener listener;

    RecipePhotoInsertTask(OnRecipePhotoRepositoryActionListener listener) {this.listener = listener;}

    @Override
    protected Void doInBackground(RecipePhoto... photos) {
        ApplicationController.getAppDatabase().recipePhotoDAO().insertTask(photos[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.actionSucces();
    }

}
