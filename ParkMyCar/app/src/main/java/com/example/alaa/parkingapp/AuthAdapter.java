package com.example.alaa.parkingapp;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Alaa on 8/1/2016.
 */
public class AuthAdapter {

    private FirebaseAuth mAuth;
    private AuthActivity mActivity;

    public AuthAdapter(AuthActivity activity) {
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mActivity.getAuthListener());
    }

    public void addUser(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            mActivity.LogError(task.getException());
                        }

                    }
                });
    }

    public void authUser(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            mActivity.LogError(task.getException());
                        }
                    }
                });
    }
}

