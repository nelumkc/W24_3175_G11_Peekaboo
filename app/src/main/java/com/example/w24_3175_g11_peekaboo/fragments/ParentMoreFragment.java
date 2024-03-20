package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class ParentMoreFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.btnLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                clearRememberMePreference();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                if(getActivity()!=null){
                    getActivity().finish();
                }

            }
        });


        return view;
    }


    private void clearRememberMePreference() {
        SharedPreferences preferences = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(LoginActivity.PREF_REMEMBER_ME);
        editor.apply();
    }
}