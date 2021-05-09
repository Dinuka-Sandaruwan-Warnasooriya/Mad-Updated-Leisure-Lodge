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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ManagerLogin extends AppCompatActivity implements View.OnClickListener{
    Button login, gotosignup;
    FirebaseAuth mauth;
    EditText emailET, pwET;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login2);
        mauth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.login_email);
        pwET = findViewById(R.id.login_password);
        loader = findViewById(R.id.progress_loader);
        login = findViewById(R.id.btn_managerLogin);
        login.setOnClickListener(this);
        gotosignup = findViewById(R.id.btn_gotoSignUp);
        gotosignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_gotoSignUp){
            startActivity(new Intent(this, MainActivity.class));
        }else if(id == R.id.btn_managerLogin){
            signInUser();
        }
    }


    private void signInUser() {
        String email = emailET.getText().toString();
        String password = pwET.getText().toString();
        if(validateDetails(email,password)){
            loader.setVisibility(View.VISIBLE);
            mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loader.setVisibility(View.GONE);
                        Toast.makeText(ManagerLogin.this, "user logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ManagerLogin.this, AccountPage.class));
                    }else{
                        loader.setVisibility(View.GONE);
                        Toast.makeText(ManagerLogin.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
    private boolean validateDetails(String email, String password) {
        boolean valid = true;
        if(TextUtils.isEmpty(email)){
            emailET.setError("Email is required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("This must contain a valid email address");
            valid = false;
        }else{
            emailET.setError(null);
        }

        if(!isValidPassword(password)){
            pwET.setError("Password must be 7 - 24 characters long and have capital letter, simple letter and a numeric");
            valid = false;
        }else{
            pwET.setError(null);
        }
        return valid;
    }
}