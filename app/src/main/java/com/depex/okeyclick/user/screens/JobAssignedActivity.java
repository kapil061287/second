package com.depex.okeyclick.user.screens;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JobAssignedActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    CircularProgressBar progressBar;
    GoogleMap googleMap;
    TextView textView;
    String task_id;
    MyTask myTask;
    Button viewProfile;
    LinearLayout profileLinearLayout;
    ImageView backImage;
    String spMobile;
    FusedLocationProviderClient fusedLocationProviderClient;
    Polyline polyline;
    String spId;
    String spLatitude;
    String spLngtitud;
    Marker customerMarker;
    String spName;
    Marker Spmarker;
    TextView spNameText;
    Button callBtnToSp;
    ConstraintLayout parentLayout;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_assigned);
        preferences = getSharedPreferences("service_pref_user", MODE_PRIVATE);
        task_id = preferences.getString("task_id", "0");
        textView = findViewById(R.id.pending_request_txt);
        spNameText = findViewById(R.id.sp_name);
        myTask = new MyTask();

        viewProfile =findViewById(R.id.view_profile_btn);
        viewProfile.setOnClickListener(this);
        callBtnToSp = findViewById(R.id.call_btn_to_sp);
        callBtnToSp.setOnClickListener(this);
        profileLinearLayout = findViewById(R.id.service_provider_profilelayout);
        backImage = findViewById(R.id.back_image);
        myTask.execute();
        parentLayout = findViewById(R.id.parent_constraint_layout);
        progressBar=findViewById(R.id.circle_progress_bar);

        getSupportActionBar().setTitle("Wating for Response...");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_job_assigned_activity);
        supportMapFragment.getMapAsync(this);
    }

    boolean breakJob = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_btn_to_sp:
                callToProvider(getSpMobile());
                break;
            case R.id.view_profile_btn:
                veiwProfile(getSpId());
                break;
        }
    }

    private void veiwProfile(String spId) {

       /* Bundle bundle=new Bundle();
        bundle.putString("sp_id", spId);
        Intent intent=new Intent(this, ServiceProviderProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
*/
    }

    public class MyTask extends AsyncTask<Void, Integer, String> {
        float progress = 0;

        @Override
        protected String doInBackground(Void... voids) {
            check();
            for (int i = 0; i <= 100; i++) {
                if (isCancelled()) break;
                try {
                    Thread.sleep(100);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            breakJob = true;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress += (100.0f / 100f);
            progressBar.setProgress(progress);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void check() {

        JSONObject requestData = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", task_id);
            requestData.put("RequestData", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Retrofit
                .Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .isTaskAccepted(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString = response.body();
                        Log.i("responseData", "is accept request : " + responseString);
                        try {
                            JSONObject res = new JSONObject(responseString);
                            boolean success = res.getBoolean("successBool");
                            if (success) {
                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                findViewById(R.id.back_image).setVisibility(View.GONE);
                                profileLinearLayout.setVisibility(View.VISIBLE);
                                getSupportActionBar().setTitle("Job ID : " + task_id);

                                JSONObject resData = res.getJSONObject("response");
                                setSpMobile(resData.getString("sp_name"));
                                setSpLatitude(resData.getString("sp_latitude"));
                                setSpLngtitud(resData.getString("sp_longitude"));
                                setSpName(resData.getString("sp_name"));
                                setSpId(resData.getString("sp_id"));
                                if(Spmarker!=null){
                                    Spmarker.remove();
                                }

                                MarkerOptions markerOptions=new MarkerOptions();
                                markerOptions.position(new LatLng(Double.parseDouble(getSpLatitude()), Double.parseDouble(getSpLngtitud())))
                                        .visible(true)
                                        .title(getSpName());
                                Spmarker=googleMap.addMarker(markerOptions);

                                myTask.cancel(true);
                                createPolyline();


                            } else {
                                if (!breakJob)
                                    check();
                                else {
                                    final Snackbar snackbar = Snackbar.make(parentLayout, "No Service Provider find at location you can resend Request !", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.show();
                                    snackbar.setAction("Resend", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                            finish();
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    private void createPolyline() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                MarkerOptions options=new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title(preferences.getString("fullname", "you"))
                        .visible(true);
               if(customerMarker!=null){
                   customerMarker.remove();
               }

                customerMarker=googleMap.addMarker(options);


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
                                                                .color(Color.BLUE);

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

                                    }
                                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        myTask.cancel(true);
        super.onDestroy();
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
    public class TrackSp extends AsyncTask<Integer, Integer, Integer>{

        boolean isTracking=true;

        @Override
        protected Integer doInBackground(Integer... integers) {

            trackSp();
            Log.i("responseData", "Tracking Service Provider");
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            isTracking=false;
        }

        public void trackSp(){

            if(Spmarker!=null){
                Spmarker.remove();
            }

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
                                    String lngStr=resObj.getString("user_longitute");
                                    String address=resObj.getString("user_address");
                                    double lat=Double.parseDouble(latStr);
                                    double lng=Double.parseDouble(lngStr);
                                    Spmarker =googleMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(lat, lng)).title(getSpName()).snippet(address));

                                    if(isTracking)
                                    trackSp();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
        }
    }
}