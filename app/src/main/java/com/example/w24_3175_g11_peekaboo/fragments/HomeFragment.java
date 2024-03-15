package com.example.w24_3175_g11_peekaboo.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.model.Item;
import com.example.w24_3175_g11_peekaboo.JsonUtils;
import com.example.w24_3175_g11_peekaboo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        GridLayout gridLayout = rootView.findViewById(R.id.gridLayout);

        // Sample data for demonstration
        List<Item> itemList = new ArrayList<>();
        try {
            JSONArray jsonArray = JsonUtils.loadJSONArrayFromAsset(this.getContext(), "home_data.json");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String text = jsonObject.getString("text");
                    String image = jsonObject.getString("image");
                    itemList.add(new Item(text, image));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Inflate grid items dynamically
        for (Item item : itemList) {
            View itemView = inflater.inflate(R.layout.home_grid_item_layout, gridLayout, false);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            TextView textView = itemView.findViewById(R.id.textView);

            imageView.setImageResource(this.getContext().getResources().getIdentifier(
                        item.getImageResource(),"drawable",this.getContext().getPackageName()));
            textView.setText(item.getText());

            itemView.setOnClickListener(v -> {
                Toast.makeText(getActivity(), "click event", Toast.LENGTH_SHORT).show();
                // Handle click event to load new fragment layout
                // You can use an interface to communicate with the activity/fragment
            });

            gridLayout.addView(itemView);
        }
        return rootView;
    }
}