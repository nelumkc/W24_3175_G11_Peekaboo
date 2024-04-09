package com.example.w24_3175_g11_peekaboo.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Index;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.activities.LiveActivity;
import com.example.w24_3175_g11_peekaboo.activities.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.UUID;


public class ParentMoreFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    Button btnStartNewLive;
    TextInputEditText editLiveId;

    String userId;
    String liveId="";
    PaymentButtonContainer paymentButtonContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_more, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.btnLogout);
        Button directionButton = view.findViewById(R.id.btnDaycareDirection);
        paymentButtonContainer = view.findViewById(R.id.payment_button_container);

        btnStartNewLive = view.findViewById(R.id.btnStartLive);
        editLiveId = view.findViewById(R.id.edtLiveId);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                clearRememberMePreference();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                if(getActivity()!=null){
                    getActivity().finish();
                }

            }
        });

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container, new GoogleMapFragment());
                    transaction.addToBackStack(null); // Add the transaction to the back stack
                    transaction.commit();
                }

        });



        btnStartNewLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveId = editLiveId.getText().toString();
                if(liveId.isEmpty()){
                    editLiveId.setError("Live Id is Required");
                    editLiveId.requestFocus();
                    return;
                }
                startMeeting();
            }
        });

        //payment
        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.CAD)
                                                        .value("10.00")
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                String transactionId = "10001";
                                double amount = 10;
                                String currency = "CAD";
                                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }




        );


        return view;
    }

    private void clearRememberMePreference() {
        SharedPreferences preferences = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(LoginActivity.PREF_REMEMBER_ME);
        editor.apply();
    }

    void startMeeting(){
         userId = UUID.randomUUID().toString();
            Intent intent = new Intent(getActivity(), LiveActivity.class);
            intent.putExtra("user_id",userId);
            intent.putExtra("name","Parent");
            intent.putExtra("live_id",liveId);
            intent.putExtra("host",false);
            startActivity(intent);
    }
}