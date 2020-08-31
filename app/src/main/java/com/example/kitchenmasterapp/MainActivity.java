package com.example.kitchenmasterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kitchenmasterapp.repositories.User.UserRepository;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //setez numele utilizatorului
        SharedPreferences sharedPref =  getApplicationContext().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("com.example.kitchenmasterapp.userId", 0); //i = default value
        UserRepository userRepository = new UserRepository(getApplicationContext());
        String userName = userRepository.getUserNameById(userId);

        TextView userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.UserName);
        userNameTextView.setText("Hello, " + userName + "!");

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //TextView userNameTextView = findViewById(R.id.UserName);
        //userNameTextView.setText("Mihaela");

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, new RecipesFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_add_recipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, new AddRecipeFragment()).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, new RecipesFragment()).commit();
                break;
            case R.id.nav_my_recipes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,new MyRecipesFragment()).commit();
                break;
            case R.id.nav_log_out:
                LoginManager.getInstance().logOut();
                //elimin valorile pastrate in SharedPreferences
                SharedPreferences sharedPref = this.getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();
                Intent intent =  new Intent(this, FirstActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            moveTaskToBack(true);
            //super.onBackPressed();
    }
}