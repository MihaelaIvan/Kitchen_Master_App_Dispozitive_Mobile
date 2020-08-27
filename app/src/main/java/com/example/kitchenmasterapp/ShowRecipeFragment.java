package com.example.kitchenmasterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitchenmasterapp.database.Recipe;
import com.example.kitchenmasterapp.repositories.Recipe.RecipeRepository;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ShowRecipeFragment extends Fragment {


    public ShowRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_recipe, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
        int recipeId = sharedPref.getInt("com.example.kitchenmasterapp.recipeId", 0); //i = default value

        RecipeRepository recipeRepository = new RecipeRepository(getActivity());
        //caut recipe-ul
        Recipe recipe = recipeRepository.getRecipe(recipeId);

        final TextView recipeNameTextView = view.findViewById(R.id.showRecipeName);
        final TextView recipeContentTextView = view.findViewById(R.id.showRecipeContenet);
        final ImageView recipeImageTextView = view.findViewById(R.id.showImage);

        recipeNameTextView.setText("\t" + "Swipe Up to see " + recipe.getName() + " recipe ");
        recipeContentTextView.setText("Ingredients: " + recipe.getIngredients() + "\n" +
                "Description: " + recipe.getDescriptopn()  + "\n" +
                "Preparation time: " + ((Integer)recipe.getPreparation_time()).toString() + "min" + "\n" +
                "Calories per 100g: " + ((Integer)recipe.getCalories()).toString() + "calories" + "\n");

        final Uri uri = Uri.parse(recipe.getImage_path());
        recipeImageTextView.setImageURI(uri);

        Button shareButton = view.findViewById(R.id.share_button);

        //actiunea de share
        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //text-ul care vreau sa fie trimis
                String text = recipeContentTextView.getText().toString();

                Intent mSharingIntent = new Intent(Intent.ACTION_SEND);
                mSharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Recipe " + recipeNameTextView.getText().toString());
                //pun textul
                mSharingIntent.putExtra(Intent.EXTRA_TEXT, text);
                //pun imaginea
                mSharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                //setez tipul
                mSharingIntent.setType("*/*");
                startActivity(Intent.createChooser(mSharingIntent,"Share text via"));
            }


        });


        return view;
    }
}