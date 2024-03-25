package com.example.w24_3175_g11_peekaboo.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;


public class ChatFragment extends Fragment {

    EditText searchInput;
    ImageButton searchButton;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchInput.getText().toString();
                if(searchTerm.isEmpty()){
                    Toast.makeText(getContext(), "Get all child names", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "filter name from database", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }
}