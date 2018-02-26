package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.Notification;

import java.util.List;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    List<Notification> notifications;
    Context context;
    NotificationItemClickListenter notificationItemClickListenter;
    public NotificationAdapter(Context context, List<Notification> notifications, NotificationItemClickListenter notificationItemClickListenter){
        this.context=context;
        this.notifications=notifications;
        this.notificationItemClickListenter=notificationItemClickListenter;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.notification_recycler_layout, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification=notifications.get(position);
        String notiyDate=notification.getNotifyDate();
        String notifyTitle=notification.getNotifyTitle();
        String notifyMsg=notification.getNotifyMsg();
        holder.notificationTitle.setText(notifyTitle);
        holder.notificationMsg.setText(notifyMsg);
        holder.notification_date.setText(notiyDate);
        final int i=position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationItemClickListenter.onNotificationItemClick(notifications.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView notificationTitle;
        TextView notificationMsg;
        TextView notification_date;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            notificationTitle=itemView.findViewById(R.id.notification_title_text);
            notificationMsg=itemView.findViewById(R.id.notification_msg_text);
            notification_date=itemView.findViewById(R.id.notification_date_text);
        }
    }
}