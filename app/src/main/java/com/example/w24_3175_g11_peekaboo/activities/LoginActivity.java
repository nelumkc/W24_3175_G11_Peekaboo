package com.example.w24_3175_g11_peekaboo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.w24_3175_g11_peekaboo.R;
import com.example.w24_3175_g11_peekaboo.databases.DaycareDatabase;
import com.example.w24_3175_g11_peekaboo.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    SignInButton googleSignInButton;
    EditText emailEt, passEt;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    public static final String PREFS_NAME = "sharedPref";
    public static final String PREF_REMEMBER_ME = "rememberMe";
    private static final int RC_SIGN_IN = 9001;
    private CheckBox rememberMeCheckBox;
    GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    //private DataBaseHelper db;
    private DaycareDatabase daycaredb;

    private static final String ADMIN_EMAIL = "nelumkc@gmail.com";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String ADMIN_PASSWORD = "admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // create admin when first installation
        //db = new DataBaseHelper(this);
        daycaredb = Room.databaseBuilder(this.getApplicationContext(), DaycareDatabase.class, "daycare.db").build();

        checkFirstRunAndCreateAdmin();

        //Login
        loginBtn = findViewById(R.id.btnLogin);
        emailEt = findViewById(R.id.editTextEmail);
        passEt = findViewById(R.id.editTextPassword);
        rememberMeCheckBox = findViewById(R.id.checkBoxRememberMe);
        googleSignInButton = findViewById(R.id.sign_in_button);

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (getRememberMePreference() && currentUser != null) {
            navigateToMainActivity();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logEmailPassUser(emailEt.getText().toString().trim(),
                        passEt.getText().toString().trim()
                );
            }
        });

        //Google Sign in
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            handleSignInResult(task);
                        }
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(intent);
            }
        });

    }

    private void checkFirstRunAndCreateAdmin() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (daycaredb.userDao().countUserByRole("ADMIN") == 0) {
                    User adminUser = new User("admin", ADMIN_EMAIL, ADMIN_ROLE, ADMIN_PASSWORD);
                    daycaredb.userDao().insertUserAdmin(adminUser);
                }
            }
        });
        executor.shutdown();
    }


    private void logEmailPassUser(String email, String pwd) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    User user = daycaredb.userDao().findUserByEmailAndPassword(email, pwd);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user != null) {
                                setRememberMePreference(rememberMeCheckBox.isChecked());
                                navigateToMainActivity();
                            }else {
                                Toast.makeText(LoginActivity.this,
                                        "Authentication failed. Please check your credentials.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(LoginActivity.this,
                    "Please enter both email and password.", Toast.LENGTH_SHORT).show();
        }
    }


    private void setRememberMePreference(boolean isChecked) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREF_REMEMBER_ME, isChecked);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("userEmail", emailEt.getText().toString());
        startActivity(intent);
        finish();
    }

    private boolean getRememberMePreference() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getBoolean(PREF_REMEMBER_ME, false);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                String userEmail = user.getEmail();
                                checkUserRegistration(userEmail);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error during sign in. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserRegistration(String userEmail){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int userCount = daycaredb.userDao().countUserByEmail(userEmail);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(userCount == 0){
                            Toast.makeText(LoginActivity.this, "Your email is not registered in our system. Please contact support.", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                        }else{
                            navigateToMainActivity();
                        }
                    }
                });
            }
        });
    }

}