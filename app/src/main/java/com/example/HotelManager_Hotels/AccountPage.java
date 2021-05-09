package com.example.HotelManager_Hotels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AccountPage extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    Button btnlogout, btnBookings, btnMyAccount, btnMyHotels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnlogout = findViewById(R.id.btn_logOut);
        btnBookings  =findViewById(R.id.btn_bookings);
        btnMyAccount = findViewById(R.id.btn_account);
        btnMyHotels = findViewById(R.id.btn_hotels);


        btnBookings.setOnClickListener(this);

        btnMyHotels.setOnClickListener(this);
        btnMyAccount.setOnClickListener(this);
        btnlogout.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, com.example.hotelbookingsystem.ManagerLogin.class));
        } else {
            System.out.println("user logged in");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_logOut){
            auth.signOut();
            startActivity(new Intent(this, com.example.hotelbookingsystem.ManagerLogin.class));
        }else if(R.id.btn_hotels == id){
            startActivity(new Intent(this, MyHotels.class));
        }else if(R.id.btn_account == id){
            startActivity(new Intent(this, com.example.hotelbookingsystem.Account.class));
        }/*else if(id == R.id.btn_bookings){
            startActivity(new Intent(this, ViewBookings.class));
        }*/

    }
}