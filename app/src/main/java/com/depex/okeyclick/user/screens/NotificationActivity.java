package com.depex.okeyclick.user.screens;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.NotificationAdapter;
import com.depex.okeyclick.user.adpater.NotificationItemClickListenter;
import com.depex.okeyclick.user.database.NotificationHelperDatabase;
import com.depex.okeyclick.user.model.Notification;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationActivity extends AppCompatActivity implements NotificationItemClickListenter{

    @BindView(R.id.notification_txt)
    TextView notificationText;
    @BindView(R.id.notification_alarm_img)
    ImageView notificationAlarmImage;
    @BindView(R.id.notification_recycler_view)
    RecyclerView notificationRecyclerView;

    Toolbar toolbar;

    NotificationHelperDatabase notificationHelperDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        toolbar=findViewById(R.id.toolbar);
        notificationHelperDatabase=new NotificationHelperDatabase(this);



        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationActivity.super.onBackPressed();
            }
        });

        initNotification();

        //TODO this code is test for ScheduleActivity
        //TODO Start
        notificationAlarmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NotificationActivity.this, ScheduleVisitActivity.class);
                //startActivity(intent);
            }
        });
        //TODO End
    }

    private void initNotification() {
        Cursor cursor=notificationHelperDatabase.getNotifications();
        List<Notification> notifications= new ArrayList<>();
        if(cursor.moveToFirst()) {

            notificationAlarmImage.setVisibility(View.GONE);
            notificationText.setVisibility(View.GONE);
            do {
                    String id=cursor.getString(0);
                    String notifyType=cursor.getString(1);
                    String notifyMsg=cursor.getString(2);
                    String notifyData=cursor.getString(3);
                    String notifyDate=cursor.getString(4);
                    if(notifyType.equalsIgnoreCase("invoice")){
                        Notification notification=new Notification();
                        notification.setNotifyDate(notifyDate);
                        notification.setNotifyData(notifyData);
                        notification.setNotifyTitle("Invoice");
                        notification.setNotifyMsg(notifyMsg);

                            notifications.add(notification);
                    }
            } while (cursor.moveToNext());

            NotificationAdapter adapter=new NotificationAdapter(this, notifications, this);
            notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            notificationRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onNotificationItemClick(Notification notification) {

    }
}