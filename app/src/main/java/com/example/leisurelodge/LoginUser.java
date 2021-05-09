package com.example.leisurelodge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {
    Button login, gotoReg;
    FirebaseAuth auth;
    EditText emailET, pwET;
    private ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        auth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.login_email);
        pwET = findViewById(R.id.login_password);
        loader = findViewById(R.id.progress_loader);
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        gotoReg = findViewById(R.id.btn_goto_reg);
        gotoReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_goto_reg){
            startActivity(new Intent(this, RegisterUser.class));
        }else if(id == R.id.btn_login){
            signInUser();
        }
    }


    private void signInUser() {
        String email = emailET.getText().toString();
        String password = pwET.getText().toString();
        if(validateDetails(email,password)){
            loader.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loader.setVisibility(View.GONE);
                        Toast.makeText(LoginUser.this, "user logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginUser.this, MainActivity.class));
                    }else{
                        loader.setVisibility(View.GONE);
                        Toast.makeText(LoginUser.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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