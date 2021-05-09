package com.example.HotelManager_Hotels;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddHotel extends AppCompatActivity implements View.OnClickListener{
    FirebaseFirestore storage;

    TextView hotelName;
    TextView hotelAddressLine1;
    TextView hotelAddressLine2;
    TextView hotelAddressLine3;
    TextView contactNum;
    Button cancelbtn, savebtn;
    private ProgressBar loader;

    String username;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);
        user = FirebaseAuth.getInstance().getCurrentUser();
        hotelName = findViewById(R.id.hotel_name);
        storage = FirebaseFirestore.getInstance();
        loader = findViewById(R.id.progress_loader);
        savebtn = findViewById(R.id.addhotel_save);
        cancelbtn = findViewById(R.id.addhotel_cancel);
        savebtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.addhotel_cancel) {
            this.finish();
        } else if (id == R.id.addhotel_save) {
            System.out.println("Hotel Added");
            addHotel();
        }
    }

    private boolean validateFeedback() {
        boolean valid = true;



        if (hotelName == null) {
            Toast.makeText(this, "Please select a hotel", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (hotelAddressLine1 == null) {
            Toast.makeText(this, "Please enter address", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (hotelAddressLine2 == null) {
            Toast.makeText(this, "Please enter address", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if(contactNum == null){
            Toast.makeText(this, "Please enter contact number", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    private void addHotel() {
        if (validateFeedback()) {
            loader.setVisibility(View.VISIBLE);
            Map<String, Object> hotel = new HashMap<>();
            hotel.put("hotelName", hotelName);
            hotel.put("addressLine1", hotelAddressLine1);
            hotel.put("addressLine2", hotelAddressLine2);
            hotel.put("addressLine3", hotelAddressLine3);
            hotel.put("contactNumber", contactNum);
            hotel.put("userId", user.getUid());
            storage.collection("hotels").document().set(hotel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddHotel.this, "Feedback added successfully", Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                    AddHotel.this.finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddHotel.this, "Cannot add the feedback right now, Try again", Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                }
            });
        }
    }
}