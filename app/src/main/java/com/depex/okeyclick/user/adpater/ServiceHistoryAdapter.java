package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.ServiceHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.ServiceHistoryViewHolder> {

    private Context context;
    private List<ServiceHistory> serviceHistories;
    private ServiceHistoryItemClickListener serviceHistoryItemClickListener;
    public ServiceHistoryAdapter(Context context, List<ServiceHistory> serviceHistories,ServiceHistoryItemClickListener serviceHistoryItemClickListener){
        this.serviceHistoryItemClickListener=serviceHistoryItemClickListener;
        this.context=context;
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
            holder.taskKey.setText(serviceHistory.getTaskKey());
            holder.taskName.setText(serviceHistory.getTaskName());
            holder.taskDate.setText(serviceHistory.getCreatedByDate());
            holder.taskStatus.setText(getStatus(serviceHistory.getStatus()));
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
                return "Cancel";
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return serviceHistories.size();
    }

    class ServiceHistoryViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.task_key_text_view)
        TextView taskKey;
        @BindView(R.id.task_name_text_view)
        TextView taskName;
        @BindView(R.id.task_status_text_view)
        TextView taskStatus;
        @BindView(R.id.task_date_text_view)
        TextView taskDate;
        public ServiceHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}