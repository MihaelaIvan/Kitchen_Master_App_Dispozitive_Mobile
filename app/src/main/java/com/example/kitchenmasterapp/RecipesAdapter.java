package com.example.kitchenmasterapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitchenmasterapp.database.Recipe;
import com.example.kitchenmasterapp.repositories.Recipe.RecipeRepository;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Recipe> items;
    private Context context;

    public RecipesAdapter(Context context, ArrayList<Recipe> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.recipe_list_row, parent, false);
        RecyclerView.ViewHolder viewHolder = new RecipesAdapter.ViewHolder(view);

        final TextView recipeIdTextView = ((ViewHolder)viewHolder).recipeIdTextView;

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
        //Uri uri = Uri.parse((items.get(position).getImage_path()));
        //image.setImageURI(uri);
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

            nameTextView = itemView.findViewById(R.id.recipeName);
            recipeIdTextView = itemView.findViewById(R.id.recipeId);
            recipePicture = itemView.findViewById(R.id.recipePicture);
        }
    }
}
