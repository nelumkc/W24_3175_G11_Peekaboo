package com.example.w24_3175_g11_peekaboo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.w24_3175_g11_peekaboo.R;

import java.util.ArrayList;


public class ParentClassroomFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_classroom, container, false);
        ImageSlider imageslider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.bunnysplash, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.eagle, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.elephant, ScaleTypes.FIT));
        imageslider.setImageList(slideModels,ScaleTypes.FIT);
        return view;
    }
}