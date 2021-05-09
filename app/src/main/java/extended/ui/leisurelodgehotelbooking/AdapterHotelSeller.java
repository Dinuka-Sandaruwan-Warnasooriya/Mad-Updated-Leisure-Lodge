package extended.ui.leisurelodgehotelbooking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterHotelSeller extends RecyclerView.Adapter<AdapterHotelSeller.HolderHotelSeller>  implements Filterable {
    private Context context;
    public ArrayList<ModelHotel> hotelList, filterList;
    private FilterHotel filter;

    public AdapterHotelSeller(Context context, ArrayList<ModelHotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
        this.filterList = hotelList;
    }

    @NonNull
    @Override
    public HolderHotelSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_hotel_seller, parent, false);
        return new HolderHotelSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHotelSeller holder, int position) {

        ModelHotel modelHotel = hotelList.get(position);
        String id = modelHotel.getHotelId();
        String uid = modelHotel.getUid();
        String discountAvailable = modelHotel.getDiscountAvailable();
        String discountNote = modelHotel.getDiscountNote();
        String discountPrice = modelHotel.getDiscountPrice();
        String hotelCategory = modelHotel.getHotelCategory();
        String hotelDescription = modelHotel.getHotelDescription();
        String icon = modelHotel.getHotelIcon();
        String quantity = modelHotel.getHotelQuantity();
        String title = modelHotel.getHotelTitle();
        String timestamp = modelHotel.getTimestamp();
        String originalPrice = modelHotel.getOriginalPrice();


        holder.titleTv.setText(title);
        holder.quantityTv.setText(quantity);
        holder.discountedNoteTv.setText(discountNote);
        holder.discountedPriceTv.setText("$"+discountPrice);
        holder.originalPriceTv.setText("$"+originalPrice);
        if (discountAvailable.equals("true")){
            holder.discountedPriceTv.setVisibility(View.VISIBLE);
            holder.discountedNoteTv.setVisibility(View.VISIBLE);
            holder.originalPriceTv.setPaintFlags(holder.originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.discountedPriceTv.setVisibility(View.GONE);
            holder.discountedNoteTv.setVisibility(View.GONE);

        }
        try {
            Picasso.get().load(icon).placeholder(R.drawable.ic_add_business_primary).into(holder.hotelIconIv);
        }catch (Exception e){
            holder.hotelIconIv.setImageResource(R.drawable.ic_add_business_primary);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBottomSheet(modelHotel);
            }
        });


    }

    private void detailsBottomSheet(ModelHotel modelHotel) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bs_hotel_details_seller,null);
        bottomSheetDialog.setContentView(view);



        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageView hotelIconIv = view.findViewById(R.id.hotelIconIv);
        TextView discountNoteTv = view.findViewById(R.id.discountNoteTv);
        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView categoryTv = view.findViewById(R.id.categoryTv);
        TextView quantityTv = view.findViewById(R.id.quantityTv);
        TextView discountedPriceTv = view.findViewById(R.id.discountedPriceTv);
        TextView originalPriceTv = view.findViewById(R.id.originalPriceTv);

        String id = modelHotel.getHotelId();
        String uid = modelHotel.getUid();
        String discountAvailable = modelHotel.getDiscountAvailable();
        String discountNote = modelHotel.getDiscountNote();
        String discountPrice = modelHotel.getDiscountPrice();
        String hotelCategory = modelHotel.getHotelCategory();
        String hotelDescription = modelHotel.getHotelDescription();
        String icon = modelHotel.getHotelIcon();
        String quantity = modelHotel.getHotelQuantity();
        String title = modelHotel.getHotelTitle();
        String timestamp = modelHotel.getTimestamp();
        String originalPrice = modelHotel.getOriginalPrice();

        titleTv.setText(title);
        descriptionTv.setText(hotelDescription);
        categoryTv.setText(hotelCategory);
        quantityTv.setText(quantity);
        discountNoteTv.setText(discountNote);
        discountedPriceTv.setText("$"+discountPrice);
        originalPriceTv.setText("$"+originalPrice);
        if (discountAvailable.equals("true")){
            discountedPriceTv.setVisibility(View.VISIBLE);
            discountNoteTv.setVisibility(View.VISIBLE);
            originalPriceTv.setPaintFlags(originalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            discountedPriceTv.setVisibility(View.GONE);
            discountNoteTv.setVisibility(View.GONE);

        }
        try {
            Picasso.get().load(icon).placeholder(R.drawable.ic_add_business_primary).into(hotelIconIv);
        }catch (Exception e){
            hotelIconIv.setImageResource(R.drawable.ic_add_business_primary);
        }
        bottomSheetDialog.show();
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(context, EditHotelActivity.class);
                intent.putExtra("hotelId", id);
                context.startActivity(intent);

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sere you want to delete hotel"+title+"?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteHotel(id);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });




    }

    private void deleteHotel(String id) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Product deleted...", Toast.LENGTH_SHORT).show();
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterHotel(this,filterList);
        }
        return filter;
    }

    class HolderHotelSeller extends RecyclerView.ViewHolder{

        private ImageView hotelIconIv;
        private TextView discountedNoteTv, titleTv, quantityTv, discountedPriceTv, originalPriceTv;

        public HolderHotelSeller(@NonNull View itemView) {
            super(itemView);

            hotelIconIv = itemView.findViewById(R.id.hotelIconIv);
            discountedNoteTv = itemView.findViewById(R.id.discountedNoteTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            discountedPriceTv = itemView.findViewById(R.id.discountedPriceTv);
            originalPriceTv = itemView.findViewById(R.id.originalPriceTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);




        }
    }
}
