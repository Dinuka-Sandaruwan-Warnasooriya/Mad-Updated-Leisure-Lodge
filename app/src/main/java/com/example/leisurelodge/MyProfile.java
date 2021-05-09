package com.example.leisurelodge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfile extends AppCompatActivity implements View.OnClickListener{

    FirebaseUser user;
    FirebaseFirestore store;
    TextView emailView, nameView, phoneView, nicView;
    ImageView proPic;
    Button logoutBtn, deleteUserBtn, updateUserBtn;
    FirebaseAuth auth;
    ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        store = FirebaseFirestore.getInstance();
        emailView = findViewById(R.id.my_email);
        nameView = findViewById(R.id.my_name);
        phoneView = findViewById(R.id.my_phone);
        nicView = findViewById(R.id.nic);
        proPic = findViewById(R.id.profilePic);
        logoutBtn = findViewById(R.id.my_logout);
        updateUserBtn = findViewById(R.id.goto_update);
        deleteUserBtn = findViewById(R.id.delete_user_btn);
        loader = findViewById(R.id.progress_loader);
        updateUserBtn.setOnClickListener(this);
        deleteUserBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loader.setVisibility(View.VISIBLE);
        store.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if (snap != null) {
                        emailView.setText(user.getEmail());
                        nameView.setText(snap.getString("fullname"));
                        phoneView.setText(snap.getString("mobile"));
                        nicView.setText(snap.getString("nic"));

                        if (snap.getString("image") != null && !snap.getString("image").isEmpty()) {
                            Picasso.get()
                                    .load(snap.getString("image"))
                                    .resize(1000, 1000)
                                    .into(proPic, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Bitmap imageBitmap = ((BitmapDrawable) proPic.getDrawable()).getBitmap();
                                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                            imageDrawable.setCircular(true);
                                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                            proPic.setImageDrawable(imageDrawable);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            proPic.setImageResource(R.drawable.logox);
                                        }
                                    });
                        }
                    }
                }
                loader.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.delete_user_btn) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete account")
                    .setMessage("Are you sure you want to delete this account? All your data will be deleted")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_menu_delete)
                    .show();
        } else if (id == R.id.my_logout) {
            auth.signOut();
            startActivity(new Intent(this, LoginUser.class));
        }else if(id == R.id.goto_update){
            startActivity(new Intent(this, UpdateProfile.class));
        }
    }

    private void deleteUser() {

        loader.setVisibility(View.VISIBLE);
        final String uid = user.getUid();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    store.collection("feedbacks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.getString("userId").equals(uid)) {
                                    doc.getReference().delete();
                                }
                            }
                        }
                    });
                    store.collection("users").document(uid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MyProfile.this, "User info deleted", Toast.LENGTH_LONG).show();
                        }
                    });
                    loader.setVisibility(View.GONE);
                    Toast.makeText(MyProfile.this, "user deleted successfully", Toast.LENGTH_LONG).show();
                    auth.signOut();
                    startActivity(new Intent(MyProfile.this, LoginUser.class));
                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(MyProfile.this, "Cannot delete user", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

}