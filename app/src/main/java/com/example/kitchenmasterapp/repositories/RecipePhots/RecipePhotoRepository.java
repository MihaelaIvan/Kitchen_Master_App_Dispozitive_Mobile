package com.example.kitchenmasterapp.repositories.RecipePhots;

import android.content.Context;

import com.example.kitchenmasterapp.ApplicationController;
import com.example.kitchenmasterapp.database.MyAppDatabase;
import com.example.kitchenmasterapp.database.RecipePhoto;

public class RecipePhotoRepository {
    private MyAppDatabase appDatabase;

    public RecipePhotoRepository(Context context){
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertTask(final RecipePhoto recipePhoto,
                           final OnRecipePhotoRepositoryActionListener listener){
        new RecipePhotoInsertTask(listener).execute(recipePhoto);
    }

    public String getImage(int recipe_id) { return appDatabase.recipePhotoDAO().getImage(recipe_id); }

    public void updateTask(final RecipePhoto recipePhoto, final OnRecipePhotoRepositoryActionListener listener) {new RecipePhotoUpdateTask(listener).execute(recipePhoto);};
}
