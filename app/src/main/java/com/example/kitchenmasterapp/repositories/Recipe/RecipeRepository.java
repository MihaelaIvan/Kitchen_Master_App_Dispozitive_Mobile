package com.example.kitchenmasterapp.repositories.Recipe;

import android.content.Context;

import com.example.kitchenmasterapp.ApplicationController;
import com.example.kitchenmasterapp.database.MyAppDatabase;
import com.example.kitchenmasterapp.database.Recipe;

import java.util.List;

public class RecipeRepository {

    private MyAppDatabase appDatabase;

    public RecipeRepository(Context context){
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertTask(final Recipe recipe,
                           final OnRecipeRepositoryActionListener listener){
        new RecipeInsertTask(listener).execute(recipe);
    }

    public List<Recipe> getRecipes() {
        return appDatabase.recipeDAO().getAll();
    }

    public List<Recipe> getRecipesByUserId(int userId) {
        return appDatabase.recipeDAO().getAllRecipesByUserId(userId);
    }

    public void DeleteRecipe ( Recipe recipe)
    {
        appDatabase.recipeDAO().delete(recipe);
    }

    public Recipe getRecipe(int RecipeId) {
        return appDatabase.recipeDAO().getRecipe(RecipeId);
    }
}
