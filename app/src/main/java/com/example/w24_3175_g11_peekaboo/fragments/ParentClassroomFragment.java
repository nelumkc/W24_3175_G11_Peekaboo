package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.Entry;

import java.util.ArrayList;
import java.util.List;


public class ParentClassroomFragment extends Fragment {

    DaycareDatabase daycaredb;
    public static final String PREFS_NAME = "sharedPref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_classroom, container, false);

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);

        daycaredb = Room.databaseBuilder(getContext().getApplicationContext(), DaycareDatabase.class, "daycare.db").allowMainThreadQueries().build();

        long currentUserId = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong("currentUserId", -1);
        long parentId = daycaredb.parentDao().getParentIdByUserId(String.valueOf(currentUserId));
        //entries by parent id
        List<Entry> entries = daycaredb.entryDao().getLatestFourEntriesByParentId(parentId);

        ArrayList<SlideModel> imageList = new ArrayList<>();

        String placeholderUrl = "https://example.com/placeholder.png";
        for (Entry entry : entries) {
            String imageUrl = entry.getEntryImage();
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = placeholderUrl; // Use placeholder image
            }
            imageList.add(new SlideModel(imageUrl, entry.getEntryTitle(), ScaleTypes.FIT));
        }
        imageSlider.setImageList(imageList,ScaleTypes.CENTER_CROP);

        return view;
    }
}