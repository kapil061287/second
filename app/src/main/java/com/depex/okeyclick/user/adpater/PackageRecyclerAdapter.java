package com.depex.okeyclick.user.adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.model.UserPackage;
import com.depex.okeyclick.user.R;

import com.depex.okeyclick.user.listener.PackageClickListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PackageRecyclerAdapter extends RecyclerView.Adapter<PackageRecyclerAdapter.PackageRecyclerViewHolder> {

    private  List<UserPackage> packages;
    private Context context;
    private View currentView;
    boolean firstElement;
    private View lastView;
    private PackageClickListener packageClickListener;
    boolean isBookLater;


    public PackageRecyclerAdapter(Context context, List<UserPackage> packages, PackageClickListener packageClickListener, boolean isBookLater){
        this.packages=packages;
        this.context=context;
        this.packageClickListener=packageClickListener;
        this.isBookLater=isBookLater;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        setLastView(this.currentView);
        if(currentView==null)return;
        if(isBookLater){
            this.currentView=currentView;
            TextView textView= (TextView) currentView;
            textView.setBackground(context.getResources().getDrawable(R.drawable.package_round));
            textView.setTextColor(Color.parseColor("#FFFFFFFF"));
         return;
        }
        //this.currentView=currentView;
        this.currentView= (View) currentView.getParent();
        TextView nameText=this.currentView.findViewById(R.id.package_name_recycler_txt);
        TextView priceText=this.currentView.findViewById(R.id.package_price_recycler_txt);
        nameText.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        priceText.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        RoundedImageView imageView=this.currentView.findViewById(R.id.userpackge_recycler_img);
        imageView.setBackgroundColor(Color.parseColor("#dce1ea"));
        //this.currentView.setBackgroundColor(Color.parseColor("#dce1ea"));
    }

    public View getLastView() {
        return lastView;
    }

    public void setLastView(View lastView) {
        if(lastView==null)return;

        if(isBookLater){
            this.lastView = lastView;
            TextView textView= (TextView) currentView;
            textView.setBackground(context.getResources().getDrawable(R.drawable.book_now_round));
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            return;
        }
        this.lastView = lastView;
        this.lastView.setBackgroundColor(Color.WHITE);
        TextView priceText=this.lastView.findViewById(R.id.package_price_recycler_txt);
        TextView nameText=this.lastView.findViewById(R.id.package_name_recycler_txt);
        RoundedImageView imageView=this.currentView.findViewById(R.id.userpackge_recycler_img);
        imageView.setBackgroundColor(Color.WHITE);
        priceText.setTextColor(Color.BLACK);
        nameText.setTextColor(Color.BLACK);
    }




    @Override
    public PackageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.userpackages_recycler_layout, parent, false);
        //Log.i("layoutWidth" , "View Width : "+view.getWidth());
        //int screenWidth=context.getResources().getDisplayMetrics().widthPixels;
        //Log.i("layoutWidth" , "Screen Width : "+screenWidth);
        return new PackageRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PackageRecyclerViewHolder holder, final int position) {

            UserPackage userPackage=packages.get(position);
            String name=userPackage.getPackageName();
            String packageImageUrl=userPackage.getPackageImage();
            String packagePrice=userPackage.getPackagePricePerHr();

if(isBookLater){
    holder.imageView.setVisibility(View.GONE);
    holder.infoImage.setVisibility(View.GONE);
    holder.textView2.setVisibility(View.GONE);

    holder.textView1.setBackground(context.getResources().getDrawable(R.drawable.book_now_round));
    holder.textView1.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

    holder.textView1.setText(name);
    holder.textView1.getLayoutParams().width= LinearLayout.LayoutParams.MATCH_PARENT;
    holder.textView1.getLayoutParams().height= 80;
    if(position==0){
        setCurrentView(holder.textView1);
        packageClickListener.onPackageLongClick(packages.get(position));
    }
    int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
    lp.width = screenWidth / 3;

    holder.textView1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setCurrentView(view);
            packageClickListener.onPackageClick(packages.get(position));
        }
    });
}else {

    if(position==0){
        setCurrentView(holder.imageView);
        packageClickListener.onPackageClick(packages.get(position));
    }

    GlideApp.with(context).load(packageImageUrl).into(holder.imageView);
    holder.textView1.setText(name);
    holder.textView2.setText(context.getString(R.string.uro) + packagePrice);
    int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
    lp.width = screenWidth / 3;
    GlideApp.with(context).load(R.drawable.ic_info_black_24dp).into(holder.infoImage);
    holder.infoImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            packageClickListener.onInfoClick(packages.get(position));
        }
    });
        /*if(!firstElement){
            packageClickListener.onPackageClick(packages.get(position));
            setCurrentView(holder.itemView);
            firstElement=true;
        }*/


    holder.imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setCurrentView(view);
            packageClickListener.onPackageClick(packages.get(position));
        }
    });
    holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            packageClickListener.onPackageLongClick(packages.get(position));
            return true;
        }
    });
}
      // Log.i("layoutWidth" , "View Width : "+lp.width);
       // Log.i("layoutWidth" , "Screen Width : "+screenWidth);
    }



    @Override
    public int getItemCount() {
        return packages.size();
    }


    public class PackageRecyclerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.userpackge_recycler_img)
        RoundedImageView imageView;

        @BindView(R.id.package_name_recycler_txt)
        TextView textView1;

        @BindView(R.id.package_price_recycler_txt)
        TextView textView2;

        @BindView(R.id.info_ico)
        ImageView infoImage;

        public PackageRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}