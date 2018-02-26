package com.depex.okeyclick.user.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.database.NotificationHelperDatabase;
import com.depex.okeyclick.user.screens.InvoiceActivity;
import com.depex.okeyclick.user.screens.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;


public class OkeyMessagingService extends FirebaseMessagingService {

    SharedPreferences preferences;

    NotificationHelperDatabase database;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String address=remoteMessage.getFrom();
        Log.i("remoteMessage","From : " +address);
        Log.i("remoteMessage", "Message Data Payload : "+remoteMessage.getData());
        if(remoteMessage.getData().get("notification_type")==null)return;
        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        database=new NotificationHelperDatabase(this);
        boolean inCustomerTimeActivity=preferences.getBoolean("inCustomerTimeActivity", false);
        Map<String, String > map=remoteMessage.getData();

        if(inCustomerTimeActivity){
            if(map.get("notification_type").equalsIgnoreCase("invoice")) {
                Bundle bundle = createBundleForInvoiceFromMap(map);
                Intent intent=createIntentForInvoice(bundle);
                startActivity(intent);
            }
        }else{
            if("invoice".equalsIgnoreCase(map.get("notification_type"))){
                sendNotification(map, map.get("msg"), "Invoice");
            }
        }

        if("invoice".equalsIgnoreCase(map.get("notification_type"))){
            Date date=new Date();
            String msg=remoteMessage.getData().get("msg");
            String notifyData=jsonFromMap(map).toString();
            long id=database.insert("invoice", msg, notifyData, date);
            Log.i("responseData", id+"");
        }

    }

    JSONObject jsonFromMap(Map<String, String> map){
        JSONObject jsonObject=new JSONObject();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {

                jsonObject.put(entry.getKey(), entry.getValue());

            }
        }catch (Exception e){
                Log.e("responseDataError", e.toString());
        }
        return jsonObject;
    }


    public void sendNotification(Map<String, String> map, String msg, String title){
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel(getString(R.string.notification_channel_id), "Notification", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        Bundle bundle=createBundleForInvoiceFromMap(map);

        Intent intent=createIntentForInvoice(bundle);


        Notification  notification=new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        notification.flags=NotificationCompat.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);

    }

    public Intent createIntentForInvoice(Bundle bundle){
        Intent intent=new Intent(this, InvoiceActivity.class);
                if(bundle.getString("notification_type", "0").equalsIgnoreCase("invoice")){
                    intent.putExtras(bundle);
                }
                return intent;
    }


    public Bundle createBundleForInvoiceFromMap(Map<String, String> map){
        Bundle bundle=new Bundle();
            for(Map.Entry<String, String> entry : map.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                bundle.putString(key, value);
            }
        return bundle;
    }
}
