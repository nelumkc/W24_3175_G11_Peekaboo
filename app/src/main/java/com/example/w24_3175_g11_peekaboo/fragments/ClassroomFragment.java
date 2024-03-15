package com.example.w24_3175_g11_peekaboo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.w24_3175_g11_peekaboo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classroom, container, false);
    }
}