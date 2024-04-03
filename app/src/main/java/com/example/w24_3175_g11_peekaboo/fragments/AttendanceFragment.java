package com.example.w24_3175_g11_peekaboo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.adapters.AttendanceAdapter;
import com.example.w24_3175_g11_peekaboo.adapters.ChildAdapter;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.Child;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AttendanceFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Child> childrenList;
    DaycareDatabase daycaredb;
    AttendanceAdapter adapter;

    Button btnAttendance;

    TextView presentCount,absentCount,dateDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        presentCount = view.findViewById(R.id.totalPresent);
        absentCount = view.findViewById(R.id.totalAbsent);
        dateDisplay = view.findViewById(R.id.dateDisplay);

        String currentDate = new SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(new Date());
        dateDisplay.setText(currentDate);

        daycaredb = Room.databaseBuilder(getContext().getApplicationContext(), DaycareDatabase.class, "daycare.db").allowMainThreadQueries().build();
        childrenList = new ArrayList<>();

        adapter = new AttendanceAdapter(getContext(),childrenList,daycaredb);
        recyclerView = view.findViewById(R.id.recyclerViewAttendance);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        displayData();

        updateAttendanceCounts();


        return view;
    }

    private void updateAttendanceCounts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            int presentTotal = daycaredb.childDao().countPresentChildren(currentDate);
            int absentTotal = daycaredb.childDao().countAbsentChildren(currentDate);

            getActivity().runOnUiThread(() -> {
                presentCount.setText("Present: " + presentTotal);
                absentCount.setText("Absent: " + absentTotal);
            });
        });
    }


    private void displayData() {
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

    }
}