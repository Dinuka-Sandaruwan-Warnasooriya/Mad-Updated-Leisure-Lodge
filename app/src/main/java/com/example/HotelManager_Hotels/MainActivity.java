package com.example.HotelManager_Hotels;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailET, pwET, confirmPwET, usernameET;
    private Button btnRegister, btnGotoLogin;
    private ProgressBar loader;
    FirebaseFirestore store = FirebaseFirestore.getInstance();
    //FirebaseFirestore store;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAuth mAuth;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_manager);

        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        emailET = findViewById(R.id.signup_email);
        pwET = findViewById(R.id.signup_password);
        confirmPwET = findViewById(R.id.signup_confirm_pass);
        usernameET = findViewById(R.id.signup_username);

        loader = findViewById(R.id.progress_loader);
        btnGotoLogin = findViewById(R.id.btn_gotoLogin);
        btnRegister = findViewById(R.id.btn_signUp);

        btnRegister.setOnClickListener(this);
        btnGotoLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_gotoLogin) {
            startActivity(new Intent(this, com.example.hotelbookingsystem.ManagerLogin.class));
        } else if (id == R.id.btn_signUp) {
            registerUser();
        }
    }

    private void registerUser() {
        String email = emailET.getText().toString();
        final String username = usernameET.getText().toString();
        String password = pwET.getText().toString();
        String confirmPass = confirmPwET.getText().toString();


        if (validateRegisterInput(email, username, password, confirmPass)) {
            loader.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", username);


                        if (user != null) {
                            store.collection("users").document(user.getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "user registered", Toast.LENGTH_SHORT).show();
                                    loader.setVisibility(View.GONE);
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                                }
                            });
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Error while registering, Try again", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    public static boolean isValidPassword(String password) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{7,24}");

        return !TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean validateRegisterInput(String email, String fullname, String password, String confirmPass) {
        boolean validity = true;

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Email is required");
            validity = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("This must contain a valid email address");
            validity = false;
        } else {
            emailET.setError(null);
        }
        if (!isValidPassword(password)) {
            pwET.setError("Password must have a capital letter, simple letter and a numeric value and be 7 - 24 characters long");
            validity = false;
        } else if (!password.equals(confirmPass)) {
            confirmPwET.setError("Password confirmation does not match");
            validity = false;
        } else {
            confirmPwET.setError(null);
            pwET.setError(null);
        }

        if (TextUtils.isEmpty(fullname)) {
            usernameET.setError("Username cannot be empty");
            validity = false;
        } else if (fullname.length() < 6) {
            usernameET.setError("Username must contain more than 6 characters");
            validity = false;
        } else {
            usernameET.setError(null);
        }


        return validity;
    }
}