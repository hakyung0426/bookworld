package com.example.bookworld;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookreviewRvAdapter extends RecyclerView.Adapter<BookreviewRvAdapter.MyViewHolder> {
    ArrayList<Bookreview> bookreviews;
    Context context;
    String isbn;

    public BookreviewRvAdapter(Context context, ArrayList<Bookreview> bookreviews, String isbn) {
        this.context = context;
        this.bookreviews = bookreviews;
        this.isbn = isbn;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bookreview, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView1.setText(bookreviews.get(position).getReviewuser());
        holder.textView2.setText(bookreviews.get(position).getReviewdate());
        holder.textView3.setText(bookreviews.get(position).getReviewstar());
        holder.textView4.setText(bookreviews.get(position).getReviewmood());
        holder.textView5.setText(bookreviews.get(position).getReviewcontent());

        String reportimgUrl = context.getString(R.string.server_address) + "reportimg/" + bookreviews.get(position).getReviewuser() + isbn + ".jpeg" ;
        Glide.with(context).load(reportimgUrl).error(R.drawable.noimage).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return bookreviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3, textView4, textView5;
        ImageButton button;

        ImageView imageView;
        View v;

        public MyViewHolder(@NonNull View v) {
            super(v);
            textView1 = v.findViewById(R.id.review_userid);
            button = v.findViewById(R.id.go_userpage);
            textView2 = v.findViewById(R.id.review_date);
            textView3 = v.findViewById(R.id.review_star);
            textView4 = v.findViewById(R.id.review_mood);
            textView5 = v.findViewById(R.id.review_content);
            imageView = v.findViewById(R.id.review_image);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    Bookreview bookreview = bookreviews.get(pos);
                    Intent intent;
                    intent = new Intent(context, UserpageActivity.class);
                    intent.putExtra("edname", bookreview.getReviewuser());
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

        }


    }
}



class Bookreview{
    private String reviewuser, reviewdate, reviewstar, reviewmood, reviewcontent;
    public Bookreview(String reviewuser, String reviewdate, String reviewstar, String reviewmood, String reviewcontent) {
        this.reviewcontent = reviewcontent;
        this.reviewuser = reviewuser;
        this.reviewdate = reviewdate;
        this.reviewstar = reviewstar;
        this.reviewmood = reviewmood;
    }

    public String getReviewcontent() {
        return reviewcontent;
    }
    public String getReviewuser() {
        return reviewuser;
    }
    public String getReviewdate() {
        return reviewdate;
    }
    public String getReviewmood() {
        return reviewmood;
    }
    public String getReviewstar() {
        return reviewstar;
    }
}