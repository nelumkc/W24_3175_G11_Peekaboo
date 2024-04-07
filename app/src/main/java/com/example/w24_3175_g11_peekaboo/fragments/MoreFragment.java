package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.activities.LiveActivity;
import com.example.w24_3175_g11_peekaboo.activities.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;
import java.util.UUID;


public class MoreFragment extends Fragment {


    private FirebaseAuth firebaseAuth;

    Button btnStartNewLive;
    TextInputEditText editLiveId, editName;

    String name, userId;
    String liveId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more, container, false);

        btnStartNewLive = view.findViewById(R.id.btnStartLive);
        editLiveId = view.findViewById(R.id.edtLiveId);
        editName = view.findViewById(R.id.edtName);

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

        btnStartNewLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editName.getText().toString();

                if(name.isEmpty()){
                    editName.setError("Name is Required");
                    editName.requestFocus();
                    return;
                }

                if(liveId.length()>0 && liveId.length()!=5){
                    editName.setError("Invalid id");
                    editName.requestFocus();
                    return;

                }
                startMeeting();
            }
        });

        editLiveId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                liveId = editLiveId.getText().toString();
                if(liveId.length()==0){
                    btnStartNewLive.setText("Peekaboo Live");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

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

    void startMeeting(){

        boolean isHost = true;
        if(liveId.length()==5){
            isHost = false;
        }else{
            liveId = generateLiveId();
        }
        userId = UUID.randomUUID().toString();
        Intent intent = new Intent(getActivity(), LiveActivity.class);
        intent.putExtra("user_id",userId);
        intent.putExtra("name",name);
        intent.putExtra("live_id",liveId);
        intent.putExtra("host",isHost);
        startActivity(intent);
    }

    String generateLiveId(){
        StringBuilder id = new StringBuilder();
        while (id.length()!=5){
            int random = new Random().nextInt(10);
            id.append(random);
        }
        return id.toString();
    }
}