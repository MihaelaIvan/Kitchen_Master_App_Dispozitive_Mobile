package com.example.kitchenmasterapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FirstActivity extends AppCompatActivity {

    public static  FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

       //initializez fragmentul
        fragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.fragment_container_first) != null)
        {
            if(savedInstanceState != null)
            {
                return;
            }

            //aici se va adauga register fragment la main activity
            fragmentManager.beginTransaction().add(R.id.fragment_container_first, new RegisterFragment()).commit();
        }
    }
    @Override
    public void onBackPressed() {
            moveTaskToBack(true);
    }

}