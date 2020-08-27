package com.example.kitchenmasterapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.kitchenmasterapp.database.User;
import com.example.kitchenmasterapp.database.UserDAO;

@Database(entities = {User.class, Recipe.class, RecipePhoto.class},version = 1)
public abstract class MyAppDatabase extends RoomDatabase {

    public abstract UserDAO userDAO();
    public abstract RecipeDAO recipeDAO();
    public abstract RecipePhotoDAO recipePhotoDAO();

}
