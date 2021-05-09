package com.example.leisurelodge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{
    public ArrayList<Feedback> feedbacks;

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        public TextView hotelNameTV;
        public TextView comment;
        public TextView user;
        public ImageButton deleteBtn, editBtn;
        public RatingBar ratingBar;

        public FeedbackViewHolder(@NonNull final View itemView) {
            super(itemView);
            hotelNameTV = itemView.findViewById(R.id.rating_prd_name);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            comment = itemView.findViewById(R.id.rating_comment);
            user = itemView.findViewById(R.id.rating_user);
            deleteBtn = itemView.findViewById(R.id.rating_delete_btn);
            editBtn = itemView.findViewById(R.id.rating_update_btn);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent feedbackIntent = new Intent(view.getContext(), UpdateFeedback.class);
                    feedbackIntent.putExtra("id", feedbacks.get(getBindingAdapterPosition()).getId());
                    view.getContext().startActivity(feedbackIntent);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(view.getContext()).setTitle("Delete feedback")
                            .setMessage("Are you sure you want to delete this feedback?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("feedbacks").document(feedbacks.get(getBindingAdapterPosition()).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(itemView.getContext(),"Feedback successfully deleted", Toast.LENGTH_LONG).show();
                                            ((Activity) itemView.getContext()).finish();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(itemView.getContext(),"Can't delete the feedback right now", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                            }).setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_menu_delete)
                            .show();
                }
            });
        }

    }

    public FeedbackAdapter(ArrayList<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_card, parent, false);
        FeedbackViewHolder feedbackViewHolder = new FeedbackViewHolder(view);
        return feedbackViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbacks.get(position);

        holder.hotelNameTV.setText(feedback.getHotelName());
        holder.ratingBar.setRating(feedback.getRate());
        holder.comment.setText(feedback.getComment());
        holder.user.setText(feedback.getUserName());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(feedback.getUserId().equals(user.getUid())){
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.editBtn.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return feedbacks.size();
    }
}
