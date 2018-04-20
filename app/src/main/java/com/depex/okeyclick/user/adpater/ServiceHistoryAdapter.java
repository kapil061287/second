package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.ServiceHistory;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.ServiceHistoryViewHolder> {

    private Context context;
    private List<ServiceHistory> serviceHistories;
    private ServiceHistoryItemClickListener serviceHistoryItemClickListener;
    public ServiceHistoryAdapter(Context context, List<ServiceHistory> serviceHistories,ServiceHistoryItemClickListener serviceHistoryItemClickListener){
        this.serviceHistoryItemClickListener=serviceHistoryItemClickListener;
        this.context=context;
        Collections.sort(serviceHistories, new ServiceHistory.ServiceHistoryComparator());
        this.serviceHistories=serviceHistories;
    }

    @Override
    public ServiceHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.service_history_recycler_layout, parent, false);
        return new ServiceHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceHistoryViewHolder holder, int position) {
        final ServiceHistory serviceHistory=serviceHistories.get(position);
            GlideApp.with(context).load(serviceHistory.getCategoryIcon()).into(holder.serviceImage);
            Date date=serviceHistory.getCreatedByDate();
            SimpleDateFormat dateFormat1=new SimpleDateFormat("KK:mm a");
            String taskName="<b>"+dateFormat1.format(date)+"</b> on "+serviceHistory.getCategory();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               holder.taskName.setText(Html.fromHtml(taskName,Html.FROM_HTML_MODE_LEGACY));
            }else {
                holder.taskName.setText(Html.fromHtml(taskName));
            }


            SimpleDateFormat dateFormat=new SimpleDateFormat(context.getString(R.string.show_date_format));
            String date_create=dateFormat.format(date);
            holder.taskDate.setText(date_create);

            holder.taskStatus.setText(getStatus(serviceHistory.getStatus()));
            if(getStatus(serviceHistory.getStatus()).equalsIgnoreCase("Accepeted")){
                holder.taskStatus.setTextColor(Color.parseColor("#FFE2812C"));
            }
            if(getStatus(serviceHistory.getStatus()).equalsIgnoreCase("complete")){
                holder.taskStatus.setTextColor(Color.parseColor("#FF62B93D"));
            }else {
                holder.taskStatus.setTextColor(Color.parseColor("#FF000000"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        serviceHistoryItemClickListener.onServiceHistoryItemClickListener(serviceHistory);
                }
            });


    }

    private String getStatus(String status) {
        switch (status){
            case "1":
                return "Pending";
            case "2":
                return "Accepeted";
            case "3":
                return "Start Job Journey";
            case "4":
                return "Reached";
            case "5":
                return "Start Job";
            case "6":
                return "Inprogress";
            case "7":
                return "Complete";
            case "8":
                return "Complete";
            case "9":
                return "Cancelled";
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        if(serviceHistories!=null)
        return serviceHistories.size();
        else return 0;
    }

    class ServiceHistoryViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.sp_name_service_history)
        TextView spName;
        @BindView(R.id.task_name_text_view)
        TextView taskName;
        @BindView(R.id.task_status_text_view)
        TextView taskStatus;
        @BindView(R.id.task_date_text_view)
        TextView taskDate;
        @BindView(R.id.service_image)
        ImageView serviceImage;
        public ServiceHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}