package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.activities.LiveActivity;
import com.example.w24_3175_g11_peekaboo.activities.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;


public class ParentMoreFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    Button btnStartNewLive;
    TextInputEditText editLiveId;

    String userId;
    String liveId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_more, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.btnLogout);
        Button directionButton = view.findViewById(R.id.btnDaycareDirection);
        Button makepayButton = view.findViewById(R.id.btnMakePayment);

        btnStartNewLive = view.findViewById(R.id.btnStartLive);
        editLiveId = view.findViewById(R.id.edtLiveId);

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

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container, new GoogleMapFragment());
                    transaction.addToBackStack(null); // Add the transaction to the back stack
                    transaction.commit();
                }

        });
        makepayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_container, new paymentFragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        btnStartNewLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveId = editLiveId.getText().toString();
                if(liveId.isEmpty()){
                    editLiveId.setError("Live Id is Required");
                    editLiveId.requestFocus();
                    return;
                }
                startMeeting();
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
         userId = UUID.randomUUID().toString();
            Intent intent = new Intent(getActivity(), LiveActivity.class);
            intent.putExtra("user_id",userId);
            intent.putExtra("name","Parent");
            intent.putExtra("live_id",liveId);
            intent.putExtra("host",false);
            startActivity(intent);
    }
}