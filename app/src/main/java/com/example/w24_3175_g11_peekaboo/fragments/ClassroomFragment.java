package com.example.w24_3175_g11_peekaboo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.adapters.ChildAdapter;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.Child;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClassroomFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Child> childrenList;
    DaycareDatabase daycaredb;
    ChildAdapter adapter;

    Button btnNewChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classroom, container, false);

        btnNewChild = view.findViewById(R.id.btnNewChild);

        ///
        daycaredb = Room.databaseBuilder(getContext().getApplicationContext(), DaycareDatabase.class, "daycare.db").allowMainThreadQueries().build();
        childrenList = new ArrayList<>();
        adapter = new ChildAdapter(getContext(),childrenList);
        recyclerView = view.findViewById(R.id.recyclerViewChildren);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Child> children = daycaredb.childDao().getAllChildren();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (children.isEmpty()) {
                            Toast.makeText(getContext(), "No Entry Exists", Toast.LENGTH_SHORT).show();

                        } else {
                            childrenList.clear();
                            childrenList.addAll(children);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        ///





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




    }
}