package com.example.kitchenmasterapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RecipePhoto {

    @PrimaryKey
    private int recipe_id;

    @ColumnInfo(name = "image")
    private String image;

    public RecipePhoto(int recipe_id, String image) {
        this.recipe_id = recipe_id;
        this.image = image;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
