package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.model.SubService;
import com.depex.okeyclick.user.view.OkeyClickTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsServiceAdapter extends RecyclerView.Adapter<DetailsServiceAdapter.DetailsViewHolder> {
    private List<Service> services;
    private Context context;
    public DetailsServiceAdapter(Context context, List<Service> services){
        this.context=context;
        this.services=services;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.content_details_service_rec, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        Service service=services.get(position);
        List<SubService> subServices=service.getSubServices();
        holder.serviceNameDetails.setText(service.getServiceName());
        OkeyClickTextView textView=new OkeyClickTextView(context, R.layout.okey_click_text_view);
        for(SubService subService : subServices) {
            View view=textView.getView(holder.subServiceContainer, subService.getSubServiceName());
            holder.subServiceContainer.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        if(services!=null)
        return services.size();
        else return 0;
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.subservice_container)
        LinearLayout subServiceContainer;

        @BindView(R.id.service_name_details)
        TextView serviceNameDetails;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
