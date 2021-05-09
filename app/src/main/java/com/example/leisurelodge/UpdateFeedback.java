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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class UpdateFeedback extends AppCompatActivity implements View.OnClickListener{

    String feedbackId;
    FirebaseFirestore db;
    TextView hotelNameTV;
    Button btnUpdate, btnCancel;
    RatingBar ratingBar;
    private ProgressBar loader;
    EditText commentET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_feedback);
        feedbackId = getIntent().getStringExtra("id");
        db = FirebaseFirestore.getInstance();
        ratingBar =findViewById(R.id.rating);
        hotelNameTV = findViewById(R.id.hotel_name);
        btnUpdate = findViewById(R.id.btn_update_feedback);
        btnCancel = findViewById(R.id.btn_cancel);
        commentET = findViewById(R.id.comment);
        loader = findViewById(R.id.progress_loader);

        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(feedbackId != null){
            loader.setVisibility(View.VISIBLE);
            db.collection("feedbacks").document(feedbackId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.getResult() != null){
                        commentET.setText(task.getResult().getString("comment"));
                        hotelNameTV.setText(task.getResult().getString("hotelName"));
                        ratingBar.setRating(Integer.parseInt(task.getResult().get("rating").toString()));
                    }
                    loader.setVisibility(View.GONE);
                }
            });
        }else{
            Toast.makeText(this, "No Feedback id, please select a feedback", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_update_feedback){
            updateFeedback();
        }else{
            this.finish();
        }
    }

    private boolean validateFeedback(){
        boolean valid = true;
        int val = Math.round(ratingBar.getRating());
        String comment = commentET.getText().toString();

        if(TextUtils.isEmpty(comment)){
            commentET.setError("Comment cannot be empty");
            valid = false;
        }else if(comment.length() < 10){
            commentET.setError("Comment must contain more than 10 characters");
            valid = false;
        }else{
            commentET.setError(null);
        }

        if(val <= 0){
            Toast.makeText(this, "Please give a rating", Toast.LENGTH_LONG).show();
            valid = false;
        }else if(feedbackId == null){
            Toast.makeText(this, "Please select a feedback to update", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return  valid;
    }

    private void updateFeedback() {
        if(validateFeedback()){
            loader.setVisibility(View.VISIBLE);
            int val = Math.round(ratingBar.getRating());
            String comment = commentET.getText().toString();
            Map<String, Object> feedback = new HashMap<>();
            feedback.put("rating", val);
            feedback.put("comment", comment);

            db.collection("feedbacks").document(feedbackId).update(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(UpdateFeedback.this, "Feedback updated successfully", Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                    UpdateFeedback.this.finish();

                }
            });
        }else{
            Toast.makeText(this, "Unable to update the feedback", Toast.LENGTH_LONG).show();
        }
    }
}