package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.Child;
import com.example.w24_3175_g11_peekaboo.model.Parent;
import com.example.w24_3175_g11_peekaboo.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChildRegistrationFragment extends Fragment {

   EditText firstNameEdit,lastNameEdit,dobEdit,parentNameEdit,parentEmailEdit;
   RadioGroup genderGroup;
   Button registerButton,uploadButton;
    DaycareDatabase daycaredb;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profilePic;
    private ActivityResultLauncher<String> getContent;
    private String currentImagePath = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_registration, container, false);

        firstNameEdit = view.findViewById(R.id.editTextFirstName);
        lastNameEdit = view.findViewById(R.id.editTextLastName);
        dobEdit = view.findViewById(R.id.editTextdob);
        parentNameEdit = view.findViewById(R.id.editTextParentName);
        parentEmailEdit = view.findViewById(R.id.editTextParentEmail);
        genderGroup = view.findViewById(R.id.radGroupGender);
        registerButton = view.findViewById(R.id.btnRegister);
        uploadButton = view.findViewById(R.id.btnUpload);
        profilePic = view.findViewById(R.id.picProfile);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all the values
                String firstName = firstNameEdit.getText().toString().trim();
                String lastName = lastNameEdit.getText().toString().trim();
                String dob = dobEdit.getText().toString().trim();
                String parentName = parentNameEdit.getText().toString().trim();
                String parentEmail = parentEmailEdit.getText().toString().trim();
                int selectedId = genderGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = view.findViewById(selectedId);
                String gender = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

                //profile pic
                String imagePath = currentImagePath;

                daycaredb = Room.databaseBuilder(getContext().getApplicationContext(),DaycareDatabase.class,"daycare.db").build();

                //run seperate thread to create users,parents and children
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String token =  UUID.randomUUID().toString();
                        User newUser = new User(parentName, parentEmail, "PARENT", "123", token);
                        Long userId = daycaredb.userDao().insertOneUser(newUser);

                        if (userId > 0) {
                            Parent newParent = new Parent(userId);
                            long parentId = daycaredb.parentDao().insertOneParent(newParent);

                            if (parentId > 0) {
                                Child newChild = new Child(firstName, lastName, dob, gender, imagePath, parentId);
                                daycaredb.childDao().insertOneChild(newChild);
                                // Update UI on success
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        }
                                    });
                                }
                            }
                        }

                    }
                });

            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
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
                profilePic.setImageURI(uri);
                currentImagePath = saveUriToInternalStorage(uri);
            }
        });
    }

    private String saveUriToInternalStorage(Uri imageUri) {
        try {
            Bitmap bitmap;
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P){
                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }else{
                bitmap =  MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            }
            String imageName = "childImage_" + System.currentTimeMillis() + ".png";
            return saveToInternalStorage(bitmap, imageName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String imageName) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // directory path to /data/data/peekaboo/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory, imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // compress method on the Bitmap object to write the image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }



}