package com.example.kitchenmasterapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface RecipePhotoDAO {

    @Insert
    void insertTask(RecipePhoto recipePhoto);

    @Query("SELECT image FROM RecipePhoto WHERE recipe_id = :id")
    String getImage(int id);

    @Query("UPDATE RecipePhoto SET image = :newImage  WHERE recipe_id= :recipeId")
    void updateRecipePhotoImage(int recipeId, String newImage);

}
