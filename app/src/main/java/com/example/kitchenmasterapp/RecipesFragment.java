package com.example.kitchenmasterapp;

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
import android.widget.EditText;

import com.example.kitchenmasterapp.database.Recipe;
import com.example.kitchenmasterapp.repositories.Recipe.RecipeRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment {

    RecipesAdapter recipesAdapter;
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        recipes = (ArrayList<Recipe>) new RecipeRepository(getContext()).getRecipes();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_recipes);
        recyclerView.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter(getContext(), recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recipesAdapter);
        layoutManager.setSmoothScrollbarEnabled (true);

        EditText editText = (EditText) view.findViewById(R.id.search_recipe);
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
        recipesAdapter.filterList(filteredRecipes);
    }
}