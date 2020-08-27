package com.example.kitchenmasterapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.kitchenmasterapp.database.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {

    @Query("SELECT * FROM Recipes order by recipe_date")
    List<Recipe> getAll();

    @Query("SELECT * FROM Recipes WHERE user_id =:userId")
    List<Recipe> getAllRecipesByUserId(int userId);

    @Query("SELECT * FROM Recipes WHERE recipe_id=:id")
    Recipe getRecipe(int id);

    @Insert
    void insertAll(Recipe... recipes);

    @Insert
    void insertTask(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

}
