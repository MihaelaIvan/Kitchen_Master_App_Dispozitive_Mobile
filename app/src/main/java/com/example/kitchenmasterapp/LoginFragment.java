package com.example.kitchenmasterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.request.RequestOptions;
import com.example.kitchenmasterapp.database.User;
import com.example.kitchenmasterapp.repositories.User.OnUserRepositoryActionListener;
import com.example.kitchenmasterapp.repositories.User.UserRepository;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    //OnActivityFragmentCommunication onActivityFragmentCommunication;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText email = view.findViewById(R.id.et_email);
        final EditText password = view.findViewById(R.id.et_password);

        loginButton = view.findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("email,public_profile"));
        loginButton.setFragment(this);
        CheckLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                if (loggedIn){
                    loginButton.setVisibility(View.GONE);
                }
                else{
                    loginButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if (loggedIn){
            loginButton.setVisibility(View.GONE);
        }
        else{
            loginButton.setVisibility(View.VISIBLE);
        }

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent startPos, MotionEvent finishPos, float velocityX,
                                           float velocityY) {
                        Log.i("Swipe fling", "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(startPos.getY() - finishPos.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (finishPos.getX() - startPos.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("Swipe fling", "Left to Right");
                                RegisterFragment registerFragment = new RegisterFragment();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container_first, registerFragment);
                                fragmentTransaction.commit();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(startPos, finishPos, velocityX, velocityY);
                    }
                });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        VideoView videoView =view.findViewById(R.id.videoVw);
        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(getContext());
        mediaController.setVisibility(View.GONE);
        //Location of Media File
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.videofood);
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();


        Button loginUser = view.findViewById(R.id.btn_login);
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRepository userRepository = new UserRepository(getContext());
                //caut user-ul
                User user = userRepository.getUserByEmailString(email.getText().toString());
                String passwordFound = userRepository.getUserPassword(email.getText().toString()) ;

                if (email.getText().toString().isEmpty())
                {
                    email.setError("Field required");
                    email.requestFocus();

                } else if (password.getText().toString().isEmpty())
                {
                    password.setError("Field required");
                    password.requestFocus();
                } else if (user != null && !passwordFound.equals(password.getText().toString()))
                {
                    password.setError("Incorrect password!");
                    password.requestFocus();

                }
                else {
                    //daca exista ii pastrez valaorea id-ului
                    if (user != null) {
                        Toast.makeText(getContext(), "User found!", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("com.example.kitchenmasterapp.userId", user.getId());
                        editor.apply();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "User not found!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null)
            {
                Toast.makeText(getContext(), "User logged out", Toast.LENGTH_LONG).show();
            }
            else{
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    final String email = object.getString("email");
                    final String user_name = first_name + " " + last_name;
                    //Caut user-ul dupa nume si email si daca nu exista il inserez - email-ul este unic
                    UserRepository userRepository = new UserRepository(getContext());
                    //caut user-ul
                    User user = userRepository.getUserByEmailAndNameString(email, user_name);

                    //daca nu exista il inserez si pastrez id ul
                    if(user == null)
                    {
                        final User userAdded = new User(user_name,"password1234", email);
                        new UserRepository(getContext()).insertTask(userAdded, new OnUserRepositoryActionListener() {
                            @Override
                            public void actionSucces() {
                                UserRepository userRep = new UserRepository(getContext());
                                User userFound = userRep.getUser(email, "password1234");

                                //Memorez userId in sharedPreferences
                                SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("com.example.kitchenmasterapp.userId", userFound.getId());
                                editor.apply();
                            }
                            @Override
                            public void actionFailed() {

                            }
                        });
                    }
                    else //daca exista ii iau id-ul
                    {
                        //Memorez userId in sharedPreferences
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("com.example.kitchenmasterapp.userId", user.getId());
                        editor.apply();
                    }

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void CheckLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken() != null){
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }
}