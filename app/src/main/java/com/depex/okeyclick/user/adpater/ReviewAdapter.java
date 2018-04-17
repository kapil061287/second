package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we on 4/13/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    List<Review> reviews;
    Context context;
    public ReviewAdapter(Context context, List<Review> reviews){
        this.context=context;

        this.reviews=reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.content_review_layout, parent, false);
        return new ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
                Review review=reviews.get(position);
                holder.commentView.setText(review.getComment());
                holder.nameView.setText(review.getSender());
                holder.ratingBar.setRating(Float.parseFloat(review.getRating()));
                GlideApp.with(context).load(review.getSenderImage()).circleCrop().into(holder.imageView);
                holder.commentTimeLeft.setText(updateTimer(review.getCreatedDate().getTime()));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.review_pic)
        ImageView imageView;

        @BindView(R.id.name_view)
        TextView nameView;

        @BindView(R.id.comment_review)
        TextView commentView;

        @BindView(R.id.rating_bar_review)
        RatingBar ratingBar;

        @BindView(R.id.comment_time_left)
        TextView commentTimeLeft;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public String updateTimer(long millis){

        String str=new String();
        long currentTimeMillis=System.currentTimeMillis();
        long time=currentTimeMillis-millis;
        long seconds=time/1000;

        long hours=0;
        long minuts=0;
        long seconds2=0;

        long remainder=0;
        long days=seconds/(24*60*60);
        remainder=seconds%(24*60*60);
        hours=remainder/(60*60);
        remainder=remainder%(60*60);
        minuts=remainder/60;
        remainder=remainder%60;
        seconds2=remainder;

        if(days==0){
            if(hours==0){
                if(minuts==0){
                    str="Just Now";
                }else {
                    str=minuts+" min. ago";
                }
            }else {
                if(hours>1){
                    str=hours+" hours ago";
                }else {
                    str=hours+" hour ago";
                }
            }
        }else {
            if(days>1){
                str=days+" days ago";
            }else {
                str=days+" day ago";
            }
        }
        return str;
    }
}
