package com.depex.okeyclick.user.adpater;



import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.fragment.SubServiceFragment2;
import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.fragment.SubServiceFragment;

import java.util.List;


public class ServicesRecyclerAdapter  extends RecyclerView.Adapter<ServicesRecyclerAdapter.ServiceRecyclerViewHolder>{

    private List<Service> services;
    Context context;
    FragmentManager fragmentManager;
    RecyclerView.LayoutManager manager;

    public ServicesRecyclerAdapter(Context context, List<Service> services, FragmentManager fragmentManager, RecyclerView.LayoutManager manager){
        this.context=context;
        this.services=services;
        this.fragmentManager=fragmentManager;
        this.manager= manager;
    }

    @Override
    public ServiceRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.service_recycler_layout, parent, false);
        return new ServiceRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceRecyclerViewHolder holder, int position) {
        Service service=services.get(position);
        holder.itemView.setTag(service);
        String name=service.getServiceName();
        String url=service.getImageUrl();

        if(holder.serviceName==null){
            Toast.makeText(context, "Null is ", Toast.LENGTH_LONG).show();
            return;
        }
        holder.serviceName.setText(name.toUpperCase());

        if(holder.serviceImg==null){
            return;
        }

        holder.serviceImg.setAdjustViewBounds(true);
        if(manager instanceof GridLayoutManager)
            holder.serviceImg.setScaleType(ImageView.ScaleType.MATRIX);
        else
            holder.serviceImg.setScaleType(ImageView.ScaleType.CENTER_CROP);


        GlideApp.with(context)
                .load(Utils.IMAGE_URL+url)
                //.centerCrop()
                .into(holder.serviceImg);
    }


    @Override
    public int getItemCount() {
        return services.size();
    }

    public  class ServiceRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView serviceName;
        ImageView serviceImg;
        View itemView;

        public ServiceRecyclerViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            serviceName=itemView.findViewById(R.id.service_name);
            serviceImg=itemView.findViewById(R.id.service_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position=services.indexOf(view.getTag());

            SubServiceFragment2 serviceFragment2=SubServiceFragment2.newInstance(services, position);
            serviceFragment2.setHasOptionsMenu(true);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_container, serviceFragment2, "service")
                    .addToBackStack(null)
                    .commit();
        }
    }
}