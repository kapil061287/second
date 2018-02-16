package com.depex.okeyclick.user.screens;

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

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;

import org.json.JSONException;
import org.json.JSONObject;

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
    private boolean isTimerStart=true;

    private boolean isStartJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_timer);

        ButterKnife.bind(this);

        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        Typeface typeface= ResourcesCompat.getFont(this, R.font.digital);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        Mytask mytask=new Mytask();
        mytask.execute();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerTimerActivity.super.onBackPressed();
            }
        });
        timer_text.setTypeface(typeface);
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
                        Log.i("responseDataRunning", responseString );
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                int task_status=resObj.getInt("task_status");
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
                                        //Start Job
                                        break;
                                    case 6:
                                        //in Progress
                                        break;
                                    case 7:
                                            isFinishJob=true;
                                        //Finish the job
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("responseDataError", e.toString());
                        }

                        //TODO for recursion
                       if(!isFinishJob)
                            checkServiceProviderRunningStatus();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }



    class Mytask extends AsyncTask<Integer , Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            checkServiceProviderRunningStatus();
            int i=1;
            while (isTimerStart)
            {
                try {
                    publishProgress(i);
                    Thread.sleep(1000);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
                String timerText=updateTimer(values[0]);
                timer_text.setText(timerText);
        }


        @Override
        protected void onPostExecute(Integer integer) {

        }
    }
    boolean isFinishJob;
}