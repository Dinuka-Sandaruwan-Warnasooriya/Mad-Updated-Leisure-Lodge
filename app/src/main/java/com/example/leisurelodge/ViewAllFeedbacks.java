package com.example.leisurelodge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewAllFeedbacks extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView feedbackRecyclerView;
    private RecyclerView.Adapter fAdapter;
    private RecyclerView.LayoutManager fLayoutManager;
    ArrayList<Feedback> feedbacks = new ArrayList<>();
    private FirebaseFirestore db;
    Button searchBtn;
    EditText searchText;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_feedbacks);
        db = FirebaseFirestore.getInstance();
        feedbackRecyclerView = findViewById(R.id.product_recyclerView);
        feedbackRecyclerView.setHasFixedSize(true);
        fLayoutManager = new LinearLayoutManager(this);
        loader = findViewById(R.id.progress_loader);
        searchText = findViewById(R.id.search_tv);
        searchBtn = findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAllFeedbacks();
    }

    private void loadAllFeedbacks() {
        feedbacks.clear();
        loader.setVisibility(View.VISIBLE);
        db.collection("feedbacks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        Toast.makeText(ViewAllFeedbacks.this, "No feedbacks yet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ViewAllFeedbacks.this, "Cannot load feedbacks, check your internet", Toast.LENGTH_LONG).show();
                }
                loader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.search_btn) {
            searchFeedbacks();
        }
    }

    private void searchFeedbacks() {
        View view = this.getCurrentFocus();
        this.searchText.clearFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        final String searchText = this.searchText.getText().toString();
        if (searchText.isEmpty()) {
            loadAllFeedbacks();
        } else {
            feedbacks.clear();
            loader.setVisibility(View.VISIBLE);
            db.collection("feedbacks").orderBy("hotelName").startAt(searchText).endAt(searchText + "\uf8ff").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                            Toast.makeText(ViewAllFeedbacks.this, "No feedbacks for " + searchText, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ViewAllFeedbacks.this, "Cannot load feedbacks, check your internet", Toast.LENGTH_LONG).show();
                    }
                    loader.setVisibility(View.GONE);
                }
            });
        }
    }
}