package com.depex.okeyclick.user.screens;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.NotificationAdapter;
import com.depex.okeyclick.user.adpater.NotificationItemClickListenter;
import com.depex.okeyclick.user.database.OkeyClickDatabaseHelper;
import com.depex.okeyclick.user.model.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

    OkeyClickDatabaseHelper okeyClickDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        okeyClickDatabaseHelper =new OkeyClickDatabaseHelper(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
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
               // Intent intent=new Intent(NotificationActivity.this, ScheduleVisitActivity.class);
                //startActivity(intent);
            }
        });
        //TODO End
    }

    private void initNotification() {
        Cursor cursor= okeyClickDatabaseHelper.getNotifications();
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
                        notification.setNotifyId(id);

                        notification.setNotifyTitle("Invoice");
                        notification.setNotifyMsg(notifyMsg);

                            notifications.add(notification);
                    }
            } while (cursor.moveToNext());

            NotificationAdapter adapter=new NotificationAdapter(this, notifications, this);

            LinearLayoutManager manager=new LinearLayoutManager(this);
            notificationRecyclerView.setLayoutManager(manager);
            DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, manager.getOrientation());
            notificationRecyclerView.addItemDecoration(dividerItemDecoration);
            notificationRecyclerView.setAdapter(adapter);

        }
    }

    @Override
    public void onNotificationItemClick(Notification notification) {
        Log.i("responseData", "NotifyData : "+notification.getNotifyData());
        String notifyData=notification.getNotifyData();
        try {
            JSONObject notifyDataJson=new JSONObject(notifyData);
            Bundle bundle=createBundleFromJson(notifyDataJson);

            startInvoiceActivity(bundle);
        } catch (JSONException e) {
            Log.e("responseDataError", e.toString());
        }
    }


    Bundle createBundleFromJson(JSONObject jsonObject) throws JSONException {
        Iterator<String> iterator=jsonObject.keys();
            Bundle bundle = new Bundle();
            while (iterator.hasNext()) {
                String key = iterator.next();
                bundle.putString(key, jsonObject.getString(key));
            }
            return bundle;
    }


    public void startInvoiceActivity(Bundle bundle){
        Intent invoiceIntent=new Intent(this, InvoiceActivity.class);
        invoiceIntent.putExtras(bundle);
        startActivity(invoiceIntent);
    }
}