package com.example.leisurelodge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    Button logoutBtn, btnH1, btnH2, btnMyProfile, btnMyFeedback, btnallFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoutBtn = findViewById(R.id.btn_logout);
        btnH1  =findViewById(R.id.btn_hotel1);
        btnH2  =findViewById(R.id.btn_hotel2);
        btnMyProfile = findViewById(R.id.btn_myprofile);
        btnMyFeedback = findViewById(R.id.btn_myfeedback);
        btnallFeedback = findViewById(R.id.btn_all_feedbacks);
        btnallFeedback.setOnClickListener(this);
        btnH1.setOnClickListener(this);
        btnH2.setOnClickListener(this);
        btnMyFeedback.setOnClickListener(this);
        btnMyProfile.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginUser.class));
        } else {
            System.out.println("user logged in");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_logout){
            auth.signOut();
            startActivity(new Intent(this, LoginUser.class));
        }else if(id == R.id.btn_hotel1){
            Intent hotel = new Intent(this, ViewHotel.class);
            hotel.putExtra("id", "Z1g8SI1FkNE995H95ZPL");
            startActivity(hotel);
        }else if(id == R.id.btn_hotel2){
            Intent hotel = new Intent(this, ViewHotel.class);
            hotel.putExtra("id", "nOJdcikb27JreyiD5KbO");
            startActivity(hotel);
        }else if(R.id.btn_myfeedback == id){
            startActivity(new Intent(this, MyFeedback.class));
        }else if(R.id.btn_myprofile == id){
            startActivity(new Intent(this, MyProfile.class));
        }else if(id == R.id.btn_all_feedbacks){
            startActivity(new Intent(this, ViewAllFeedbacks.class));
        }

    }
}