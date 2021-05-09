package com.example.leisurelodge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private EditText emailET, pwET, confirmPwET, phoneET, nicET, fullnameET;
    private Button btnRegister, btnGotoLogin;
    private ProgressBar loader;

    FirebaseFirestore store;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        emailET = findViewById(R.id.reg_email);
        pwET = findViewById(R.id.reg_password);
        confirmPwET = findViewById(R.id.reg_confirm_pass);
        fullnameET = findViewById(R.id.reg_fullname);
        phoneET = findViewById(R.id.reg_mobile);
        nicET = findViewById(R.id.reg_nic);
        loader = findViewById(R.id.progress_loader);
        btnGotoLogin = findViewById(R.id.btn_goto_login);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);
        btnGotoLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_goto_login){
            startActivity(new Intent(this, LoginUser.class));
        }else if(id == R.id.btn_register){
            registerUser();
        }
    }

    private void registerUser() {
        String email = emailET.getText().toString();
        final String fullname = fullnameET.getText().toString();
        String password = pwET.getText().toString();
        String confirmPass = confirmPwET.getText().toString();
        final String mobile = phoneET.getText().toString();
        final String nic = nicET.getText().toString();

        if(validateRegisterInput(email, fullname, password, confirmPass, mobile, nic)){
            loader.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("fullname", fullname);
                        userData.put("nic", nic);
                        userData.put("mobile", mobile);

                        if (user != null) {
                            store.collection("users").document(user.getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterUser.this, "user registered", Toast.LENGTH_SHORT ).show();
                                    loader.setVisibility(View.GONE);
                                    startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                }
                            });
                        }
                    }else{
                        loader.setVisibility(View.GONE);
                        Toast.makeText(RegisterUser.this, "Error while registering, Try again", Toast.LENGTH_LONG ).show();
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
    private boolean validateRegisterInput(String email, String fullname, String password, String confirmPass, String mobile, String nic) {
        boolean validity = true;

        if(TextUtils.isEmpty(email)){
            emailET.setError("Email is required");
            validity = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("This must contain a valid email address");
            validity = false;
        }else{
            emailET.setError(null);
        }
        if(!isValidPassword(password)){
            pwET.setError("Password must have a capital letter, simple letter and a numeric value and be 7 - 24 characters long");
            validity = false;
        }else if(!password.equals(confirmPass)){
            confirmPwET.setError("Password confirmation does not match");
            validity = false;
        }else{
            confirmPwET.setError(null);
            pwET.setError(null);
        }

        if(TextUtils.isEmpty(fullname)){
            fullnameET.setError("Full name cannot be empty");
            validity = false;
        }else if(fullname.length() < 6){
            fullnameET.setError("Full name must contain more than 6 characters");
            validity = false;
        }else{
            fullnameET.setError(null);
        }


        if(TextUtils.isEmpty(mobile)){
            phoneET.setError("Phone number cannot be empty");
            validity = false;
        }else if(mobile.length() < 10){
            phoneET.setError("Phone number must contain more than 10 characters");
            validity = false;
        }else{
            phoneET.setError(null);
        }

        if(TextUtils.isEmpty(nic)){
            nicET.setError("NiC number cannot be empty");
            validity = false;
        }else if(!(nic.length() == 10 || nic.length() == 12)){
            nicET.setError("Nic number only can contain 10 characters or 12");
            validity = false;
        }else{
            nicET.setError(null);
        }


        return  validity;
    }
}