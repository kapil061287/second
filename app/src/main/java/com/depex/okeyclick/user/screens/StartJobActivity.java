package com.depex.okeyclick.user.screens;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.model.TaskDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.depex.okeyclick.user.screens.JobAssignByNotification.getRatingText;

public class StartJobActivity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private static final String START_JOB = "start job";
    private static final String FINISH_JOB = "finish job";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sp_image)
    RoundedImageView spImage;

    @BindView(R.id.customer_image)
    RoundedImageView customerImage;

    @BindView(R.id.sp_name)
    TextView spName;

    @BindView(R.id.service_time)
    TextView serviceTime;

    @BindView(R.id.service_address)
    TextView serviceAddress;

    @BindView(R.id.job_id)
    TextView jobId;

    @BindView(R.id.service_amount)
    TextView serviceAmount;

    @BindView(R.id.customer_name)
    TextView customerName;

    @BindView(R.id.confirm_complete)
    Button confirmComplete;

    @BindView(R.id.service_name)
    TextView serviceName;



    SharedPreferences preferences;
    String task_id;

    TaskDetail detail;

    RatingBar ratingBarBottomSheet;

    TextView rateTextView;

    Button submitRatingBtn;

    EditText rateCommentEdit;


    AlertDialog confirmRatingDialog;
    MyReciever reciever;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_job);
        ButterKnife.bind(this);
        toolbar.setTitle("Job Progress");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title_color));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        confirmComplete.setOnClickListener(this);
        reciever=new MyReciever();


        registerMyReceiver();

        preferences=getSharedPreferences(Utils.SERVICE_PREF, MODE_PRIVATE);
        task_id=preferences.getString("task_id", "0");

        Bundle bundle=getIntent().getExtras();
        if(bundle.getString("taskDetailsJson")!=null){
            String json=bundle.getString("taskDetailsJson");
            Log.i("responseData", "Task Details : "+json);
            Gson gson=new GsonBuilder().setDateFormat(getString(R.string.date_time_format_from_web)).create();
            detail=gson.fromJson(json, TaskDetail.class);
            initScreen(detail);

        }else {

            init();

        }
    }

    private void registerMyReceiver() {
            if(reciever!=null){
                IntentFilter filter=new IntentFilter(Utils.ACTION_TASK_PROCESS_INTENT);
                LocalBroadcastManager.getInstance(this).registerReceiver(reciever, filter);
            }
    }

    private void initScreen(TaskDetail detail) {
        String spurl=detail.getSpProfilePic();
        String csUrl=detail.getCsProfilePic();
        task_id=detail.getTaskId();
        spName.setText(detail.getSpName());
        customerName.setText(detail.getCsName());
        String job=detail.getTaskId();
        jobId.setText(job);
        serviceAddress.setText(detail.getCsAddress());
        serviceName.setText(detail.getCategory());
        serviceAddress.setText(detail.getCsAddress());
        serviceTime.setText(detail.getWorkDuration());
        serviceAmount.setText(getString(R.string.uro)+detail.getTotal());


        GlideApp.with(this).load(spurl).placeholder(R.drawable.user_dp_place_holder).circleCrop().into(spImage);
        GlideApp.with(this).load(csUrl).placeholder(R.drawable.user_dp_place_holder).circleCrop().into(customerImage);
    }

    private void init() {

        final JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();

        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", ""));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", task_id);
            requestData.put("RequestData", data);
        } catch (JSONException e) {
            Log.e("responseDataError", e.toString());
        }


        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .taskDetails(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString=response.body();
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                Gson gson=new GsonBuilder().setDateFormat(getString(R.string.date_time_format_from_web)).create();
                                detail=gson.fromJson(resObj.toString(), TaskDetail.class);
                                initScreen(detail);
                            }
                        } catch (JSONException e) {
                            Log.e("responseDataError", e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("responseDataError"," "+t.toString());
                    }
                });
    }


    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.confirm_complete:
                        confirmComplete();
                    break;
                case R.id.submit_rating_btn:
                    submitRating();
                    break;
            }
    }



        private void confirmComplete() {
            JSONObject requestData=new JSONObject();
            JSONObject data=new JSONObject();
            try {
                data.put("v_code", getString(R.string.v_code));
                data.put("apikey", getString(R.string.apikey));
                data.put("user_id", preferences.getString("user_id", "0"));
                data.put("userToken", preferences.getString("userToken", "0"));
                data.put("task_id", task_id);
                requestData.put("RequestData", data);

                new Retrofit.Builder()
                        .baseUrl(Utils.SITE_URL)
                        .addConverterFactory(new StringConvertFactory())
                        .build()
                        .create(ProjectAPI.class)
                        .confirmComplete(requestData.toString())
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String responseString=response.body();
                                Log.i("responseData", "Confirm Complete : "+responseString);
                                try {
                                    JSONObject res=new JSONObject(responseString);
                                    boolean success=res.getBoolean("successBool");
                                    if(success){
                                        showReviewRatingSheet();
                                        splitPayment();
                                    }
                                } catch (JSONException e) {
                                    Log.e("responseDataError", "Start job Activity : "+e.toString());
                                }
                            }


                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("responseData","confirm complete error : "+ t.toString());
                            }

                        });
            } catch (JSONException e) {
                Log.e("responseDataError","Start Job Errror : "+ e.toString());
            }
        }

    private void splitPayment() {
            String charegeId=preferences.getString("charge_id", "0");
            ReleaseTask task=new ReleaseTask();
            task.execute(charegeId);

    }

    private void showReviewRatingSheet() {
        // Toast.makeText(this, "Review Rating screen sheet " , Toast.LENGTH_LONG).show();
        View view= LayoutInflater.from(this).inflate(R.layout.content_review_rating_action_sheet, null, false);
        BottomSheetDialog dialog=new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
        ratingBarBottomSheet=view.findViewById(R.id.rating_bar_review_bottom_sheet);
        rateTextView=view.findViewById(R.id.rate_txt);
        submitRatingBtn=view.findViewById(R.id.submit_rating_btn);
        submitRatingBtn.setOnClickListener(this);
        rateTextView.setText(getRatingText(ratingBarBottomSheet.getRating()));
        ratingBarBottomSheet.setOnRatingBarChangeListener(this);
        rateCommentEdit=view.findViewById(R.id.write_review_edit);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        rateTextView.setText(getRatingText(v));
    }

    private void submitRating() {

        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("sender_id", preferences.getString("user_id","0"));
            data.put("receiver_id",detail.getCsId());
            data.put("task_id", task_id);
            data.put("rate", ratingBarBottomSheet.getRating());
            data.put("comment", rateCommentEdit.getText().toString());
            requestData.put("RequestData", data);
            Log.i("requestData", "Rating API : "+requestData);
        } catch (JSONException e) {
            Log.e("responseDatError","Submit Rating : "+e.toString());
        }


        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .rating(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString=response.body();
                        Log.i("responseData", "Rating Api : "+responseString);
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                String msg=resObj.getString("msg");
                                showConfirmRatingDialog(msg);
                            }
                        } catch (JSONException e) {
                            Log.e("responseData", "submitRating :  "+e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("responseData", "submitRating :  "+t.toString());
                    }
                });
    }

    private void showConfirmRatingDialog(String msg) {
        confirmRatingDialog =new AlertDialog.Builder(this)
                .setMessage(msg)
                .setTitle("Confirm Rating")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startHomeActivity();
                        confirmRatingDialog.dismiss();
                    }
                }).create();
        confirmRatingDialog.show();
    }


    private void startHomeActivity() {
        Intent homeIntent=new Intent(this, HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
    }

    class MyReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equalsIgnoreCase(Utils.ACTION_TASK_PROCESS_INTENT)){
                    changeTaskStatus(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterMyReciver();
    }

    private void unregisterMyReciver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reciever);
    }

    private void changeTaskStatus(Intent intent) {
        Bundle bundle=intent.getExtras();
        String json=bundle.getString("json");
        try {
            JSONObject jsonObj=new JSONObject(json);
            String taskStatus=jsonObj.getString("task_status");
            Log.i("responseData","Task Status : "+taskStatus);
            switch (taskStatus){
                case "3":
//                        taskProcess.setText(R.string.start_job_journey);
                    break;
                case "4":
                    //startJobStartActivity();
                    break;
                case "5":
                    //taskProcess.setText(R.string.start_job);
                    break;
                case "7":
                    confirmComplete.setVisibility(View.VISIBLE);
                 /*   callBtnToSp.setVisibility(View.GONE);
                    viewProfile.setVisibility(View.GONE);
                    taskProcess.setText(R.string.finish_job_sp_side);*/
                    break;
            }
        } catch (JSONException e) {
            Log.i("respnseDataError", "Json Obje Task "+e.toString());
        }
    }

    class ReleaseTask extends AsyncTask<String ,String ,String>{

        @Override
        protected String doInBackground(String... strings) {

            com.stripe.Stripe.apiKey=getString(R.string.stripe_api_secret_key);
            try {
               /* Map<String , Object > params=new HashMap<>();
                Map<String, Object> destinationUpdate=new HashMap<>();
                destinationUpdate.put("amount", 20000);
                destinationUpdate.put("account", "acct_1CAwwuCoyEC3BPTg");
                params.put("destination", destinationUpdate);*/

//                params.put("application_fee", 300);
//                RequestOptions requestOptions = RequestOptions.builder().setStripeAccount("acct_1CAwwuCoyEC3BPTg").build();
                Charge charge=Charge.retrieve(strings[0]);
                charge.capture();

                Log.i("responseData", " Release Payment : "+charge.toJson());
            } catch (AuthenticationException e) {
                Log.e("responseDataError","AuthenticationException" +e.toString());
            } catch (InvalidRequestException e) {
                Log.e("responseDataError","InvalidRequestException" +e.toString());
            } catch (APIConnectionException e) {
                Log.e("responseDataError","APIConnectionException" +e.toString());
            } catch (CardException e) {
                Log.e("responseDataError","CardException" +e.toString());
            } catch (APIException e) {
                Log.e("responseDataError","APIException" +e.toString());
            }


            return null;
        }
    }
}