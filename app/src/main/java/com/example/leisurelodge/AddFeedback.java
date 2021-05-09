package com.example.leisurelodge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddFeedback extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore storage;
    String id;
    TextView hotelNameTV;
    Button saveFB, cancelFB;
    RatingBar ratingBar;
    private ProgressBar loader;
    EditText commentET;
    String hotelName;
    String userName;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);
        user = FirebaseAuth.getInstance().getCurrentUser();

        id = getIntent().getStringExtra("id");
        hotelNameTV = findViewById(R.id.hotel_name);
        storage = FirebaseFirestore.getInstance();
        commentET = findViewById(R.id.comment);
        loader = findViewById(R.id.progress_loader);
        saveFB = findViewById(R.id.btn_save_feedback);
        cancelFB = findViewById(R.id.btn_cancel);
        ratingBar = findViewById(R.id.rating);

        saveFB.setOnClickListener(this);
        cancelFB.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loader.setVisibility(View.VISIBLE);
        if (id != null) {
            storage.collection("hotels").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snap = task.getResult();
                        if (snap != null) {
                            hotelNameTV.setText(snap.getString("name").toUpperCase());
                            hotelName = snap.getString("name");
                        }
                    }
                    loader.setVisibility(View.GONE);
                }
            });

            if (user != null) {
                storage.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null) {
                            AddFeedback.this.userName = task.getResult().getString("fullname");
                        } else {
                            Toast.makeText(AddFeedback.this, "Can't load the user, Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "No hotel found, please select a hotel", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            this.finish();
        } else if (id == R.id.btn_save_feedback) {
            System.out.println("fsfsfdds");
            addFeedback();
        }
    }

    private boolean validateFeedback() {
        boolean valid = true;
        int val = Math.round(ratingBar.getRating());
        String comment = commentET.getText().toString();

        if (TextUtils.isEmpty(comment)) {
            commentET.setError("Comment cannot be empty");
            valid = false;
        } else if (comment.length() < 10) {
            commentET.setError("Comment must contain more than 10 characters");
            valid = false;
        } else {
            commentET.setError(null);
        }

        if (val <= 0) {
            Toast.makeText(this, "Please give a rating", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (hotelName == null) {
            Toast.makeText(this, "Please select a hotel", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (id == null) {
            Toast.makeText(this, "Please select a hotel", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if(userName == null){
            Toast.makeText(this, "Can't load the user", Toast.LENGTH_LONG).show();
            valid = false;
        }


        return valid;
    }

    private void addFeedback() {
        if (validateFeedback()) {
            loader.setVisibility(View.VISIBLE);
            int val = Math.round(ratingBar.getRating());
            String comment = commentET.getText().toString();
            Map<String, Object> feedback = new HashMap<>();
            feedback.put("rating", val);
            feedback.put("comment", comment);
            feedback.put("hotelId", id);
            feedback.put("hotelName", hotelName);
            feedback.put("userId", user.getUid());
            feedback.put("userName", userName);
            storage.collection("feedbacks").document().set(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddFeedback.this, "Feedback added successfully", Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                    AddFeedback.this.finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddFeedback.this, "Cannot add the feedback right now, Try again", Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                }
            });
        }
    }
}