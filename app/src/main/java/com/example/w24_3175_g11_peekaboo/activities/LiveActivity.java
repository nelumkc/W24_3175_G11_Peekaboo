package com.example.w24_3175_g11_peekaboo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.utils.Constant;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig;
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment;

public class LiveActivity extends AppCompatActivity {

    String userId,name,liveID;

    boolean isHost;

    TextView txtLiveId;
    ImageView btnShare;

    TextView textLiveId;

    private static final int PERMISSION_REQ_ID = 1001;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        if (!checkPermissions()) {
            requestPermissions();
        }

        txtLiveId = findViewById(R.id.txtLive);
        btnShare = findViewById(R.id.btnShare);


        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        liveID = getIntent().getStringExtra("live_id");
        isHost = getIntent().getBooleanExtra("host",false);

        txtLiveId.setText(liveID);
        addFragment();

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Join my Live" + liveID);
                startActivity(Intent.createChooser(intent,"Share via"));
            }
        });
    }

    void addFragment(){
        Toast.makeText(this, "Host"+ isHost, Toast.LENGTH_SHORT).show();
        ZegoUIKitPrebuiltLiveStreamingConfig config;
        if(isHost){
            config = ZegoUIKitPrebuiltLiveStreamingConfig.host();
        }else{
            config = ZegoUIKitPrebuiltLiveStreamingConfig.audience();
        }

        ZegoUIKitPrebuiltLiveStreamingFragment fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
                Constant.appId,Constant.appSign,userId,name,liveID,config        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.liveContainer,fragment)
                .commitNow();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQ_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "All permissions are required for the app to function", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            // Permissions are granted. You can initialize components that require permissions here
        }
    }

    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}