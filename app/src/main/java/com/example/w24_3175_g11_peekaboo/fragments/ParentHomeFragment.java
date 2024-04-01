package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.adapters.ChildAdapter;
import com.example.w24_3175_g11_peekaboo.adapters.EntryAdapter;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.model.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ParentHomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Entry> entryList;
    DaycareDatabase daycaredb;
    EntryAdapter adapter;

    public static final String PREFS_NAME = "sharedPref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_home, container, false);

        daycaredb = Room.databaseBuilder(getContext().getApplicationContext(), DaycareDatabase.class, "daycare.db").allowMainThreadQueries().build();
        entryList = new ArrayList<>();
        adapter = new EntryAdapter(getContext(),entryList,this::navigateToEntryFragment);
        recyclerView = view.findViewById(R.id.recyclerViewEntry);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        displayData();

        return view;
    }

    private void displayData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long currentUserId = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong("currentUserId", -1);
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "current user id " + currentUserId, Toast.LENGTH_SHORT).show()
                );

                if(currentUserId != -1){
                    long parentId = daycaredb.parentDao().getParentIdByUserId(String.valueOf(currentUserId));
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "paren "+ parentId, Toast.LENGTH_SHORT).show()
                    );

                    List<Entry> entries = daycaredb.entryDao().getEntriesByParentId(parentId);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (entries.isEmpty()) {
                                Toast.makeText(getContext(), "No Entry Exists", Toast.LENGTH_SHORT).show();

                            } else {
                                entryList.clear();
                                entryList.addAll(entries);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                //List<Entry> entries = daycaredb.entryDao().getAllEntries();



            }
        });

    }

    private void navigateToEntryFragment(Entry entry) {
        ChildProfileFragment childProfileFragment = new ChildProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("entry", entry);
        childProfileFragment.setArguments(args);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, childProfileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}