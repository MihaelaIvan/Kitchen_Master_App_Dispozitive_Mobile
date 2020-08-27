package com.example.kitchenmasterapp.repositories.RecipePhots;

import android.os.AsyncTask;

import com.example.kitchenmasterapp.ApplicationController;
import com.example.kitchenmasterapp.database.RecipePhoto;

public class RecipePhotoUpdateTask extends AsyncTask<RecipePhoto, Void, Void> {
    OnRecipePhotoRepositoryActionListener listener;

    RecipePhotoUpdateTask(OnRecipePhotoRepositoryActionListener listener){
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(RecipePhoto... photos) {
        ApplicationController.getAppDatabase().recipePhotoDAO().updateRecipePhotoImage(
                photos[0].getRecipe_id(), photos[0].getImage());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.actionSucces();
    }
}
