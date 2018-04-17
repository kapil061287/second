package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    List<Notification> notifications;
    Context context;
    private  String[]arr={"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
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

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-d" );
        try {
         Date dt=  format.parse(notiyDate);
         SimpleDateFormat format1=new SimpleDateFormat("d MMM, yyyy");

            holder.notification_date.setText(format1.format(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }



       /* String[]arr=notiyDate.split("-");
        int monthIndex= Integer.parseInt(arr[1]);
*/
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