package com.depex.okeyclick.user.screens;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.PolyUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JobAssignByNotification extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private static final int INVOICE_REQUEST_CODE = 5;
    GoogleMap googleMap;
    TextView textView;

    String task_id;
    boolean isTracking = true;
    MyTask myTask;
    @BindView(R.id.view_profile_btn)
    Button viewProfile;
    @BindView(R.id.service_provider_profilelayout)
    LinearLayout profileLinearLayout;

    @BindView(R.id.back_image)
    ImageView backImage;

    @BindView(R.id.task_process)
    TextView taskProcess;

    String spMobile;
    FusedLocationProviderClient fusedLocationProviderClient;
    Polyline polyline;
    String spId;
    String spLatitude;
    String spLngtitud;
    Marker customerMarker;
    String spName;
    Marker Spmarker;
    @BindView(R.id.sp_name)
    TextView spNameText;
    @BindView(R.id.call_btn_to_sp)
    Button callBtnToSp;
    @BindView(R.id.connecting_nearest)
    LinearLayout connetingNearst;
    @BindView(R.id.parent_constraint_layout)
    ConstraintLayout parentLayout;

    SharedPreferences preferences;
    String profilePicUrl;

    @BindView(R.id.profile_pic_activity_job_assigned)
    RoundedImageView profilePicImageView;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;

    boolean isPaymentSucceed;

    MyReciever myReciever;
    boolean isRequestAccepted;

    @BindView(R.id.confirm_complete_btn)
    Button confirmComplteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_job_assigned);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(Utils.SERVICE_PREF, MODE_PRIVATE);
        preferences.edit().putInt("requestTime", 1).apply();
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.i("tokenR", token);
        sendTokenToserver(token);
        task_id = preferences.getString("task_id", "0");
        //textView = findViewById(R.id.pending_request_txt);
        spNameText = findViewById(R.id.sp_name);

        myTask = new MyTask();

        viewProfile.setOnClickListener(this);

        callBtnToSp.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        registerReciever();
        myTask.execute();
        confirmComplteBtn.setOnClickListener(this);

        getSupportActionBar().setTitle("Wating for Response...");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_job_assigned_activity);
        supportMapFragment.getMapAsync(this);
    }

    private void registerReciever() {
        myReciever=new MyReciever();
        IntentFilter filter=new IntentFilter(Utils.INTENT_ACCEPT_REQUEST);
        filter.addAction(Utils.ACTION_TASK_PROCESS_INTENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReciever,filter);
    }

    //boolean breakJob = false;
    // boolean isAccept=false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_btn_to_sp:
                if(!isPaymentSucceed) {
                    generateInvoice();
                }else {
                    callToProvider(getSpMobile());
                }
                break;
            case R.id.view_profile_btn:
                veiwProfile(getSpId());
                break;
            case R.id.cancel_btn:
                startReasonActivity();
                break;
            case R.id.confirm_complete_btn:
                confirmComplete();
                break;

        }
    }

    private void confirmComplete() {

    }

    private void startReasonActivity() {
        Intent intent=new Intent(this, CancelTaskActivity.class);
        startActivity(intent);
    }


    private void generateInvoice() {
        JSONObject data=new JSONObject();
        JSONObject requestData=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", preferences.getString("task_id", "0"));
            data.put("task_WDuration", preferences.getString("quanOfWork", "0"));
            requestData.put("RequestData", data);

            new Retrofit.Builder()
                    .addConverterFactory(new StringConvertFactory())
                    .baseUrl(Utils.SITE_URL)
                    .build()
                    .create(ProjectAPI.class)
                    .generateInvoice(requestData.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseString=response.body();
                            Log.i("responseData","Invoice : "+ responseString);
                            try {
                                JSONObject res=new JSONObject(responseString);
                                boolean success=res.getBoolean("successBool");
                                if(success){
                                    JSONObject resObj=res.getJSONObject("response");
                                    Bundle bundle=new Bundle();
                                    for(Iterator<String> keys =resObj.keys();keys.hasNext();){
                                        String key=keys.next();
                                        bundle.putString(key, resObj.getString(key));

                                    }
                                    startInvoice(bundle);
                                }
                            } catch (JSONException e) {
                                Log.i("responseDataError", e.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void startInvoice(Bundle bundle) {
        Intent intent=new Intent(this, InvoiceActivity.class);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, INVOICE_REQUEST_CODE);
    }

    private void veiwProfile(String spId) {
        Bundle bundle=new Bundle();
        bundle.putString("sp_id", spId);
        Intent intent=new Intent(this, ServiceProviderProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCameraMove() {
        profileLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCameraIdle() {
        if(isRequestAccepted) {
            profileLinearLayout.setVisibility(View.VISIBLE);
        }
    }


    public class MyTask extends AsyncTask<Void, Integer, String> {
        float progress = 0;

        @Override
        protected String doInBackground(Void... voids) {
            //check();
            for (int i = 0; i <= 100; i++) {
                if (isCancelled()) break;
                try {
                    Thread.sleep(100);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
           // breakJob = true;
            return "resend";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress += (100.0f / 100f);

        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equalsIgnoreCase("resend") && !isRequestAccepted){
                final Snackbar snackbar = Snackbar.make(parentLayout, "No Service Provider find at location you can resend Request !", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                snackbar.setAction("Resend", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myTask.cancel(true);
                        resendHitTohttp();
                        snackbar.dismiss();
                    }
                });
            }
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnCameraMoveListener(this);
        this.googleMap.setOnCameraIdleListener(this);
        Bundle bundle=getIntent().getExtras();
        double lat=bundle.getDouble("lat");
        double lng=bundle.getDouble("lng");
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15f));

    }


    //boolean isArrived;


    public void startTimer(){
        Intent intent=new Intent(this, CustomerTimerActivity.class);
        startActivity(intent);
        finish();
    }


    private void createPolyline() {

        Bundle bundle=getIntent().getExtras();
        double lat=bundle.getDouble("lat");
        double lng=bundle.getDouble("lng");

        Location location=new Location("ext");
        location.setLatitude(lat);
        location.setLongitude(lng);

        MarkerOptions options=new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(preferences.getString("fullname", "you"))
                .visible(true);
        if(customerMarker!=null){
            customerMarker.remove();
        }

        customerMarker=googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerMarker.getPosition(), 15));


        String origin=getSpLatitude()+","+getSpLngtitud();
        String destination=location.getLatitude()+","+location.getLongitude();
        new Retrofit.Builder()
                .addConverterFactory(new StringConvertFactory())
                .baseUrl("https://maps.googleapis.com/")
                .build()
                .create(ProjectAPI.class)
                .getPolyLineDirection(origin, destination, getString(R.string.server_key))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString = response.body();
                        // Log.i("responseDataGoogle", responseString);
                        try {
                            JSONObject googleRouteJson = new JSONObject(responseString);
                            JSONArray routeArray = googleRouteJson.getJSONArray("routes");
                            for (int i = 0; i < routeArray.length(); i++) {
                                JSONObject jsonForLags = routeArray.getJSONObject(i);
                                JSONArray legsArray = jsonForLags.getJSONArray("legs");
                                for (int j = 0; j < legsArray.length(); j++) {
                                    JSONObject jsonForSteps = legsArray.getJSONObject(j);
                                    JSONArray stepsArray = jsonForSteps.getJSONArray("steps");

                                    for (int k = 0; k < stepsArray.length(); k++) {
                                        JSONObject jsonForPolyLine = stepsArray.getJSONObject(k);

                                        JSONObject polyLine=jsonForPolyLine.getJSONObject("polyline");
                                        String polyPoint=polyLine.getString("points");

                                        //Log.i("responseDataGoogle", polyPoint);

                                        //Toast.makeText(AcceptServiceActivity.this , polyPoint, Toast.LENGTH_LONG).show();
                                        List<LatLng> polyPoints = PolyUtil.decode(polyPoint);
                                        PolylineOptions polylineOptions = new PolylineOptions();
                                        polylineOptions.addAll(polyPoints)
                                                .width(15)
                                                .jointType(JointType.ROUND)
                                                .color(getResources().getColor(R.color.colorPrimaryDark));
                                        List<PatternItem> pattern = Arrays.<PatternItem>asList(
                                                new Dot(), new Gap(1), new Dash(30), new Gap(1));


                                        Log.i("responseDataGoogle", polyPoints.toString());

                                        polyline = googleMap.addPolyline(polylineOptions);



                                        // Toast.makeText(AcceptServiceActivity.this, "Service Provider is add to marker", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        createPolyline();
                    }
                });
    }

    @Override
    protected void onDestroy() {

        myTask.cancel(true);
        unregisterBroadReceiver();
        super.onDestroy();
    }

    private void unregisterBroadReceiver() {
        if(myReciever!=null){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReciever);
        }
    }


    public void callToProvider(String number){
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    public void setSpMobile(String spMobile) {
        this.spMobile = spMobile;
    }

    public String getSpMobile() {
        return spMobile;
    }

    public String getSpName() {
        return spName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
        GlideApp.with(this).load(profilePicUrl).into(profilePicImageView);
    }


    public void setSpName(String spName) {
        spNameText.setText(spName);
        this.spName = spName;
    }

    public String getSpLatitude() {
        return spLatitude;
    }

    public void setSpLatitude(String spLatitude) {
        this.spLatitude = spLatitude;
    }

    public String getSpLngtitud() {
        return spLngtitud;
    }

    public void setSpLngtitud(String spLngtitud) {
        this.spLngtitud = spLngtitud;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }


    public void setVisible(int visible, View... view){
        for(View v : view){
            v.setVisibility(visible);
        }
    }


/*

    public void trackSp(){
        new Retrofit
                .Builder()
                .addConverterFactory(new StringConvertFactory())
                .baseUrl(Utils.SITE_URL)
                .build()
                .create(ProjectAPI.class)
                .getServiceProviderLocation(getString(R.string.apikey), getSpId())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String resString=response.body();
                        Log.i("responseData", "Tracking : "+resString);

                        try {
                            JSONObject res=new JSONObject(resString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                String latStr=resObj.getString("user_latitude");
                                String lngStr=resObj.getString("user_longitude");
                                String address=resObj.getString("user_address");
                                //Toast.makeText(JobAssignedActivity.this, address, Toast.LENGTH_LONG).show();
                                double lat=Double.parseDouble(latStr);
                                double lng=Double.parseDouble(lngStr);
                                if(Spmarker!=null){
                                    Spmarker.remove();
                                }
                                Spmarker =googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lng))
                                        .title(getSpName())
                                        .snippet(address));

                                if(isTracking) {
                                    trackSp();
                                }

                            }
                        } catch (Exception e) {

                            Log.e("responseDataError", e.toString());
                            Toast.makeText(JobAssignedActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        trackSp();
                                Log.e("responseDataError", t.toString());
                    }
                });
    }
*/






    public void sendTokenToserver(String token){

        if(!preferences.getBoolean("isLogin", false)){
            return;
        }

        JSONObject requestData=new JSONObject();
        try {
            String userToken=preferences.getString("userToken", "0");
            JSONObject data=new JSONObject();
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("deviceType", "android");
            data.put("userToken", userToken);
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("DeviceToken", token);
            requestData.put("RequestData" , data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit.Builder builder=new Retrofit.Builder();
        builder.baseUrl(Utils.SITE_URL);
        builder.addConverterFactory(new StringConvertFactory());
        Retrofit retrofit=builder.build();
        ProjectAPI projectAPI=retrofit.create(ProjectAPI.class);


        Call<String> updateCall=projectAPI.updateFcmToken(requestData.toString());
        updateCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("responseData","Send Token To Server : "+response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("responseDataError", "Send Token To Server : "+t.toString());
            }
        });
    }


    private void resendHitTohttp() {
        double lat=getIntent().getExtras().getDouble("lat");
        double lng=getIntent().getExtras().getDouble("lng");
        String category=getIntent().getExtras().getString("category");
        String subCategory=getIntent().getExtras().getString("subcategory");
        String packageId=getIntent().getExtras().getString("package");

        JSONObject data=new JSONObject();
        JSONObject requestData=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code))
                    .put("apikey", getString(R.string.apikey))
                    .put("category", category )
                    .put("subcategory", subCategory)
                    .put("package", packageId)
                    .put("latitude", lat)
                    .put("longitude", lng)
                    .put("userToken", preferences.getString("userToken", "0"))
                    .put("created_by", preferences.getString("user_id", "0"))
                    .put("task_id", preferences.getString("task_id", "0"))
                    .put("task_key", preferences.getString("task_key", "0"));

            requestData.put("RequestData", data);
            Log.i("requestData", "Resend Request : "+requestData.toString());

        } catch (JSONException e) {
            Log.e("responseDataError", e.toString());
        }


        new Retrofit.Builder()
                .addConverterFactory(new StringConvertFactory())
                .baseUrl(Utils.SITE_URL)
                .build()
                .create(ProjectAPI.class)
                .resendRequest(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                       // breakJob=false;
                        myTask=new MyTask();
                        myTask.execute();
                        Log.d("responseData", "Resend Request : "+response.body());
                    }


                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("responseDataError","Resend Request Error : "+t.toString());
                    }
                });
    }


    public class MyReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case Utils.INTENT_ACCEPT_REQUEST:
                    taskProcess.setText(R.string.accept_request);
                        acceptRequest(intent);
                    break;
                case Utils.ACTION_TASK_PROCESS_INTENT:
                        changeTaskStatus(intent);
                    break;
            }
        }
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
                        taskProcess.setText(R.string.start_job_journey);
                    break;
                case "4":
                    taskProcess.setText(R.string.arrive);
                    break;
                case "5":
                    taskProcess.setText(R.string.start_job);
                    break;
                case "7":
                    confirmComplteBtn.setVisibility(View.VISIBLE);
                    callBtnToSp.setVisibility(View.GONE);
                    viewProfile.setVisibility(View.GONE);
                    taskProcess.setText(R.string.finish_job_sp_side);

                    break;
            }
        } catch (JSONException e) {
            Log.i("respnseDataError", "Json Obje Task "+e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        if(!isPaymentSucceed && isRequestAccepted) {
                new AlertDialog
                        .Builder(this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startReasonActivity();
                            finish();
                    }
                })
                        .setTitle("Cencal Confirmation")
                        .setMessage("Do you want to cancel request ?")
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
        }else if(!isRequestAccepted){
                super.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }

    //Get notification from service provider for accept request
    private void acceptRequest(Intent intent) {
        setVisible(View.GONE, findViewById(R.id.back_image), connetingNearst, findViewById(R.id.avl_loader));
        profileLinearLayout.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle("Job ID : " + task_id);
        preferences.edit().putString("task_id", task_id).apply();
        preferences.edit().putBoolean("taskAccepted", true).apply();

        Bundle bundle=intent.getExtras();
        setSpMobile(bundle.getString("sp_mobile"));
        setSpLatitude(bundle.getString("sp_latitude"));
        setSpLngtitud(bundle.getString("sp_longitude"));
        setSpName(bundle.getString("sp_name"));
        setSpId(bundle.getString("sp_id"));
        setProfilePicUrl(bundle.getString("sp_profile"));
        isRequestAccepted=true;
        if(Spmarker!=null){
            Spmarker.remove();
        }
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(new LatLng(Double.parseDouble(getSpLatitude()), Double.parseDouble(getSpLngtitud())))
                .visible(true)
                .title(getSpName());
        Spmarker=googleMap.addMarker(markerOptions);
        createPolyline();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    switch (requestCode){
                        case INVOICE_REQUEST_CODE:
                            if(resultCode==RESULT_OK){
                                isPaymentSucceed=true;
                                callBtnToSp.setText("Call");
                                cancelBtn.setVisibility(View.GONE);
                            }
                            break;
                    }
    }
}