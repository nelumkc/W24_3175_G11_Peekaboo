package com.example.w24_3175_g11_peekaboo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.fragments.ChatFragment;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    MessageFragment messageFragment = new MessageFragment();
    ChatFragment chatFragment = new ChatFragment();
    ClassroomFragment classroomFragment = new ClassroomFragment();
    MoreFragment moreFragment = new MoreFragment();

    ParentHomeFragment parentHomeFragment = new ParentHomeFragment();
    ParentMessageFragment parentMessageFragment = new ParentMessageFragment();
    ParentClassroomFragment parentClassroomFragment = new ParentClassroomFragment();
    ParentMoreFragment parentMoreFragment = new ParentMoreFragment();

    DaycareDatabase daycaredb;
    String userRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //replace container->frame layout with home fragment
        daycaredb = Room.databaseBuilder(this.getApplicationContext(), DaycareDatabase.class, "daycare.db").build();

        userRole = "PARENT"; // DEFAULT
        String userEmail = getIntent().getStringExtra("userEmail");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (userEmail != null) {
                    Fragment selectedFragment;
                    String roleFromDb = daycaredb.userDao().getUserRoleByEmail(userEmail);

                    if(roleFromDb!=null && !roleFromDb.isEmpty()){
                        userRole = roleFromDb;
                        if(userRole.equals("PARENT")){
                            selectedFragment = parentHomeFragment;
                        }else{
                            selectedFragment = homeFragment;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,selectedFragment).commit();
                            }
                        });
                        executor.shutdown();
                    }
                    /*
                    if(roleFromDb == null || roleFromDb.isEmpty()){
                        // Role is empty or null, start LoginActivity
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        executor.shutdown();
                        return;
                    }
                    // Existing logic to set the userRole and update UI
                    userRole = roleFromDb;
                    if(userRole.equals("PARENT")){
                        selectedFragment = parentHomeFragment;
                    }else{
                        selectedFragment = homeFragment;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,selectedFragment).commit();
                        }
                    });
                    executor.shutdown();
*/
                }
            }
        });



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;
                String role = userRole != null ? userRole : "";
                if (item.getItemId() == R.id.bottom_home) {
                    selectedFragment = userRole.equals("PARENT") ? parentHomeFragment : homeFragment;
                } else if (item.getItemId() == R.id.bottom_message) {
                    selectedFragment = userRole.equals("PARENT") ? parentMessageFragment : chatFragment;
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