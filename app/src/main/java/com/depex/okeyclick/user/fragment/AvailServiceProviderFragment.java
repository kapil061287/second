package com.depex.okeyclick.user.fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.depex.okeyclick.user.model.UserPackage;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.PackageRecyclerAdapter;
import com.depex.okeyclick.user.api.ApiListener;
import com.depex.okeyclick.user.api.CallbackApi;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.listener.PackageClickListener;
import com.depex.okeyclick.user.screens.JobAssignedActivity;
import com.depex.okeyclick.user.screens.LoginActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AvailServiceProviderFragment extends Fragment implements OnMapReadyCallback, ApiListener<JsonObject>, PackageClickListener, View.OnClickListener {
    GoogleMap googleMap;
    SharedPreferences preferences;
    Marker marker;
    FusedLocationProviderClient client;
    boolean isLogin;
    JSONObject data = null;
    Context context;
    LinearLayout linearLayout;
    int currentHeight;
    RecyclerView recyclerView;
    LocationRequest locationRequest;
    LocationCallback mLocationCallback;
    Location location;
    boolean isAnimate;
    ProgressBar progressBar;
    UserPackage userPackage;
    Button bookNowBtn;
    Button bookLaterBtn;
    ConstraintLayout constraintLayout;
    ArrayList<Marker> markers = new ArrayList<>();
    String json;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_avail_service_provide_fragment, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        Menu menu = toolbar.getMenu();
        progressBar=view.findViewById(R.id.progress_bar_avail);
        linearLayout=view.findViewById(R.id.bottom_panel_avail_fragment);
        MenuItem menuItem = menu.add(/*groupid*/1,/*Itemid*/1,/*Order*/1,/*Title*/"Pick a Location");
        linearLayout.findViewById(R.id.done_btn_availfragment).setOnClickListener(this);
        menuItem.setIcon(R.drawable.ic_location_on_black_24dp);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        constraintLayout=view.findViewById(R.id.parent_constraint_layout);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                PlaceAutocomplete.IntentBuilder intentBuilder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);
                try {
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });




        bookNowBtn = view.findViewById(R.id.book_now_btn_avail_fragment);
        bookLaterBtn = view.findViewById(R.id.book_later_btn_avail_fragment);
        bookLaterBtn.setOnClickListener(this);
        bookNowBtn.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        //  manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        /// manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,this);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                changeLocation(location);
            }
        });

        //client = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder1 = new LocationSettingsRequest.Builder();
        builder1.addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder1.build());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    changeLocation(location);
                }
            }
        };


      /*  PlaceAutocomplete.IntentBuilder intentBuilder=new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);
        try {
            Intent intent=intentBuilder.build(getActivity());
            startActivityForResult(intent, 1);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }*/


        recyclerView = view.findViewById(R.id.recycler_view_package);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.baseUrl(Utils.SITE_URL);
        Retrofit retrofit = builder.build();
        ProjectAPI projectAPI = retrofit.create(ProjectAPI.class);
        Call<JsonObject> packageCall = projectAPI.getPackages(getString(R.string.apikey));
        packageCall.enqueue(new CallbackApi<>(this));


        preferences = getActivity().getSharedPreferences("service_pref_user", Context.MODE_PRIVATE);
        setLogin(preferences.getBoolean("isLogin", false));

        String json = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            json = bundle.getString("json");
            Log.i("responseData", "Bundle Arg : " + json);
        }
        try {
            data = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
       // client.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    public void changeLocation(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        this.location = location;
        LatLng latLng = new LatLng(lat, lng);
        if(marker!=null)
            marker.remove();

        String fullname = preferences.getString("fullname", "You");

        try {
            List<Address> addresses = new Geocoder(getActivity()).getFromLocation(lat, lng, 1);

            String addressLine = "";
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                if (address.getMaxAddressLineIndex() > 0) {
                    addressLine = address.getAddressLine(0);
                }
            }


            marker = googleMap.addMarker(new MarkerOptions()
                    .visible(true)
                    .title(fullname)
                    .snippet(addressLine)
                    .position(latLng));
            CameraPosition position = new CameraPosition.Builder().target(marker.getPosition()).tilt(90).zoom(15).bearing(10).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000, null);
        } catch (IOException e) {
            Log.e("googleLogError", e.toString());
        }
    }


    public static Fragment newInstance(String json) {
        Fragment fragment = new AvailServiceProviderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    /*@Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/

    @Override
    public void success(Call<JsonObject> call, Response<JsonObject> response, Object... objects) {
        if (response == null)
            return;
        Log.i("responseData", response.body().toString());
        JsonObject res = response.body();
        boolean success = res.get("successBool").getAsBoolean();
        if (success) {
            JsonObject responseData = res.get("response").getAsJsonObject();
            JsonArray package_list = responseData.get("List").getAsJsonArray();
            Gson gson = new Gson();
            UserPackage[] packagesArr = gson.fromJson(package_list, UserPackage[].class);
            ArrayList<UserPackage> userPackages = new ArrayList<>(Arrays.asList(packagesArr));
            Log.i("userPackages", userPackages.toString());
            final PackageRecyclerAdapter packageRecyclerAdapter = new PackageRecyclerAdapter(getActivity(), userPackages, this);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(packageRecyclerAdapter);
        }
    }


   /* public void startDownAnimation(View v){

        v.setVisibility(View.GONE);
        linearLayout.findViewById(R.id.description_content_txt_avail_fragment).setVisibility(View.GONE);
        linearLayout.findViewById(R.id.description_text_avail_fragment).setVisibility(View.GONE);
        linearLayout.findViewById(R.id.book_btn_book_later_btn_avail_fram_layout).setVisibility(View.VISIBLE);

        final ValueAnimator animator=ValueAnimator.ofInt(currentHeight, getResources().getDimensionPixelSize(R.dimen.linear_avail_package_height));
        animator.setDuration(1000);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value= (int) valueAnimator.getAnimatedValue();
                Log.i("valueAnimator", String.valueOf(value));
                //ViewGroup.LayoutParams params=linearLayout.getLayoutParams();
                //params.height=value;
                //linearLayout.setLayoutParams(params);
                //isAnimate=false;
            }
        });
    }*/


  /*  public void startUpAnimation(){
        final ViewGroup.LayoutParams layoutParams=linearLayout.getLayoutParams();
        final int height=layoutParams.height;
        final ValueAnimator animator=ValueAnimator.ofInt(getResources().getDimensionPixelSize(R.dimen.animate_from_value), getResources().getDimensionPixelSize(R.dimen.animate_to_value));
        //ValueAnimator animator=ValueAnimator.ofInt(0,50);
        animator.setDuration(1000);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value= (int) valueAnimator.getAnimatedValue();
                layoutParams.height=height+value;
                linearLayout.setLayoutParams(layoutParams);
                FrameLayout frameLayout=linearLayout.findViewById(R.id.book_btn_book_later_btn_avail_fram_layout);
                frameLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                final TextView description =linearLayout.findViewById(R.id.description_text_avail_fragment);
                TextView description_content=linearLayout.findViewById(R.id.description_content_txt_avail_fragment);
                description.measure(description.getLayoutParams().width, description.getLayoutParams().height);
                description_content.measure(description_content.getLayoutParams().width, description_content.getLayoutParams().height);
                Button doneButton=linearLayout.findViewById(R.id.done_btn_availfragment);
                doneButton.measure(doneButton.getLayoutParams().width, doneButton.getLayoutParams().height);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    description.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
                        @Override
                        public void onDraw() {
                            Log.i("valueAnimator", "View is ready !");
                            Log.i("valueAnimator", "View Height : "+description.getHeight());
                        }
                    });
                }
                int measureFramelayoutHeight=frameLayout.getMeasuredHeight();
                int measureDescriptionTextViewHeight=description.getMeasuredHeight();
                int measureDescriptionContentTextViewHeight=description_content.getMeasuredHeight();
                int measureDoneBtn=doneButton.getMeasuredHeight();
                


                int totalHeight=measureDescriptionTextViewHeight+measureDescriptionContentTextViewHeight+measureDoneBtn;

                if(value>=totalHeight){
                    Log.i("valueAnimator", "Total Height : "+totalHeight+" Value : "+value+" Des H : "+measureDescriptionTextViewHeight+" des cont H : "+measureDescriptionContentTextViewHeight+" done Btn H : "+measureDoneBtn);
                    doneButton.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    description_content.setVisibility(View.VISIBLE);
                    isAnimate=true;
                    currentHeight=linearLayout.getLayoutParams().height;
                    animator.cancel();
                }
            }
        });
    }
*/


    @Override
    public void onPackageClick(final UserPackage userPackage) {



        this.userPackage = userPackage;

        /*AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(userPackage.getPackageDescription());
        builder.setTitle("Package Description");
        builder.setPositiveButton("Done", null);
        builder.create().show();*/


        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
        View view=LayoutInflater.from(context).inflate(R.layout.package_bottom_sheet_layout, null, false);
        TextView textView=view.findViewById(R.id.pac_description);
        TextView textView1=view.findViewById(R.id.package_name_desc);
        Button button=view.findViewById(R.id.done_btn_pack_desc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        textView1.setText(userPackage.getPackageName());
        textView.setText(userPackage.getPackageDescription());
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();


        /*if(!isAnimate) {
            startUpAnimation();
        }else {
            boolean isFrameLayoutVisible=linearLayout.findViewById(R.id.book_btn_book_later_btn_avail_fram_layout).getVisibility()==View.VISIBLE;
            if(isFrameLayoutVisible){
                linearLayout.findViewById(R.id.book_btn_book_later_btn_avail_fram_layout).setVisibility(View.GONE);
                linearLayout.findViewById(R.id.description_text_avail_fragment).setVisibility(View.VISIBLE);
                linearLayout.findViewById(R.id.description_content_txt_avail_fragment).setVisibility(View.VISIBLE);
                linearLayout.findViewById(R.id.done_btn_availfragment).setVisibility(View.VISIBLE);
            }
        }*/

        if(location!=null){
            getAvailableServiceProvider(location,userPackage);

            return;
        }



        /*linearLayout.animate()
                .setDuration(500)
                .translationY(0f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                            ViewGroup.LayoutParams params=linearLayout.getLayoutParams();
                            params.height=params.height+200;
                            linearLayout.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
*/
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    getAvailableServiceProvider(location, userPackage);
                    Geocoder geocoder=new Geocoder(context, Locale.getDefault());
                    try {
                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Log.i("localityLog", addresses+" : "+location);
                        for(Address address : addresses){
                            Log.i("localityLog", address.getLocality()+"");
                        }

                    } catch (Exception e) {
                       Log.e("localityLog", e.toString());
                    }
                }
            });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    public void getAvailableServiceProvider(Location location, UserPackage userPackage){
        Log.i("locationLog", location+"");
        double currentLat=location.getLatitude();
        double currentLng=location.getLongitude();
//        double currentLat=28.5834765;
//        double currentLng=77.3186916;

        String id=userPackage.getId();

        JSONObject requestData=new JSONObject();
        try {
            data.put("package", id);
            data.put("latitude", currentLat);
            data.put("longitude", currentLng);
            requestData.put("RequestData", data);
            Log.i("requestData", "Request Data for Avail sp : "+requestData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit.Builder builder=new Retrofit.Builder();
        builder.baseUrl(Utils.SITE_URL);
        builder.addConverterFactory(new StringConvertFactory());
        Call<String> availuser= builder.build()
                .create(ProjectAPI.class)
                .availableServiceProvider(requestData.toString());
        availuser.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString=response.body();
                Log.i("responseData", "Avail Service : "+responseString);
                if(markers.size()>0){
                    for(Marker marker : markers){
                        marker.remove();
                    }
                    markers.clear();
                }
                try {
                    JSONObject res=new JSONObject(responseString);
                    boolean success=res.getBoolean("successBool");
                    if(success){
                        JSONObject responseData=res.getJSONObject("response");
                        JSONArray jsonArray=responseData.getJSONArray("List");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            double lat=object.getDouble("latitude");
                            double lng=object.getDouble("longitude");
                            String first_name=object.getString("first_name");
                            String last_name=object.getString("last_name");

                            Geocoder geocoder=new Geocoder(context);
                            String address="SP Location";
                            try {
                                List<Address> list=geocoder.getFromLocation(lat,lng, 1);
                                Address address1=list.get(0);
                                address=address1.getAddressLine(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Marker marker=googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(first_name+" "+last_name).visible(true).snippet(address));
                            //marker.showInfoWindow();
                            markers.add(marker);
                        }
                    }else {
                        Toast.makeText(context, "Sorry! No Service Provider available on this location", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("responseError", t.toString());
            }
        });
    }


    public void sendHttpRequest(JSONObject  jsonObject){
        final Retrofit.Builder builder=new Retrofit.Builder();
        ProjectAPI projectAPI=builder.baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build().create(ProjectAPI.class);
        projectAPI.createRequest(jsonObject.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString=response.body();
                Log.i("responseData", responseString);
                try {
                    JSONObject res= new JSONObject(responseString);
                    boolean success=res.getBoolean("successBool");
                    if(success){
                        JSONObject responseObj=res.getJSONObject("response");
                        String task_id=responseObj.getString("task_id");
                        Bundle bundle=new Bundle();
                        bundle.putString("task_id", task_id);
                        progressBar.setVisibility(View.GONE);

                        Bundle bundle1=new Bundle();
                        bundle1.putDouble("lat", AvailServiceProviderFragment.this.location.getLatitude());
                        bundle1.putDouble("lng", AvailServiceProviderFragment.this.location.getLongitude());

                        Intent intent=new Intent(context, JobAssignedActivity.class);
                        intent.putExtras(bundle1);
                        preferences.edit().putString("task_id", task_id).apply();
                        startActivity(intent);
                    }else{
                        progressBar.setVisibility(View.GONE);

                        JSONObject errorObj=res.getJSONObject("ErrorObj");
                        String code=errorObj.getString("ErrorCode");
                        if(code.equals("103")){
                            String msg=errorObj.getString("ErrorMsg");
                            Snackbar.make(constraintLayout, msg, Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
                        }


                    }
                } catch (JSONException e) {
                    Log.e("responseDataError", e.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("responseError", t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.book_now_btn_avail_fragment:
                if(location==null){
                    final Snackbar snackbar=Snackbar.make(view, "We are unable to pick your location please Select your location!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    return;
                }
                if(userPackage==null){
                    final Snackbar snackbar=Snackbar.make(view, "Please Select your package!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                }

                    if(isLogin()){
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "start booking ", Toast.LENGTH_LONG).show();
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("v_code", getString(R.string.v_code));
                            jsonObject.put("apikey", getString(R.string.apikey));
                            jsonObject.put("category", data.getString("category"));
                            jsonObject.put("subcategory", data.getString("subcategory"));
                            jsonObject.put("package", userPackage.getId());
                            jsonObject.put("latitude", location.getLatitude());
                            jsonObject.put("longitude", location.getLongitude());
                            jsonObject.put("task_title", "");
                            jsonObject.put("applied_coupen", "dWHhmJ");
                            jsonObject.put("userToken", preferences.getString("userToken", "0"));
                            jsonObject.put("created_by", preferences.getString("user_id", "0"));
                            JSONObject requestData=new JSONObject();
                            requestData.put("RequestData", jsonObject);
                            Log.i("requestDataCreate", jsonObject.toString());
                            sendHttpRequest(requestData);
                            preferences.edit()
                                    .putString("from_book_screen", jsonObject.toString())
                                    .putBoolean("createRequest", true).apply();
                        }catch (Exception e){
                            Log.e("responseDataError", "Error brom Booking : "+e);
                        }
                    }else{
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("v_code", getString(R.string.v_code));
                            jsonObject.put("apikey", getString(R.string.apikey));
                            jsonObject.put("category", data.getString("category"));
                            jsonObject.put("subcategory", data.getString("subcategory"));
                            jsonObject.put("package", userPackage.getId());
                            jsonObject.put("latitude", location.getLatitude());
                            jsonObject.put("longitude", location.getLongitude());
                            jsonObject.put("task_title", "");
                            jsonObject.put("applied_coupen", "");
                            Intent intent=new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            preferences.edit()
                                    .putString("from_book_screen", jsonObject.toString())
                                    .putBoolean("createRequest", true).apply();
                        } catch (JSONException e) {
                            Log.e("responseDataError", "Error brom Booking : "+e);
                        }
                    }
                break;
            case R.id.book_later_btn_avail_fragment:
                break;
            case R.id.done_btn_availfragment:
                //startDownAnimation(view);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            Place place=PlaceAutocomplete.getPlace(context, data);
            LatLng latLng=place.getLatLng();
            if(marker!=null)
                marker.remove();
            client.removeLocationUpdates(mLocationCallback);
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.visible(true)
                    .title(preferences.getString("fullname", "You"))
                    .position(latLng);
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.if_blue_pin_68011));
            marker = googleMap.addMarker(markerOptions);
            CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng, 13);
            googleMap.moveCamera(cameraUpdate);
            Location location=new Location("A");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            if(userPackage!=null)
            getAvailableServiceProvider(location, userPackage);
            this.location=location;
        }
    }



    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        client.removeLocationUpdates(mLocationCallback);
        Toolbar toolbar=getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        toolbar.getMenu().removeItem(1);
    }
}