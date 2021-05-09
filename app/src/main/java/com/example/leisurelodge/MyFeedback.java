package com.example.leisurelodge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyFeedback extends AppCompatActivity {
    private RecyclerView feedbackRecyclerView;
    private RecyclerView.Adapter fAdapter;
    private RecyclerView.LayoutManager fLayoutManager;
    ArrayList<Feedback> feedbacks = new ArrayList<>();
    private FirebaseFirestore db;
   FirebaseUser user;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feedback);
        db = FirebaseFirestore.getInstance();
        feedbackRecyclerView = findViewById(R.id.product_recyclerView);
        feedbackRecyclerView.setHasFixedSize(true);
        fLayoutManager = new LinearLayoutManager(this);
        loader = findViewById(R.id.progress_loader);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMyFeedback();
    }

    private void loadMyFeedback() {
        feedbacks.clear();
        loader.setVisibility(View.VISIBLE);
        db.collection("feedbacks").whereEqualTo("userId", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Feedback temp = new Feedback(document.getId(), document.getString("hotelName"), document.getString("hotelId"), document.getString("userId"), document.getString("userName"), Integer.parseInt(String.valueOf(document.get("rating"))), document.getString("comment"));
                            feedbacks.add(temp);
                        }
                        fAdapter = new FeedbackAdapter(feedbacks);
                        feedbackRecyclerView.setLayoutManager(fLayoutManager);
                        feedbackRecyclerView.setAdapter(fAdapter);
                    } else {
                        Toast.makeText(MyFeedback.this, "No feedbacks yet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MyFeedback.this, "Cannot load feedbacks, check your internet", Toast.LENGTH_LONG).show();
                }
                loader.setVisibility(View.GONE);
            }
        });
    }
}