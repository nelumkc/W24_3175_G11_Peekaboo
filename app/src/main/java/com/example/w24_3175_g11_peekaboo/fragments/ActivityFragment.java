package com.example.w24_3175_g11_peekaboo.fragments;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.activities.MainActivity;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.interfaces.ChildDao;
import com.example.w24_3175_g11_peekaboo.interfaces.ParentDao;
import com.example.w24_3175_g11_peekaboo.model.Entry;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.model.Notification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityFragment extends Fragment {

    DaycareDatabase daycaredb;

    Spinner spinner;

    Button btnCreateEntry, btnupload;
    EditText desc, title;

    ImageView imageView;

    String currentImagePath = null;

    private ActivityResultLauncher<String> getContent;

    private Uri imageUrl;
    final private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    private static final int NOTIFICATION_ID = 7777;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        daycaredb = Room.databaseBuilder(getContext().getApplicationContext(), DaycareDatabase.class, "daycare.db").build();
        spinner = view.findViewById(R.id.spinnerChild);
        btnCreateEntry = view.findViewById(R.id.btnCreateEntry);
        desc = view.findViewById(R.id.editTextDescription);
        title = view.findViewById(R.id.editTextTitle);
        btnupload = view.findViewById(R.id.btnUploadImage);
        imageView = view.findViewById(R.id.imageViewSelectedImage);

        //

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.POST_NOTIFICATIONS)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.POST_NOTIFICATIONS},101);

            }
        }


        //




        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ChildDao childDao = daycaredb.childDao();
                List<Child> children = childDao.getAllChildren();

                List<String> childNames = new ArrayList<>();
                for (Child child : children) {
                    childNames.add(child.getChildFname());
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // get child name list to the spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, childNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                });
            }
        });

        btnCreateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        ChildDao childDao = daycaredb.childDao();
                        ParentDao parentDao = daycaredb.parentDao();
                        //String imagePath = currentImagePath;

                        Entry entry = new Entry(desc.getText().toString().trim(),
                                title.getText().toString().trim(), imageUrl.toString(), childDao.getChildIdByName(spinner.getSelectedItem().toString()), new Date().toString());

                       // String token = childDao.getParentUserTokenByChildfname(spinner.getSelectedItem().toString());

                        long entryid = daycaredb.entryDao().insertOneEntry(entry);
                        if(entryid!= -1){
                            Notification notification = new Notification(title.getText().toString().trim()
                                    ,childDao.getChildIdByName(spinner.getSelectedItem().toString()), entryid, false );
                            daycaredb.notificationDao().insertOneNotification(notification);
                        }


                        // Update UI on success
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Entry created", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContent.launch("image/*");
            }
        });


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ActivityResultLauncher
        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            // Handle the returned Uri
            if (uri != null) {
                imageView.setImageURI(uri);
                uploadImageToFirebase(uri);
                //currentImagePath = saveUriToInternalStorage(uri);
            }
        });
    }

    private void uploadImageToFirebase(Uri uri) {
        Toast.makeText(getActivity(), "uploadimage", Toast.LENGTH_SHORT).show();
        String fileName = System.currentTimeMillis() + "." + getFileExtension(uri);
        StorageReference fileRef = storageReference.child("images/" + fileName);
        fileRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // After successfully uploading, get the download URL
                    fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        // Here you get the image URI, which you can store in Firebase Realtime Database
                        String imageUri = downloadUri.toString();
                        imageUrl = downloadUri;
                        // Store this URI in Firebase Realtime Database under the "Images" node
                        String uploadId = databaseReference.push().getKey(); // Generates a unique id for the entry
                        databaseReference.child(uploadId).setValue(imageUri)
                                .addOnSuccessListener(aVoid -> {
                                    //Toast.makeText(getActivity(), "Image uploaded and URI saved successfully", Toast.LENGTH_LONG).show();
                                })
                                .addOnFailureListener(e -> {
                                    //Toast.makeText(getActivity(), "Failed to save URI: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }).addOnFailureListener(e -> {
                       // Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }






    private void sendPushNotification(String textTitle, String textContent, String token) {
        String CHANNEL_ID = "peekaboo_channel_1";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.peekaboo)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //
        Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("token", token);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext().getApplicationContext(),
                0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        //

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(CHANNEL_ID);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(CHANNEL_ID,
                        "Some desc" , importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationChannel.setShowBadge(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


}