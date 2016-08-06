package com.example.alaa.parkingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Alaa on 4/14/2016.
 */
public class SignUpFragment extends Fragment {

    private AuthAdapter mAdapter;
    private String mName;
    private String mEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAdapter = new AuthAdapter((AuthActivity) getActivity());
        final View view = inflater.inflate(R.layout.activity_signup, container, false);

        if(savedInstanceState != null) {
            ((EditText) view.findViewById(R.id.user_name)).setText(savedInstanceState.getString("name", ""));
            ((EditText) view.findViewById(R.id.email)).setText(savedInstanceState.getString("email", ""));
        }

        Button sign = (Button) view.findViewById(R.id.sign_submit);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mName = ((EditText) view.findViewById(R.id.user_name)).getText().toString();
                mEmail = ((EditText) view.findViewById(R.id.email)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.pass)).getText().toString();
                String confirmPass = ((EditText) view.findViewById(R.id.pass2)).getText().toString();


                if (mName.equals("") || mEmail.equals("") || password.equals("") || confirmPass.equals(""))
                    Toast.makeText(getContext(), R.string.message1, Toast.LENGTH_LONG).show();

                else if (!password.equals(confirmPass))
                    Toast.makeText(getContext(), R.string.message2, Toast.LENGTH_LONG).show();

                else
                    mAdapter.addUser(new User(mName, mEmail, password));
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if (mName != null ) outState.putString("name" , mName);
        if (mEmail != null) outState.putString("email" , mEmail);

    }
}
