package com.example.w24_3175_g11_peekaboo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.w24_3175_g11_peekaboo.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Using TimerTask and Timer
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Start the LoginActivity after 3000 milliseconds (3 seconds)
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 4000); // Schedule the task to run after 3000 milliseconds (3 seconds)
    }
}