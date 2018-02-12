package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.model.UserPackage;
import com.depex.okeyclick.user.R;

import com.depex.okeyclick.user.listener.PackageClickListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;



public class PackageRecyclerAdapter extends RecyclerView.Adapter<PackageRecyclerAdapter.PackageRecyclerViewHolder> {

    private  List<UserPackage> packages;
    private Context context;
    private View currentView;
    boolean firstElement;
    private View lastView;
    private PackageClickListener packageClickListener;


    public PackageRecyclerAdapter(Context context, List<UserPackage> packages, PackageClickListener packageClickListener){
        this.packages=packages;
        this.context=context;
        this.packageClickListener=packageClickListener;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        setLastView(this.currentView);
        if(currentView==null)return;
        this.currentView=currentView;
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



        GlideApp.with(context).load(packageImageUrl).into(holder.imageView);
        holder.textView1.setText(name);
        holder.textView2.setText(packagePrice);
        int screenWidth=context.getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams lp=holder.itemView.getLayoutParams();
        lp.width=screenWidth/3;
        /*if(!firstElement){
            packageClickListener.onPackageClick(packages.get(position));
            setCurrentView(holder.itemView);
            firstElement=true;
        }*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentView(view);
                packageClickListener.onPackageClick(packages.get(position));
            }
        });
      // Log.i("layoutWidth" , "View Width : "+lp.width);
       // Log.i("layoutWidth" , "Screen Width : "+screenWidth);
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }


    public class PackageRecyclerViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView imageView;
        TextView textView1;
        TextView textView2;
        View itemView;
        public PackageRecyclerViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            imageView=itemView.findViewById(R.id.userpackge_recycler_img);
            textView1=itemView.findViewById(R.id.package_name_recycler_txt);
            textView2=itemView.findViewById(R.id.package_price_recycler_txt);
        }
    }
}