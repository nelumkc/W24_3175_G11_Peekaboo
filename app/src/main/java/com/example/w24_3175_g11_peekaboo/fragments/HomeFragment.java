package com.example.w24_3175_g11_peekaboo.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        GridLayout gridLayout = view.findViewById(R.id.gridLayout);

        List<Item> itemList = new ArrayList<>();
        try {
            // Load the JSON array from the assets folder
            JSONArray jsonArray = JsonUtils.loadJSONArrayFromAsset(this.getContext(), "home_data.json");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String text = jsonObject.getString("text");
                    String image = jsonObject.getString("image");
                    String fragment = jsonObject.getString("fragment");
                    itemList.add(new Item(text, image,fragment));
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
                //Toast.makeText(getActivity(), "click event", Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                String Title = null;
                // Decide which fragment to navigate to based on the item's properties
                switch (item.getFragment()) {
                    case "AttendanceFragment":
                        selectedFragment = new AttendanceFragment();
                        break;
                    case "ActivityFragment":
                        selectedFragment = new ActivityFragment();
                        Title = "Activity";
                        break;
                    default:
                        Toast.makeText(getActivity(), "Fragment not found", Toast.LENGTH_SHORT).show();
                        break;
                }
                // If a valid fragment was selected, perform the fragment transaction
                if (selectedFragment != null) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container, selectedFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //Toast.makeText(getActivity(), "Opening: " + item.getText(), Toast.LENGTH_SHORT).show();
                }

            });

            gridLayout.addView(itemView);
        }
        return view;
    }
}