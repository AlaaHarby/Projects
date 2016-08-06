package com.example.alaa.parkingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class SignInFragment extends Fragment{

    public static String SIGN_UP_FRAG_TAG = "SIGN_UP";
    public static String MAPS_FRAG_TAG = "MAPS";

    FragmentManager mFragmentMgr;
    AuthAdapter mAdapter;
    ProgressBar mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_login, container, false);

        mFragmentMgr = getFragmentManager();
        mAdapter = new AuthAdapter((AuthActivity) getActivity());
        mProgressDialog = (ProgressBar) view.findViewById(R.id.progress_bar);

        final Button signUp = (Button) view.findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentMgr.beginTransaction().replace(R.id.content_frame, new SignUpFragment(), SIGN_UP_FRAG_TAG).addToBackStack(null).commit();
            }
        });

        final Button signIn = (Button) view.findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = ((EditText) view.findViewById(R.id.userName)).getText().toString();
                final String pass = ((EditText) view.findViewById(R.id.password)).getText().toString();

                mAdapter.authUser(email, pass);

            }
        });

        return view;
    }

}
