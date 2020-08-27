package com.example.kitchenmasterapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitchenmasterapp.database.Recipe;
import com.example.kitchenmasterapp.repositories.Recipe.OnRecipeRepositoryActionListener;
import com.example.kitchenmasterapp.repositories.Recipe.RecipeRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment{

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button captureBtn;
    ImageView imgView;
    View view;
    Uri image_uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        captureBtn = view.findViewById(R.id.capture_image_btn);
        imgView = view.findViewById(R.id.image_view);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        openCamera();
                    }
                }
            }
        });

        final EditText nameRecipe = view.findViewById(R.id.et_name);
        final EditText descriptionRecipe = view.findViewById(R.id.et_description);
        final EditText caloriesRecipe = view.findViewById(R.id.et_calories);
        final EditText preparation_timeRecipe = view.findViewById(R.id.et_preparation_time);
        final EditText ingredientsRecipe = view.findViewById(R.id.et_ingredients);
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final TextView imagePath = view.findViewById(R.id.string_image_uri);;

        //identificare buton adaugare reteta
        Button addRecipe = view.findViewById(R.id.btn_add_recipe);
        addRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);

                int userId = sharedPref.getInt("com.example.kitchenmasterapp.userId", 0);

                if (nameRecipe.getText().toString().isEmpty()) {
                    nameRecipe.setError("Field required");
                    nameRecipe.requestFocus();
                } else if( descriptionRecipe.getText().toString().isEmpty())
                {
                    descriptionRecipe.setError("Field requierd");
                    descriptionRecipe.requestFocus();
                }
                else {

                    final Recipe recipe = new Recipe(nameRecipe.getText().toString(), descriptionRecipe.getText().toString(),
                            ingredientsRecipe.getText().toString(), date, Integer.parseInt(caloriesRecipe.getText().toString()),
                            Integer.parseInt(preparation_timeRecipe.getText().toString()), imagePath.getText().toString(), userId);

                    new RecipeRepository(getContext()).insertTask(recipe, new OnRecipeRepositoryActionListener() {
                        @Override
                        public void actionSucces() {
                            Toast.makeText(getContext(), "Recipe successfully added", Toast.LENGTH_SHORT).show();
                            //salvez id-ul retetei
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("com.example.kitchenmasterapp.recipeId", recipe.getRecipe_id());
                            editor.apply();
                            //lansez catre o noua activitate
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container_main,new MyRecipesFragment()).commit();
                        }

                        @Override
                        public void actionFailed() {
                            Toast.makeText(getContext(), "Recipe could not be added!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

        });

        return view;
    }

    private void openCamera() {
        Context context = getActivity().getApplicationContext();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String string_image_uri = image_uri.toString();
            Uri uri = Uri.parse(string_image_uri);
            imgView.setImageURI(uri);
            TextView uri_text_view = this.view.findViewById(R.id.string_image_uri);
            uri_text_view.setText(string_image_uri);

        }
    }
}
