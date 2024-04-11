package com.example.w24_3175_g11_peekaboo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.interfaces.AttendanceUpdateCallback;
import com.example.w24_3175_g11_peekaboo.model.Attendance;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>{
    private Context context;
    private ArrayList<Child> children;

    DaycareDatabase daycaredb;

    private AttendanceUpdateCallback updateCallback ;// Callback to handle attendance updates

    public AttendanceAdapter(Context context, ArrayList<Child> children,DaycareDatabase daycaredb, AttendanceUpdateCallback updateCallback ) {
        this.context = context;
        this.children = children;
        this.daycaredb = daycaredb;
        this.updateCallback  = updateCallback ;
    }

    @NonNull
    @Override
    public AttendanceAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_attendance,parent,false);
        return new AttendanceAdapter.AttendanceViewHolder(v);
    }

    // Binds data to each list item, including setting up the attendance status and handling button clicks for attendance actions
    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Child child = children.get(position);
        holder.textViewName.setText(child.getChildFname());
        // Loads the child's image using Glide
        if(child.getChildImage()!=null){
            Glide.with(context).load(child.getChildImage()).into(holder.profileImageView);
        }


        // Check the attendance state for the child
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            Attendance existingAttendance = daycaredb.childDao().getAttendanceForChild(child.getChildId(), currentDate);

            ((Activity)context).runOnUiThread(() -> {
                if (existingAttendance != null) {
                    if (existingAttendance.checkOutTime != null) {
                        // Child has checked out
                        holder.attendanceButton.setText("Checked Out");
                        holder.textViewAttLabel.setText("");
                        holder.attendanceButton.setBackgroundColor(ContextCompat.getColor(context, R.color.inputTextColor));
                        holder.attendanceButton.setEnabled(false);
                    } else {
                        // Child has checked in but not checked out
                        holder.attendanceButton.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                        holder.attendanceButton.setText("Check Out");
                        holder.textViewAttLabel.setText("Present");
                        holder.textViewAttLabel.setTextColor(ContextCompat.getColor(context, R.color.GreenColor));                     }
                } else {
                    // Child has not checked in yet
                    holder.attendanceButton.setText("Check In");
                    holder.textViewAttLabel.setText("Not Present");
                    holder.textViewAttLabel.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            });
        });
        //

        holder.attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String currentDate = sdf.format(new Date());
                        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        String currentTime = sdfTime.format(new Date());

                        Attendance existingAttendance = daycaredb.childDao().getAttendanceForChild(child.getChildId(), currentDate);
                        boolean isNewCheckIn = false;
                        if (existingAttendance == null) {
                            // No record exists, this is a check-in
                            Attendance attendance = new Attendance();
                            attendance.attchildId = child.getChildId();
                            attendance.attDate = currentDate;
                            attendance.checkInTime = currentTime;
                            attendance.isPresent = true;

                            long id = daycaredb.childDao().insertAttendance(attendance);
                            isNewCheckIn = id != -1;
                            if (id != -1) {
                                // Successfully checked in
                                ((Activity)context).runOnUiThread(() -> {
                                    holder.attendanceButton.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                                    holder.attendanceButton.setText("Check Out");
                                    holder.textViewAttLabel.setText("Present");
                                    holder.textViewAttLabel.setTextColor(ContextCompat.getColor(context, R.color.GreenColor));

                                    updatePresentAbsentCount(currentDate);
                                });
                            }
                        } else if (existingAttendance.checkOutTime == null) {
                            // Record exists without check-out time, this is a check-out
                            existingAttendance.checkOutTime = currentTime;
                            daycaredb.childDao().updateAttendance(existingAttendance);

                            // Successfully checked out
                            ((Activity)context).runOnUiThread(() -> {
                                ((Activity)context).runOnUiThread(() -> {
                                    holder.attendanceButton.setText("Checked Out");
                                    holder.textViewAttLabel.setText("");
                                    holder.attendanceButton.setBackgroundColor(ContextCompat.getColor(context, R.color.inputTextColor));
                                    holder.attendanceButton.setEnabled(false); // Optionally disable the button after check-out

                                    // Since this is a check-out, we should also notify the fragment to update counts
                                    updatePresentAbsentCount(currentDate);
                                });

                            });
                        }

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAttLabel, presentCount, absentCount;
        ImageView profileImageView;

        Button attendanceButton;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.childName);
            profileImageView = itemView.findViewById(R.id.childImage);
            attendanceButton = itemView.findViewById(R.id.attendanceButton);
            textViewAttLabel = itemView.findViewById(R.id.attendanceLabel);

        }
    }


    // Updates the present and absent counts asynchronously and triggers the callback
    private void updatePresentAbsentCount(String currentDate) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            int presentCount = daycaredb.childDao().countPresentChildren(currentDate);
            int absentCount = daycaredb.childDao().countAbsentChildren(currentDate);

            ((Activity) context).runOnUiThread(() -> {
                if (updateCallback != null) {
                    updateCallback.onAttendanceUpdated();
                }
            });
        });
    }


}
