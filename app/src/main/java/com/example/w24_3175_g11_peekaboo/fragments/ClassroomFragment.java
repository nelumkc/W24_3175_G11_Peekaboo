package com.example.w24_3175_g11_peekaboo.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.adapters.ChildAdapter;
import com.example.w24_3175_g11_peekaboo.databases.DataBaseHelper;
import com.example.w24_3175_g11_peekaboo.model.Child;

import java.util.ArrayList;


public class ClassroomFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Child> childrenList;
    DataBaseHelper db;
    ChildAdapter adapter;

    Button btnNewChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classroom, container, false);

        btnNewChild = view.findViewById(R.id.btnNewChild);

        db = new DataBaseHelper(getContext());
        childrenList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewChildren);
        adapter = new ChildAdapter(getContext(),childrenList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        displayData();

        btnNewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Register new child", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_container, new ChildRegistrationFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        return view;
    }

    private void displayData() {
        Cursor cursor = db.getData();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "No Entry Exists", Toast.LENGTH_SHORT).show();
            return;
        }else{
            while (cursor.moveToNext()){
                String  name = cursor.getString(1);
                String  dob = cursor.getString(3);
                String profilePicPath = cursor.getString(5);
                childrenList.add(new Child(name, dob, profilePicPath));
            }
            adapter.notifyDataSetChanged();
            cursor.close();
        }
    }
}