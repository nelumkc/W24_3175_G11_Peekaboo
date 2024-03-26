package com.example.w24_3175_g11_peekaboo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.w24_3175_g11_peekaboo.R;

public class paymentFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        EditText editTextpayAmt = view.findViewById(R.id.editTextPaymentAmount);
        EditText editTextpayName = view.findViewById(R.id.editTextNameOnCard);
        EditText editTextcardNo = view.findViewById(R.id.editTextCardNumber);
        Button btnPay = view.findViewById(R.id.buttonPay);

        return view;

    }
}
