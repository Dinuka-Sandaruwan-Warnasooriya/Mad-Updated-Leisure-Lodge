package extended.ui.leisurelodgehotelbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainSellerActivity extends AppCompatActivity {

    private TextView nameTv, tabOrdersTv, filteredHotelTv;
    private TextView hotelNameTv, emailTv;
    private TextView addressTv;
    private TextView tabHotelsTv;
    private EditText searchHotelEt;
    private ImageButton addhotelBtn, logoutBtn, editProfileBtn, filterHotelBtn ;
    private ImageView profileIv;
    private RelativeLayout hotelsRl, ordersRl;
    private RecyclerView hotelsRv;

    private  FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<ModelHotel> hotelList;
    private AdapterHotelSeller adapterHotelSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        nameTv = findViewById(R.id.nameTv);
        hotelNameTv = findViewById(R.id.hotelNameTv);
        addhotelBtn = findViewById(R.id.addhotelBtn);
        filterHotelBtn = findViewById(R.id.filterHotelBtn);


        emailTv = findViewById(R.id.emailTv);
        tabHotelsTv = findViewById(R.id.tabHotelsTv);
        searchHotelEt = findViewById(R.id.searchHotelEt);


        tabOrdersTv = findViewById(R.id.tabOrdersTv);
        filterHotelBtn = findViewById(R.id.filterHotelBtn);

        addressTv = findViewById(R.id.addressTv);
        logoutBtn = findViewById(R.id.logoutBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        profileIv = findViewById(R.id.profileIv);
        hotelsRl = findViewById(R.id.hotelsRl);
        ordersRl = findViewById(R.id.ordersRl);
        hotelsRv = findViewById(R.id.hotelsRv);




        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadAllHotels();

        showHotelsUI();

        searchHotelEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterHotelSeller.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMeOffline();

            }
        });

       /* logoutBtn.setOnClickListener((v) ->{
            makeMeOffline();
        });*/

        editProfileBtn.setOnClickListener((v)->{
                startActivity(new Intent(MainSellerActivity.this, ProfileEditSellerActivity.class));
        });

        addhotelBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainSellerActivity.this, AddHotelActivity.class));

            }
        });

        tabHotelsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load hotels
                showHotelsUI();

            }
        });
        tabOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load orders(bookings)
                showOrdersUI();

            }
        });
        filterHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSellerActivity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Constants.homepagesCategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selected = Constants.homepagesCategories1[which];
                                filteredHotelTv.setText(selected);
                                if (selected.equals("All")){
                                    loadAllHotels();
                                }
                                else {
                                    loadFilteredHotels(selected);
                                }
                            }
                        })
                .show();
            }
        });

    }

    private void loadFilteredHotels(String selected) {
        hotelList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Hotels")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        hotelList.clear();
                        DataSnapshot dataSnapshot = null;
                        for (DataSnapshot ds: dataSnapshot.getChildren()){

                            String hotelCategory = ""+ds.child("hotelCategory").getValue();

                            if (selected.equals(hotelCategory)){
                                ModelHotel modelHotel = ds.getValue(ModelHotel.class);
                                hotelList.add(modelHotel);
                            }

                        }
                        adapterHotelSeller = new AdapterHotelSeller(MainSellerActivity.this,hotelList);

                        hotelsRv.setAdapter(adapterHotelSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllHotels() {
        hotelList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Hotels")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        hotelList.clear();
                        DataSnapshot dataSnapshot = null;
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelHotel modelHotel = ds.getValue(ModelHotel.class);
                            hotelList.add(modelHotel);
                        }
                        adapterHotelSeller = new AdapterHotelSeller(MainSellerActivity.this,hotelList);

                        hotelsRv.setAdapter(adapterHotelSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void showHotelsUI() {
        hotelsRl.setVisibility(View.VISIBLE);
        ordersRl.setVisibility(View.GONE);

        tabHotelsTv.setTextColor(getResources().getColor(R.color.colorBlack));
        tabHotelsTv.setBackgroundResource(R.drawable.shape_rect04);

        tabOrdersTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabOrdersTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }
    private void showOrdersUI() {
        hotelsRl.setVisibility(View.GONE);
        ordersRl.setVisibility(View.VISIBLE);

        tabHotelsTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabHotelsTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabOrdersTv.setTextColor(getResources().getColor(R.color.colorBlack));
        tabOrdersTv.setBackgroundResource(R.drawable.shape_rect04);
    }



    private void makeMeOffline() {
        progressDialog.setMessage("Logging Out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener((OnSuccessListener) (aVoid)-> {
                    firebaseAuth.signOut();
                    checkUser();
        } )
                .addOnFailureListener((e)->{
                    progressDialog.dismiss();
                    Toast.makeText(MainSellerActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainSellerActivity.this,LoginActivity.class));
            finish();
        }
        else{
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot dataSnapshot = null;
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String email = ""+ds.child("email").getValue();
                            String hotelName = ""+ds.child("hotelname").getValue();
                            String address = ""+ds.child("hotelAddress").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();





                            nameTv.setText(name);
                            hotelNameTv.setText(hotelName);
                            addressTv.setText(address);
                            try{
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_hotel_gray).into(profileIv);

                            }catch (Exception e){
                                profileIv.setImageResource(R.drawable.ic_hotel_gray);

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}