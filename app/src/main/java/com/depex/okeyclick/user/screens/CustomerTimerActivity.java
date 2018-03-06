package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.models.BraintreeApiConfiguration;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomerTimerActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences preferences;
    @BindView(R.id.timer_text)
    TextView timer_text;

    @BindView(R.id.progress_icno_image)
    ImageView timer_image;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sp_name_customer_timer)
    TextView spName;

    @BindView(R.id.service_name_timer)
    TextView serviceNameTimer;

    @BindView(R.id.sp_address_timer)
    TextView addressTimer;

    private boolean isTimerStart=false;

    Mytask mytask;
    private boolean isStartJob;

    boolean isInProgress=true;
    boolean imageChange=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_timer);

        ButterKnife.bind(this);

        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        Typeface typeface= ResourcesCompat.getFont(this, R.font.digital);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        mytask=new Mytask();
        mytask.execute(1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerTimerActivity.super.onBackPressed();
            }
        });
        timer_text.setTypeface(typeface);
    }


    private void veiwProfile(String spId) {
        Bundle bundle=new Bundle();
        bundle.putString("sp_id", spId);
        Intent intent=new Intent(this, ServiceProviderProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }




    @Override
    public void onClick(View view) {

    }

    public String updateTimer(int seconds){

        int hours=0;
        int minuts=0;
        int seconds2=0;

        int remainder=0;
        hours=seconds/(60*60);
        remainder=seconds%(60*60);
        minuts=remainder/60;
        remainder=remainder%60;
        seconds2=remainder;

        String hoursString="";
        String minutString="";
        String seconds2String="";

        if(hours<10) hoursString="0"+hours;
        else hoursString=String.valueOf(hours);

        if(minuts<10) minutString="0"+minuts;
        else minutString=String.valueOf(minuts);

        if(seconds2<10) seconds2String="0"+seconds2;
        else seconds2String=String.valueOf(seconds2);

        return hoursString+":"+minutString+":"+seconds2String;
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences.edit().putBoolean("inCustomerTimeActivity", true).apply();
    }


    private void checkServiceProviderRunningStatus() {

        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();

        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", preferences.getString("task_id", "0"));
            requestData.put("RequestData", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .checkSpStatus(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString =response.body();
                        if(responseString==null){
                            return;
                        }
                        Log.i("responseDataRunning", "Customer Timer Activity : "+responseString+"" );
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                int task_status=resObj.getInt("task_status");

                                JSONObject spData=resObj.getJSONObject("sp_Data");
                                String firstName=spData.getString("first_name");
                                spName.setText("Mr. "+firstName);
                                //String serviceName


                                switch (task_status){
                                    case 1:

                                        //Pending
                                        break;
                                    case 2:

                                        //Accepted
                                        break;
                                    case 3:
                                        //Start Journey
                                        break;
                                    case 4:

                                        //Reached ..
                                        break;
                                    case 5:
                                        isTimerStart=true;
                                        //startTimer(1);

                                        //Start Job
                                        break;
                                    case 6:
                                        //in Progress
                                        break;
                                    case 7:
                                            isFinishJob=true;
                                            isTimerStart=false;
                                            imageChange=true;
                                        //Finish the job
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("responseDataError", e.toString());
                        }

                        //TODO for recursion
                       if(!isFinishJob)
                           isResponse=true;
                           // checkServiceProviderRunningStatus();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                            Log.i("responseDataError", t.toString());
                            checkServiceProviderRunningStatus();
                    }
                });
    }


/*
    private void startTimer(int i) {
       *//* int i=1;
        while (isTimerStart)
        {*//*
       //int i=1;
                if(isTimerStart) {
                    try {
                        String timerText = updateTimer(i);
                        timer_text.setText(timerText);
                        Log.i("progressLog", "Task is in progress.... : " + timer_text.getText().toString());
                        Thread.sleep(1000);
                        //i++;
                        startTimer(++i);
                    }catch (InterruptedException e){
                        Log.e("responseDataError", e.toString());
                    }
                }else {

                }
       // }
    }*/

    private boolean isResponse=true;

    class Mytask extends AsyncTask<Integer , Integer, Integer>{
        @Override
        protected Integer doInBackground(Integer... integers) {
            int i=integers[0];
            while (isInProgress){
                if(isTimerStart) {
                    try {
                        publishProgress(i);
                        Thread.sleep(1000);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    publishProgress();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(isTimerStart && values.length>0){
                String timerText = updateTimer(values[0]);
                timer_text.setText(timerText);
                Log.i("progressLog", "Task is in progress.... : " + timer_text.getText().toString());
                //checkServiceProviderRunningStatus();
            }if(!isTimerStart) {
                if (imageChange) {
                    timer_image.setBackgroundResource(R.drawable.progress_icon_2);
                }
            }
            if(isResponse) {
                isResponse=false;
                checkServiceProviderRunningStatus();
            }
        }


        @Override
        protected void onPostExecute(Integer integer) {

        }
    }
        boolean isFinishJob;

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void stopAllService(){
        isResponse=false;
        isInProgress=false;
        isTimerStart=false;
        isFinishJob=true;
        preferences.edit().putBoolean("inCustomerTimeActivity", false).apply();
        SharedPreferences preferences1=getSharedPreferences("timer_job_pref", MODE_PRIVATE);
        preferences1.edit().putString("timeAtStopJob", timer_text.getText().toString()).apply();
        preferences1.edit().putLong("currentTimeMillis", new Date().getTime()).apply();
        mytask.cancel(true);
    }


    public void  startService(){
        SharedPreferences preferences=getSharedPreferences("timer_job_pref", MODE_PRIVATE);
        String time_Start=preferences.getString("timeAtStopJob", null);

        if(time_Start!=null){
            isResponse=true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAllService();
    }
}