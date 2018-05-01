package com.depex.okeyclick.user.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.depex.okeyclick.user.database.OkeyClickDatabaseHelper;
import com.depex.okeyclick.user.model.UserPackage;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.PackageRecyclerAdapter;
import com.depex.okeyclick.user.api.ApiListener;
import com.depex.okeyclick.user.api.CallbackApi;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.listener.PackageClickListener;
import com.depex.okeyclick.user.screens.JobAssignByNotification;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AvailServiceProviderFragment extends Fragment implements OnMapReadyCallback, ApiListener<JsonObject>, PackageClickListener, View.OnClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMyLocationButtonClickListener {
    private static final int BOOK_LATAR_REQUEST_CODE = 2;
    private static final int BOOK_NOW_REQUEST_CODE = 3;
    GoogleMap googleMap;
    SharedPreferences preferences;
    Marker marker;
    FusedLocationProviderClient client;
    boolean isLogin;
    JSONObject data = null;
    Context context;
    @BindView(R.id.bottom_panel_avail_fragment)
    LinearLayout linearLayout;
    int currentHeight;
    LaterBookFragment fragment;
    LocationRequest locationRequest;
    LocationCallback mLocationCallback;
    Location location;
    boolean isAnimate;
    ProgressBar progressBar;
    UserPackage userPackage;
    @BindView(R.id.book_now_btn_avail_fragment)
    Button bookNowBtn;
    @BindView(R.id.book_later_btn_avail_fragment)
    Button bookLaterBtn;
    Menu menu;
    MenuItem homeMenuItem;
    MenuItem locationMenuItem;
    @BindView(R.id.parent_constraint_layout)
    ConstraintLayout constraintLayout;
    ArrayList<Marker> markers = new ArrayList<>();
    OkeyClickDatabaseHelper databaseHelper;
    SpotsDialog spotsDialog;
    @BindView(R.id.recycler_view_package)
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_avail_service_provide_fragment, container, false);
        ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        menu = toolbar.getMenu();
        //progressBar=view.findViewById(R.id.progress_bar_avail);

        databaseHelper = new OkeyClickDatabaseHelper(context);
        linearLayout.findViewById(R.id.done_btn_availfragment).setOnClickListener(this);


        spotsDialog = new SpotsDialog(context);

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
                if (location == null) return;
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

        preferences = getActivity().getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.baseUrl(Utils.SITE_URL);
        Retrofit retrofit = builder.build();
        ProjectAPI projectAPI = retrofit.create(ProjectAPI.class);



        setLogin();

        String json = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            json = bundle.getString("json");
            Log.i("responseData", "Bundle Arg : " + json);
        }
        try {
            data = new JSONObject(json);
            Call<JsonObject> packageCall = projectAPI.getPackages(getString(R.string.apikey) ,
                    preferences.getString("quanOfWork", "0"), "Noida", data.getString("subcategory"));
            packageCall.enqueue(new CallbackApi<>(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(locationMenuItem!=null)
        this.locationMenuItem.setVisible(true);
        if(homeMenuItem!=null)
        this.homeMenuItem.setVisible(true);

       /* if (menuItem == null) {
            menuItem = menu.add(*//*groupid*//*1,*//*Itemid*//*1,*//*Order*//*1,*//*Title*//*"Pick a Location");


            menuItem.setIcon(R.drawable.ic_location_on_black_24dp);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    return true;
                }
            });
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }*/

        // client.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setOnMyLocationButtonClickListener(this);
        this.googleMap.setOnCameraMoveListener(this);
        this.googleMap.setOnCameraMoveCanceledListener(this);
        this.googleMap.setOnCameraIdleListener(this);


    }

    public void changeLocation(Location location) {
        if (location == null) return;
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        this.location = location;
        LatLng latLng = new LatLng(lat, lng);
        if (marker != null)
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
        //menuItem = null;
        client.removeLocationUpdates(mLocationCallback);
        Toolbar toolbar = getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        toolbar.getMenu().removeItem(1);
    }


    @Override
    public void success(Call<JsonObject> call, Response<JsonObject> response, Object... objects) {
        if (response.body() == null)
            return;
        Log.i("responseData", "From Avail Fragment Line 328" + response.body().toString());
        JsonObject res = response.body();
        boolean success = res.get("successBool").getAsBoolean();
        if (success) {
            JsonObject responseData = res.get("response").getAsJsonObject();
            JsonArray package_list = responseData.get("List").getAsJsonArray();
            Gson gson = new Gson();
            UserPackage[] packagesArr = gson.fromJson(package_list, UserPackage[].class);
            ArrayList<UserPackage> userPackages = new ArrayList<>(Arrays.asList(packagesArr));
            Log.i("userPackages", userPackages.toString());
            final PackageRecyclerAdapter packageRecyclerAdapter = new PackageRecyclerAdapter(getActivity(), userPackages, this, false);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(packageRecyclerAdapter);
        }
    }

    @Override
    public void onPackageClick(final UserPackage userPackage) {
        this.userPackage = userPackage;

        if (location != null) {
            getAvailableServiceProvider(location, userPackage);
            return;
        }


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                getAvailableServiceProvider(location, userPackage);
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Log.i("localityLog", addresses + " : " + location);
                    for (Address address : addresses) {
                        Log.i("localityLog", address.getLocality() + "");
                    }

                } catch (Exception e) {
                    Log.e("localityLog", e.toString());
                }
            }
        });
    }

    @Override
    public void onPackageLongClick(UserPackage userPackage) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.package_bottom_sheet_layout, null, false);
        TextView textView = view.findViewById(R.id.pac_description);
        TextView textView1 = view.findViewById(R.id.package_name_desc);
        Button button = view.findViewById(R.id.done_btn_pack_desc);
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
    }

    @Override
    public void onInfoClick(UserPackage userPackage) {
        showAlert(userPackage);
    }

    private void showAlert(UserPackage userPackage) {

        View view=LayoutInflater.from(context).inflate(R.layout.content_info_package, null, false);
        TextView baseFare=view.findViewById(R.id.base_fare);
        TextView services=view.findViewById(R.id.services);
        TextView cityTax=view.findViewById(R.id.city_tax);
        TextView total=view.findViewById(R.id.total);
        cityTax.setText(userPackage.getCityTax()+"%");
        total.setText(getString(R.string.uro)+userPackage.getTotal());
        baseFare.setText(getString(R.string.uro)+userPackage.getBaseFare());
        try {
            services.setText(data.getString("quanOfWork")+" x "+context.getString(R.string.uro)+userPackage.getSubCategoryPrice());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(userPackage.getPackageName())
                .setPositiveButton("OK", null)
                .create().show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public void getAvailableServiceProvider(Location location, UserPackage userPackage) {
        Log.i("locationLog", location + "");
        double currentLat = location.getLatitude();
        double currentLng = location.getLongitude();
//        double currentLat=28.5834765;
//        double currentLng=77.3186916;

        String id = userPackage.getId();

        JSONObject requestData = new JSONObject();
        try {
            data.put("package", id);
            data.put("latitude", currentLat);
            data.put("longitude", currentLng);
            requestData.put("RequestData", data);
            Log.i("requestData", "Request Data for Avail sp : " + requestData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Utils.SITE_URL);
        builder.addConverterFactory(new StringConvertFactory());
        Call<String> availuser = builder.build()
                .create(ProjectAPI.class)
                .availableServiceProvider(requestData.toString());
        availuser.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString = response.body();
                Log.i("responseData", "Avail Service : " + responseString);
                if (markers.size() > 0) {
                    for (Marker marker : markers) {
                        marker.remove();
                    }
                    markers.clear();
                }

                try {
                    JSONObject res = new JSONObject(responseString);
                    boolean success = res.getBoolean("successBool");
                    if (success) {
                        JSONObject responseData = res.getJSONObject("response");
                        JSONArray jsonArray = responseData.getJSONArray("List");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            double lat = object.getDouble("latitude");
                            double lng = object.getDouble("longitude");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String url = object.getString("profile_pic");

                            Geocoder geocoder = new Geocoder(context);
                            String address = "SP Location";
                            try {
                                List<Address> list = geocoder.getFromLocation(lat, lng, 1);
                                Address address1 = list.get(0);
                                address = address1.getAddressLine(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(first_name + " " + last_name).visible(true).snippet(address));
                            //marker.showInfoWindow();

                            markers.add(marker);
                        }
                    } else {
                        Toast.makeText(context, "Sorry! No Service Provider ", Toast.LENGTH_LONG).show();
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


    public void sendHttpRequest(JSONObject jsonObject) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        Log.i("requestData", "Create Request Quary : " + jsonObject.toString());
        ProjectAPI projectAPI = builder.baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build().create(ProjectAPI.class);
        projectAPI.createRequest(jsonObject.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString = response.body();
                Log.i("responseData", "Create Request API : " + responseString);
                try {
                    JSONObject res = new JSONObject(responseString);
                    boolean success = res.getBoolean("successBool");
                    if (success) {
                        JSONObject responseObj = res.getJSONObject("response");
                        String task_id = responseObj.getString("task_id");
                        String task_key = responseObj.getString("task_key");
                        Bundle bundle = new Bundle();
                        bundle.putString("task_id", task_id);
                        Bundle bundle1 = new Bundle();
                        preferences.edit().putString("task_id", task_id).apply();
                        preferences.edit().putString("task_key", task_key).apply();
                        //databaseHelper.taskInsert("created", task_id, null, null, preferences.getString("user_id", "0"));
                        bundle1.putDouble("lat", AvailServiceProviderFragment.this.location.getLatitude());
                        bundle1.putDouble("lng", AvailServiceProviderFragment.this.location.getLongitude());
                        bundle1.putString("category", data.getString("category"));
                        bundle1.putString("subcategory", data.getString("subcategory"));
                        bundle1.putString("package", userPackage.getId());
                        Intent intent = new Intent(context, JobAssignByNotification.class);
                        intent.putExtras(bundle1);
                        spotsDialog.dismiss();
                        startActivity(intent);
                    } else {
                        spotsDialog.dismiss();
                        JSONObject errorObj = res.getJSONObject("ErrorObj");
                        String code = errorObj.getString("ErrorCode");
                        if (code.equals("103")) {
                            String msg = errorObj.getString("ErrorMsg");
                            Snackbar.make(constraintLayout, msg, Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
                        }
                    }
                } catch (JSONException e) {
                    spotsDialog.dismiss();
                    Log.e("responseDataError", e.toString());
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("responseError", t.toString());
                spotsDialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_now_btn_avail_fragment:
                if (location == null) {
                    spotsDialog.dismiss();
                    final Snackbar snackbar = Snackbar.make(view, "We are unable to pick your location please Select your location!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    return;
                }
                if (userPackage == null) {
                    spotsDialog.dismiss();
                    final Snackbar snackbar = Snackbar.make(view, "Please Select your package!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    return;
                }
                hitBookNow();

                /*if(preferences.getInt("requestTime", 0)==1){
                    //resendHitTohttp();
                    return;
                }*/


                break;
            case R.id.book_later_btn_avail_fragment:

                if (location == null) {
                    spotsDialog.dismiss();
                    final Snackbar snackbar = Snackbar.make(view, "We are unable to pick your location please Select your location!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    return;
                }
                if (userPackage == null) {
                    spotsDialog.dismiss();
                    final Snackbar snackbar = Snackbar.make(view, "Please Select your package!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    snackbar.setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    return;
                }


                hitBookLater();

                break;
            case R.id.done_btn_availfragment:

                break;
        }
    }

    private void hitBookLater() {
        JSONObject json = new JSONObject();
        try {
            json.put("category", data.getString("category"));
            json.put("subcategory", data.getString("subcategory"));
            json.put("subserviceName", data.getString("subserviceName"));
            json.put("package", userPackage.getId());
            json.put("lat", location.getLatitude());
            json.put("lng", location.getLongitude());
            json.put("task_WDuration", data.getString("quanOfWork"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment = LaterBookFragment.getInstance(json.toString());
        if (isLogin()) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_container, fragment)
                    .addToBackStack(null)
                    .commit();

        } else {
            startLoginActivity(false);
        }
    }

    private void startLoginActivity(boolean isBookNow) {

        if (isBookNow) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isBookNow", true);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, BOOK_NOW_REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isBookLetar", true);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, BOOK_LATAR_REQUEST_CODE);
        }
    }

    public void hitBookNow() {
        if (isLogin()) {
            // progressBar.setVisibility(View.VISIBLE);
            //Toast.makeText(context, "start booking ", Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("v_code", getString(R.string.v_code));
                jsonObject.put("apikey", getString(R.string.apikey));
                jsonObject.put("category", data.getString("category"));
                jsonObject.put("subcategory", data.getString("subcategory"));
                jsonObject.put("package", userPackage.getId());
                jsonObject.put("task_WDuration", preferences.getString("quanOfWork", "0"));
                jsonObject.put("latitude", location.getLatitude());
                jsonObject.put("longitude", location.getLongitude());
                jsonObject.put("task_title", "");
                jsonObject.put("applied_coupen", "");
                jsonObject.put("userToken", preferences.getString("userToken", "0"));
                jsonObject.put("created_by", preferences.getString("user_id", "0"));
                JSONObject requestData = new JSONObject();
                requestData.put("RequestData", jsonObject);
                Log.i("requestDataCreate", "Create Request : " + jsonObject.toString());
                sendHttpRequest(requestData);
               /* preferences.edit()
                        .putString("from_book_screen", jsonObject.toString())
                        .putBoolean("createRequest", true)
                        .apply();*/
            } catch (Exception e) {
                Log.e("responseDataError", "Error brom Booking : " + e);
            }
        } else {
            startLoginActivity(true);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(context, data);
            LatLng latLng = place.getLatLng();
            if (marker != null)
                marker.remove();
            client.removeLocationUpdates(mLocationCallback);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.visible(true)
                    .title(preferences.getString("fullname", "You"))
                    .position(latLng);
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.if_blue_pin_68011));
            marker = googleMap.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            googleMap.moveCamera(cameraUpdate);
            Location location = new Location("A");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            if (userPackage != null)
                getAvailableServiceProvider(location, userPackage);
            this.location = location;
        }
        if (requestCode == BOOK_LATAR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i("successLoginLater", preferences.getString("user_id", "0"));
            setLogin();
            hitBookLater();
        }
        if (requestCode == BOOK_NOW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i("successLoginNow", preferences.getString("user_id", "0"));
            setLogin();
            hitBookNow();
        }
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin() {

        isLogin = preferences.getBoolean("isLogin", false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCameraMove() {

        linearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCameraMoveCanceled() {
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCameraIdle() {
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(context, "Toast here : ", Toast.LENGTH_LONG).show();


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) return;
                double lat=location.getLatitude();
                double lng=location.getLongitude();
                LatLng latLng=new LatLng(lat, lng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                changeLocation(location);
            }
        });
        return true;
    }



    @Override
    public void onPause() {
        super.onPause();
        if(this.homeMenuItem!=null){
            this.homeMenuItem.setVisible(false);
        }
        if(this.locationMenuItem!=null){
            this.locationMenuItem.setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.homeMenuItem=menu.findItem(R.id.home_menu_home);
        this.locationMenuItem=menu.findItem(R.id.location_menu);
        this.locationMenuItem.setVisible(true);
        this.homeMenuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.location_menu:
                PlaceAutocomplete.IntentBuilder intentBuilder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);
                try {
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.home_menu_home:
                    //getFragmentManager().popBackStack();
                HomeFragment fragment=new HomeFragment();
                    fragment.setHasOptionsMenu(true);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_container, fragment, "Home")
                        .addToBackStack("Home")
                        .commit();

                break;
        }

        return true;
    }
}