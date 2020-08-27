package com.example.kitchenmasterapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.kitchenmasterapp.database.Recipe;
import com.example.kitchenmasterapp.repositories.Recipe.RecipeRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyRecipesFragment extends Fragment {

    RecipesByUserIdAdapter recipesByUserIdAdapter;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();

    ImageView bakeImageView;
    ObjectAnimator objectAnimator;

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("com.example.kitchenmasterapp.userId", 0);

        recipes = (ArrayList<Recipe>) new RecipeRepository(getContext()).getRecipesByUserId(userId);

        Context context = view.getContext();
        bakeImageView = (ImageView) view.findViewById(R.id.imgBake);
        objectAnimator = ObjectAnimator.ofFloat(bakeImageView, "rotation", 360);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_recipes);
        recyclerView.setHasFixedSize(true);
        recipesByUserIdAdapter = new RecipesByUserIdAdapter(getContext(), recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recipesByUserIdAdapter);
        layoutManager.setSmoothScrollbarEnabled (true);

        EditText editText = (EditText) view.findViewById(R.id.search_recipe2);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                //1000 inseamna 1 secunda
                objectAnimator.setDuration(2000);
                objectAnimator.start();
            }
        });
    }

    private void filter(String text){
        ArrayList<Recipe> filteredRecipes = new ArrayList<Recipe>();

        for(Recipe recipe: recipes){
            if(recipe.getName().toLowerCase().contains(text.toLowerCase())){
                filteredRecipes.add(recipe);
            }
        }
        recipesByUserIdAdapter.filterList(filteredRecipes);
    }
}