package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;


public class NewMessageFragment extends Fragment {

    private EditText editTextSubject, editTextMessage;
    private Button buttonSend, btnChooseFile;
    private static final int PICK_FILE_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_message, container, false);

        editTextSubject = view.findViewById(R.id.editTextSubject);
        editTextMessage = view.findViewById(R.id.editTextMsgBody);
        buttonSend = view.findViewById(R.id.BtnSend);
        btnChooseFile = view.findViewById(R.id.btnChooseFile);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = editTextSubject.getText().toString();
                String message = editTextMessage.getText().toString();

                sendMessage(subject, message);
            }
        });


        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    //startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST_CODE);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void sendMessage(String subject, String message) {
        Toast.makeText(getActivity(), "Message Sent\nSubject: " + subject + "\nMessage: " + message, Toast.LENGTH_LONG).show();

        editTextSubject.setText("");
        editTextMessage.setText("");

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}