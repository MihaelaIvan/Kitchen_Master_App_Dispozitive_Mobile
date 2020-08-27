package com.example.kitchenmasterapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitchenmasterapp.database.Recipe;
import com.example.kitchenmasterapp.database.User;
import com.example.kitchenmasterapp.repositories.Recipe.RecipeRepository;
import com.example.kitchenmasterapp.repositories.User.UserRepository;

import java.util.ArrayList;

public class RecipesByUserIdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Recipe> items;
    private Context context;
    //private  ObjectAnimator objAnimator;

    public RecipesByUserIdAdapter(Context context, ArrayList<Recipe> items) {
        this.context = context;
        this.items = items;
        //this.objAnimator = objAnimator;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.my_recipes_list_row, parent, false);

        RecyclerView.ViewHolder viewHolder = new RecipesByUserIdAdapter.ViewHolder(view);

        Button deleteRecipe = view.findViewById(R.id.btn_delete_recipe);
        final TextView recipeIdTextView = ((ViewHolder)viewHolder).recipeIdTextView;

        deleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeRepository recipeRepository = new RecipeRepository(context);
                //caut recipe-ul
                Recipe recipe = recipeRepository.getRecipe(Integer.parseInt(recipeIdTextView.getText().toString()));

                recipeRepository.DeleteRecipe(recipe);
                Recipe recipe2 = recipeRepository.getRecipe(Integer.parseInt(recipeIdTextView.getText().toString()));


                if(recipe2 == null)
                {
                    Toast.makeText(context, "Recipe successfully deleted!", Toast.LENGTH_SHORT).show();
                    FragmentManager manager = ((MainActivity)context).getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_container_main, new MyRecipesFragment()).commit();

                }
                else
                {
                    Toast.makeText(context, "Error on delete recipe!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //pastrez id-ul reteti pt a aduce apoi datele
                SharedPreferences sharedPref = context.getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("com.example.kitchenmasterapp.recipeId", Integer.parseInt(recipeIdTextView.getText().toString()));
                editor.apply();

                FragmentManager manager = ((MainActivity)context).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container_main, new ShowRecipeFragment()).commit();
            }
        });
        return viewHolder;

        //return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ImageView image = ((ViewHolder)viewHolder).recipePicture;
        TextView nameTextView = ((ViewHolder)viewHolder).nameTextView;
        TextView recipeIdTextView = ((ViewHolder)viewHolder).recipeIdTextView;
        Uri uri = Uri.parse((items.get(position).getImage_path()));
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                this.context.getContentResolver(), Integer.parseInt(uri.getLastPathSegment()),
                MediaStore.Images.Thumbnails.MINI_KIND,
                (BitmapFactory.Options) null );
        nameTextView.setText(items.get(position).getName());
        recipeIdTextView.setText(((Integer)items.get(position).getRecipe_id()).toString());

        image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filterList(ArrayList<Recipe> filteredList){
        items = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView recipeIdTextView;
        public ImageView recipePicture;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.recipeName2);
            recipeIdTextView = itemView.findViewById(R.id.recipeId);
            recipePicture = itemView.findViewById(R.id.recipePicture);
        }
    }
}
