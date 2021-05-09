package com.example.leisurelodge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewHotel extends AppCompatActivity implements View.OnClickListener{

    FirebaseFirestore storage;
    String id;
    TextView hotelName;
    Button viewFb, addFb;
    private ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hotel);
        id = getIntent().getStringExtra("id");
        hotelName = findViewById(R.id.hotel_name);
        storage = FirebaseFirestore.getInstance();
        loader = findViewById(R.id.progress_loader);
        viewFb = findViewById(R.id.btn_view_feedback);
        addFb = findViewById(R.id.btn_add_feedback);

        viewFb.setOnClickListener(this);
        addFb.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loader.setVisibility(View.VISIBLE);
        storage.collection("hotels").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if(task.isSuccessful()){
                     DocumentSnapshot snap = task.getResult();
                     if(snap.getString("name") != null){
                         hotelName.setText(snap.getString("name").toUpperCase());
                     }else{
                         Toast.makeText(ViewHotel.this,"No hotel for this id",Toast.LENGTH_LONG).show();
                        ViewHotel.this.finish();
                     }
                 }
                 loader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int iid = view.getId();
        if(iid == R.id.btn_add_feedback){
            Intent addFb = new Intent(this, AddFeedback.class);
            addFb.putExtra("id", id);
            startActivity(addFb);
        }else if(iid == R.id.btn_view_feedback){
            Intent addFb = new Intent(this, ViewHotelFeedbacks.class);
            addFb.putExtra("id", id);
            startActivity(addFb);
        }
    }
}