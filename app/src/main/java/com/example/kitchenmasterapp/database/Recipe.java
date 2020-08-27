package com.example.kitchenmasterapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    private int recipe_id;

    @ColumnInfo(name = "recipe_name")
    private String name;

    @ColumnInfo(name = "recipe_description")
    private String descriptopn;

    @ColumnInfo(name = "recipe_ingredients")
    private String ingredients;

    @ColumnInfo(name = "recipe_date")
    private String date;

    @ColumnInfo(name = "number_of_calories")
    private int calories;

    @ColumnInfo(name = "preparation_time")
    private  int preparation_time;

    @ColumnInfo(name = "recipe_image")
    private String image_path;

    //id-ul user-ului care a adugat reteta (relatie one-to-many)
    @ColumnInfo(name = "user_id")
    private  int user_id;

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptopn() {
        return descriptopn;
    }

    public void setDescriptopn(String descriptopn) {
        this.descriptopn = descriptopn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(int preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Recipe(String name, String descriptopn, String ingredients, String date, int calories, int preparation_time, String image_path, int user_id) {
        this.name = name;
        this.descriptopn = descriptopn;
        this.ingredients = ingredients;
        this.date = date;
        this.calories = calories;
        this.preparation_time = preparation_time;
        this.image_path = image_path;
        this.user_id = user_id;
    }
}
