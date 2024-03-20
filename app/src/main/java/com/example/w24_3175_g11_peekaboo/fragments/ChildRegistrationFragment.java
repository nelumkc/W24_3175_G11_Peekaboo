package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

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
import com.example.w24_3175_g11_peekaboo.databases.DataBaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ChildRegistrationFragment extends Fragment {

   EditText firstNameEdit,lastNameEdit,dobEdit,parentNameEdit,parentEmailEdit;
   RadioGroup genderGroup;
   Button registerButton,uploadButton;

   DataBaseHelper db;

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

                db = new DataBaseHelper(getContext());

                String gender;
                if (selectedRadioButton != null) {
                    gender = selectedRadioButton.getText().toString();
                } else {
                    gender = "";
                }
                //profile pic
                String imagePath = currentImagePath;


                Boolean checkInsertData = db.addChildWithParent(firstName,lastName,dob,gender,currentImagePath,parentName,parentEmail);
                if(checkInsertData == true){
                    Toast.makeText(getContext(), "New Entry inserted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "New Entry not inserted", Toast.LENGTH_SHORT).show();
                }
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
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
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
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