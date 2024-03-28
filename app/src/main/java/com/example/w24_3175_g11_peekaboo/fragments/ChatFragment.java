package com.example.w24_3175_g11_peekaboo.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.adapters.ChildAdapter;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.Child;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChatFragment extends Fragment {

    EditText searchInput;
    ImageButton searchButton;
    RecyclerView recyclerView;
    DaycareDatabase daycaredb;
    ChildAdapter adapter;
    ArrayList<Child> childrenList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        searchButton = view.findViewById(R.id.btnSearchParent);
        searchInput = view.findViewById(R.id.editTextSearch);
        recyclerView = view.findViewById(R.id.recyclerViewParent);

        daycaredb = Room.databaseBuilder(getContext().getApplicationContext(), DaycareDatabase.class, "daycare.db").allowMainThreadQueries().build();
        childrenList = new ArrayList<>();

        adapter = new ChildAdapter(getContext(),childrenList,true,this::navigateToNewChatFragment);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchInput.getText().toString();
                if(searchTerm.isEmpty()){
                    Toast.makeText(getContext(), "Get all child names", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "filter name from database", Toast.LENGTH_SHORT).show();
                    setupSearchRecycleView(searchTerm);
                }
            }
        });
        return view;
    }

    private void setupSearchRecycleView(String searchTerm) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Child> children = daycaredb.childDao().searchChildrenByName("%" + searchTerm + "%");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (children.isEmpty()) {
                            childrenList.clear();
                            Toast.makeText(getContext(), "No matches found", Toast.LENGTH_SHORT).show();
                        } else {
                            childrenList.clear();
                            childrenList.addAll(children);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

    }

        private void startNewChat(Child child) {

            NewChatFragment newChatFragment = new NewChatFragment();
            // Prepare a Bundle for passing the selected child as an argument
            Bundle args = new Bundle();
            args.putSerializable("child", child);

            newChatFragment.setArguments(args);

            Log.d("ChatFragment", "Preparing to start transaction to NewChatFragment");
            // replace the current fragment with NewChatFragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, newChatFragment);
            transaction.addToBackStack(null);
            Log.d("ChatFragment", "Starting transaction to NewChatFragment");
            transaction.commit();
        }

    private void navigateToNewChatFragment(Child child) {
        //ChildProfileFragment childProfileFragment = new ChildProfileFragment();
        NewChatFragment newChatFragment = new NewChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("child", child);
        newChatFragment.setArguments(args);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, newChatFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}