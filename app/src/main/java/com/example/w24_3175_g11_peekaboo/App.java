package com.example.w24_3175_g11_peekaboo;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.pyplcheckout.BuildConfig;

public class App extends Application {
    String YOUR_CLIENT_ID = "AXRGOZ5DikmQ6AHlCQiepYr4fAUmxtTJHp5Q8_p7HETFx8pHp70FSPyqNi6rt7uf0cu09zs9KXuuJ1b9";
    String APP_NAME ="com.example.w24_3175_g11_peekaboo";
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                CurrencyCode.CAD,
                UserAction.PAY_NOW,
                "com.example.w243175g11peekaboo://paypalpay"
        ));
    }
}
