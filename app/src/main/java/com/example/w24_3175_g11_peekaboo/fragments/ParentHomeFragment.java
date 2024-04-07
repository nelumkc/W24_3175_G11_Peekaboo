package com.example.w24_3175_g11_peekaboo.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.activities.MainActivity;
import com.example.w24_3175_g11_peekaboo.adapters.ChildAdapter;
import com.example.w24_3175_g11_peekaboo.adapters.EntryAdapter;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.model.Entry;
import com.example.w24_3175_g11_peekaboo.model.Notification;

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

        long currentUserId = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong("currentUserId", -1);

        displayData(currentUserId);

        sendPushNotification(currentUserId);

        //adding the image slider
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);

        daycaredb = Room.databaseBuilder(getContext().getApplicationContext(), DaycareDatabase.class, "daycare.db").allowMainThreadQueries().build();

        getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong("currentUserId", -1);
        long parentId = daycaredb.parentDao().getParentIdByUserId(String.valueOf(currentUserId));
        //entries by parent id
        List<Entry> entries = daycaredb.entryDao().getEntriesByParentId(parentId);

        // define new list
        // push latest 4 entries in to the new list
        // iterate new list instead of existing one

        ArrayList<SlideModel> imageList = new ArrayList<>();

        String placeholderUrl = "https://example.com/placeholder.png";
        for (Entry entry : entries) {
            String imageUrl = entry.getEntryImage();
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = placeholderUrl; // Use placeholder image
            }
            imageList.add(new SlideModel(imageUrl, entry.getEntryTitle(), ScaleTypes.FIT));
        }
        imageSlider.setImageList(imageList,ScaleTypes.CENTER_CROP);//ending image slider

        return view;
    }

    private void displayData(long currentUserId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //long currentUserId = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong("currentUserId", -1);
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

    private void sendPushNotification(long currentUserId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Notification> notificationList = daycaredb.notificationDao().getNotificationDetailsByUser(String.valueOf(currentUserId));

                String CHANNEL_ID = "peekaboo_channel_1";
                NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                //create channel
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel =
                            notificationManager.getNotificationChannel(CHANNEL_ID);
                    if(notificationChannel == null){
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        notificationChannel = new NotificationChannel(CHANNEL_ID,
                                "Notifications" , importance);
                        notificationChannel.setLightColor(Color.GREEN);
                        notificationChannel.enableVibration(true);
                        notificationChannel.setShowBadge(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }

                final int[] notificationId = {0};
                for(Notification notification:notificationList){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.peekaboo)
                            .setContentTitle(notification.getNotTitle())
                            .setContentText("Test content")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    builder.setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent.putExtra("token", token);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext().getApplicationContext(),
                            0,intent,PendingIntent.FLAG_MUTABLE);
                    builder.setContentIntent(pendingIntent);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final int id = notificationId[0]++;
                                notificationManager.notify(id, builder.build());
                                daycaredb.notificationDao().updateNotification(notification.getNotId());
                            }
                        });

                    }

                }

            }
        });

    }
}