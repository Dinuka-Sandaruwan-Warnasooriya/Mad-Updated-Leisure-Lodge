package com.example.HotelManager_Hotels;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class Account extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    EditText usernameView;

    Uri filepath;
    Button cancelbtn, savebtn;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        usernameView = findViewById(R.id.account_username);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        savebtn = findViewById(R.id.account_save);
        cancelbtn = findViewById(R.id.account_cancel);
        loader = findViewById(R.id.progress_loader);
        savebtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loader.setVisibility(View.VISIBLE);
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if (snap != null) {
                        usernameView.setText(snap.getString("username"));
                    }
                }
                loader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.account_save) {
            updateProfile();
        } else if (id == R.id.account_cancel) {
            this.finish();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String username = usernameView.getText().toString();



        if (TextUtils.isEmpty(username)) {
            usernameView.setError("username cannot be empty");
            valid = false;
        } else if (username.length() < 6) {
            usernameView.setError("Username must contain more than 6 characters");
            valid = false;
        } else {
            usernameView.setError(null);
        }
        return valid;
    }

    private void updateProfile() {
        if (validateForm()) {
            String username = usernameView.getText().toString();

            loader.setVisibility(View.VISIBLE);
            final Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);

            if (filepath != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference ref = storage.getReference().child("profilephoto/user" + user.getUid());
                ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    userData.put("image", uri.toString());
                                    db.collection("users").document(user.getUid()).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Account.this, "user updated", Toast.LENGTH_LONG).show();
                                            loader.setVisibility(View.GONE);
                                            Account.this.finish();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            } else {
                db.collection("users").document(user.getUid()).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Account.this, "user updated", Toast.LENGTH_LONG).show();
                        loader.setVisibility(View.GONE);
                        Account.this.finish();
                    }
                });
            }

        } else {
            Toast.makeText(this, "Cannot update the user", Toast.LENGTH_LONG).show();
        }
    }
}