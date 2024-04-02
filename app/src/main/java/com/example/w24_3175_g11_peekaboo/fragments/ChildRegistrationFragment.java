package com.example.w24_3175_g11_peekaboo.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class ChildRegistrationFragment extends Fragment {

   EditText firstNameEdit,lastNameEdit,parentNameEdit,parentEmailEdit;
   RadioGroup genderGroup;
   Button registerButton,uploadButton, dateButton;
    DaycareDatabase daycaredb;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profilePic;
    private ActivityResultLauncher<String> getContent;
    private String currentImagePath = null;

    private DatePickerDialog datePickerDialog;

    private String userpassword = "123";

   private String parentEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_registration, container, false);

        firstNameEdit = view.findViewById(R.id.editTextFirstName);
        lastNameEdit = view.findViewById(R.id.editTextLastName);
        //dobEdit = view.findViewById(R.id.editTextdob);
        parentNameEdit = view.findViewById(R.id.editTextParentName);
        parentEmailEdit = view.findViewById(R.id.editTextParentEmail);
        genderGroup = view.findViewById(R.id.radGroupGender);
        registerButton = view.findViewById(R.id.btnRegister);
        uploadButton = view.findViewById(R.id.btnUpload);
        profilePic = view.findViewById(R.id.picProfile);
        initDatePicker();
        dateButton = view.findViewById(R.id.datePickerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all the values
                String firstName = firstNameEdit.getText().toString().trim();
                String lastName = lastNameEdit.getText().toString().trim();
                //String dob = dobEdit.getText().toString().trim();
                String dob = dateButton.getText().toString().trim();
                String parentName = parentNameEdit.getText().toString().trim();
                parentEmail = parentEmailEdit.getText().toString().trim();
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
                        User newUser = new User(parentName, parentEmail, "PARENT", userpassword, token);
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
                                            //send email
                                            sendEmail();
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

        dateButton.setOnClickListener(this::openDatePicker);

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


    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(),style,dateSetListener,year,month,day);

    }

    private String makeDateString(int day, int month,int year){
        return year + "/" + month + "/" + day;
    }
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }


    private void sendEmail() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                    final String username = "nelumkc@gmail.com";
                    final String password = "rfoecjvhkjphcyeg";

                    String message = "Hi Parent,\n\n" +
                            "You have received this email because a Peekaboo account has been created for you by Peekaboo" +
                            " Childcare. To access your account please set your password.\n" +
                            "Password: " + userpassword
                            ;

                parentEmail = "kaleynk19@gmail.com";
                final String receiverEmail = parentEmail;

                    final String stringHost = "smtp.gmail.com";

                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.starttls.enable", "true");
                    properties.put("mail.smtp.host", stringHost);
                    properties.put("mail.smtp.port", "587");

                    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

                    try {
                        MimeMessage mimeMessage = new MimeMessage(session);
                        mimeMessage.setFrom(new InternetAddress(username));
                        mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(receiverEmail));

                        // Setting the subject and message content
                        mimeMessage.setSubject("Welcome to the Peekaboo App: Create your login");
                        mimeMessage.setText(message);

                        // Sending the email
                        Transport.send(mimeMessage);

                        //check fragment is still attached
                        if(isAdded()){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Sent Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }catch (MessagingException e){
                        if(isAdded()){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Error Occurred " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }

            }
        });







    }
}