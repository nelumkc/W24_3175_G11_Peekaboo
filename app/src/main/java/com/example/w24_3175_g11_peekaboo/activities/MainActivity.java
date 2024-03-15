package com.example.w24_3175_g11_peekaboo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.w24_3175_g11_peekaboo.fragments.ClassroomFragment;
import com.example.w24_3175_g11_peekaboo.fragments.HomeFragment;
import com.example.w24_3175_g11_peekaboo.fragments.MessageFragment;
import com.example.w24_3175_g11_peekaboo.fragments.MoreFragment;
import com.example.w24_3175_g11_peekaboo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    MessageFragment messageFragment = new MessageFragment();
    ClassroomFragment classroomFragment = new ClassroomFragment();
    MoreFragment moreFragment = new MoreFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //replace container->frame layout with home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.getItemId() == R.id.bottom_home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.bottom_message) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,messageFragment).commit();
                    return true;
                }else if (item.getItemId() == R.id.bottom_classroom){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,classroomFragment).commit();
                    return true;
                }else if (item.getItemId() == R.id.bottom_more) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, moreFragment).commit();
                    return true;
                }
                return false;
            }
        });

    }
}