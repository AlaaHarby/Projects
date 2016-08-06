package com.example.alaa.parkingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by Alaa on 4/19/2016.
 */
public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences mSharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mSharedPreference = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user logged in
                    endAuthentication(true);
                    mSharedPreference.edit().putString("email_session", user.getEmail()).commit();
                } else {
                    endAuthentication(false);
                }
            }
        };
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SignInFragment()).commit();
    }

    public FirebaseAuth.AuthStateListener getAuthListener() {
        return mAuthListener;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    public void endAuthentication(boolean auth) {

        if (auth) {
            Intent maps = new Intent(this, MapsActivity.class);
            startActivity(maps);
        } else {
            //Snackbar.make(findViewById(R.id.main_layout), R.string.message2, Snackbar.LENGTH_LONG).show();
        }

    }

    public void LogError(Exception exception) {
        Snackbar.make(findViewById(R.id.main_layout), R.string.message2, Snackbar.LENGTH_LONG).show();
    }
}
