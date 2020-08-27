package com.example.kitchenmasterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kitchenmasterapp.database.User;
import com.example.kitchenmasterapp.repositories.User.OnUserRepositoryActionListener;
import com.example.kitchenmasterapp.repositories.User.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment{


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        final EditText userName = view.findViewById(R.id.et_name);
        final EditText password = view.findViewById(R.id.et_password);
        final EditText email = view.findViewById(R.id.et_email);
        final EditText re_password = view.findViewById(R.id.et_repassword);

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
                            if (finishPos.getX() - startPos.getX() < -SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("Swipe fling", "Right to Left");
                                LoginFragment loginFragment = new LoginFragment();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container_first, loginFragment);
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
        Button addUser = view.findViewById(R.id.btn_register);
        addUser.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

                User userFound = new UserRepository(getContext()).getUserByEmailString(email.getText().toString());

                if (userName.getText().toString().isEmpty()) {
                    userName.setError("Field required");
                    userName.requestFocus();
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Field required");
                    password.requestFocus();
                } else if (re_password.getText().toString().isEmpty()) {
                    re_password.setError("Field required");
                    re_password.requestFocus();
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Field required");
                    email.requestFocus();
                }else if (userFound != null) {
                    email.setError("For this email adress alreday exists an account!");
                    email.requestFocus();
                }else if (!password.getText().toString().equals(re_password.getText().toString())) {
                    re_password.setError("Repassword must be the same with password!");
                    re_password.requestFocus();
                } else
                {
                    //inserez noul user daca totul este ok
                    final User user = new User(userName.getText().toString(), password.getText().toString(),email.getText().toString());

                    new UserRepository(getContext()).insertTask(user, new OnUserRepositoryActionListener() {
                        @Override
                        public void actionSucces() {
                            Toast.makeText(getContext(), "Successfully registered", Toast.LENGTH_SHORT).show();

                            //daca s-a inregistrat cu succes caut user-ul in baza de date pt ai luat id-ul pt al folosii in urmatoarele actiuni
                            UserRepository userRep = new UserRepository(getContext());
                            User userAdded = userRep.getUser(email.getText().toString(), password.getText().toString());

                            SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.kitchenmasterapp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("com.example.kitchenmasterapp.userId", userAdded.getId());
                            editor.apply();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void actionFailed() {
                            Toast.makeText(getContext(), "User could not be added!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        return  view;
        //return inflater.inflate(R.layout.fragment_register, container, false);
    }
}