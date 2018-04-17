package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RatingBar;
import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.BookLaterServiceProvider;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class InnerViewPagerRecyclerViewAdapter extends RecyclerView.Adapter<InnerViewPagerRecyclerViewAdapter.InnerViewPagerRecyclerViewHolder> {

    private List<BookLaterServiceProvider> bookLaterServiceProviders;
    private Context context;
    private InnerViewPagerRecyclerItemClickListener innerViewPagerRecyclerItemClickListener;

    public InnerViewPagerRecyclerViewAdapter(List<BookLaterServiceProvider> bookLaterServiceProviders, Context context, InnerViewPagerRecyclerItemClickListener innerViewPagerRecyclerItemClickListener) {
        this.bookLaterServiceProviders = bookLaterServiceProviders;
        this.context = context;
        this.innerViewPagerRecyclerItemClickListener = innerViewPagerRecyclerItemClickListener;
    }

    @Override
    public InnerViewPagerRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.content_inner_view_pager_fragment_rec_layout, parent, false);
        return new InnerViewPagerRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InnerViewPagerRecyclerViewHolder holder, int position) {
        final BookLaterServiceProvider bookLaterServiceProvider=bookLaterServiceProviders.get(position);
            holder.nameTextView.setText(bookLaterServiceProvider.getName());
            holder.distanceTextViewInner.setText(bookLaterServiceProvider.getDistance());
            //holder.ratingBarInner.setStar(bookLaterServiceProvider.getRating());
            holder.ratingBarInner.setRating(3);
            holder.pricePerHourInner.setText(bookLaterServiceProvider.getPricePerHour());
        GlideApp.with(context)
                .load(bookLaterServiceProvider.getImageUrl())
                .placeholder(R.drawable.user_dp_place_holder)
                .circleCrop()
                .into(holder.innerImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                innerViewPagerRecyclerItemClickListener.onInnerViewPagerRecyclerItemClick(bookLaterServiceProvider);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookLaterServiceProviders.size();
    }

    class InnerViewPagerRecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name_text_view_inner)
        TextView nameTextView;

        @BindView(R.id.rating_bar_inner)
        RatingBar ratingBarInner;

        @BindView(R.id.distance_text_view_inner)
        TextView distanceTextViewInner;

        @BindView(R.id.price_per_hour_inner)
        TextView pricePerHourInner;

        @BindView(R.id.inner_view_pager_image)
        ImageView innerImageView;

        public InnerViewPagerRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ratingBarInner.setEnabled(false);

        }
    }
}