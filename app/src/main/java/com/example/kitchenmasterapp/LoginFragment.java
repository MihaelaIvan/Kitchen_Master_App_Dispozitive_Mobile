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

import com.example.kitchenmasterapp.database.User;
import com.example.kitchenmasterapp.repositories.User.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

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
}