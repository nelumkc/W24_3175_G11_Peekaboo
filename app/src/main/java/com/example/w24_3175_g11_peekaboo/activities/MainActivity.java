package com.example.w24_3175_g11_peekaboo.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.databases.DataBaseHelper;
import com.example.w24_3175_g11_peekaboo.fragments.ClassroomFragment;
import com.example.w24_3175_g11_peekaboo.fragments.HomeFragment;
import com.example.w24_3175_g11_peekaboo.fragments.MessageFragment;
import com.example.w24_3175_g11_peekaboo.fragments.MoreFragment;
import com.example.w24_3175_g11_peekaboo.fragments.ParentClassroomFragment;
import com.example.w24_3175_g11_peekaboo.fragments.ParentHomeFragment;
import com.example.w24_3175_g11_peekaboo.fragments.ParentMessageFragment;
import com.example.w24_3175_g11_peekaboo.fragments.ParentMoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    MessageFragment messageFragment = new MessageFragment();
    ClassroomFragment classroomFragment = new ClassroomFragment();
    MoreFragment moreFragment = new MoreFragment();

    ParentHomeFragment parentHomeFragment = new ParentHomeFragment();
    ParentMessageFragment parentMessageFragment = new ParentMessageFragment();
    ParentClassroomFragment parentClassroomFragment = new ParentClassroomFragment();
    ParentMoreFragment parentMoreFragment = new ParentMoreFragment();

    DataBaseHelper db;
    String userRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //replace container->frame layout with home fragment
        db = new DataBaseHelper(this);
        userRole = "PARENT"; // DEFAULT
        String userEmail = getIntent().getStringExtra("userEmail");

        if (userEmail != null) {
            String roleFromDb = db.getUserRoleByEmail(userEmail);
            if(roleFromDb!=null && !roleFromDb.isEmpty()){
               userRole = roleFromDb;
            }
        }


        if(userRole.equals("PARENT")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,parentHomeFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();
        }
        //


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.bottom_home) {
                    selectedFragment = userRole.equals("PARENT") ? parentHomeFragment : homeFragment;
                } else if (item.getItemId() == R.id.bottom_message) {
                    selectedFragment = userRole.equals("PARENT") ? parentMessageFragment : messageFragment;
                } else if (item.getItemId() == R.id.bottom_classroom) {
                    selectedFragment = userRole.equals("PARENT") ? parentClassroomFragment : classroomFragment;
                } else if (item.getItemId() == R.id.bottom_more) {
                    selectedFragment = userRole.equals("PARENT") ? parentMoreFragment : moreFragment;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                    return true;
                }
                return false;
            }
        });

    }
}