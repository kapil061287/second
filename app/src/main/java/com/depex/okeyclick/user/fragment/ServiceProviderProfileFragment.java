package com.depex.okeyclick.user.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.braintreepayments.api.Json;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.model.BookLaterServiceProvider;

import com.depex.okeyclick.user.screens.InvoiceActivity;
import com.hedgehog.ratingbar.RatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.stripe.model.Review;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceProviderProfileFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final int INVOICE_REQUEST_CODE = 1;

    @BindView(R.id.view_pager_profile)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    MyReciever myReciever;
    String spId;
    SectionPagerAdapter pagerAdapter;
    ProgressBar progressBar;

    @BindView(R.id.user_image_provider_profile)
    ImageView roundedImageView;


    @BindView(R.id.service_provider_name)
    TextView serviceProviderName;

    @BindView(R.id.per_hour_price)
    TextView perHourPrice;

    @BindView(R.id.star_view_profile)
    RatingBar ratingBar;


    @BindView(R.id.book_now_btn)
    Button bookNow;
    private Context context;

    @BindView(R.id.parant_layout)
    ConstraintLayout parantLayout;

    BookLaterServiceProvider bookLaterServiceProvider;
    SharedPreferences preferences;
    private boolean isPaymentSucceed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_service_provider_profile_fragment, container, false);
        ButterKnife.bind(this, view);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        bookNow.setOnClickListener(this);
        registerReciever();
        String json=getArguments().getString("json");
        preferences=context.getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        bookLaterServiceProvider=BookLaterServiceProvider.fromJson(json);
        getProfile(bookLaterServiceProvider);
        return view;
    }

    private void getProfile(BookLaterServiceProvider bookLaterServiceProvider) {
        serviceProviderName.setText(bookLaterServiceProvider.getName()+" "+bookLaterServiceProvider.getLastName());
        ratingBar.setStar(3);
        perHourPrice.setText(bookLaterServiceProvider.getPricePerHour());
        GlideApp.with(context).load(bookLaterServiceProvider.getImageUrl()).circleCrop().into(roundedImageView);
        ReviewsServiceProviderFragment reviewsServiceProviderFragment= ReviewsServiceProviderFragment.getInstance(bookLaterServiceProvider);
        DetailsServiceProviderFragment detailsServiceProviderFragment=DetailsServiceProviderFragment.getInstance(bookLaterServiceProvider);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(reviewsServiceProviderFragment);
        fragments.add(detailsServiceProviderFragment);
        SectionPagerAdapter adapter=new SectionPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        //DetailsSpProfileFragment detailsSpProfileFragment=DetailsSpProfileFragment.getInstance(bookLaterServiceProvider);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.book_now_btn:
                    bookNowProcess();
                break;
        }
    }


    private void bookNowProcess() {
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog.Builder(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
                ).build();
        Calendar calendar1=Calendar.getInstance();
        calendar1.add(Calendar.DATE, 7);
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.setMaxDate(calendar1);
        datePickerDialog.show(getFragmentManager(),"pickDate");


       /* BottomSheetDialog dialog=new BottomSheetDialog(context);
        View view=LayoutInflater.from(context).inflate(R.layout.content_book_letar_bottom_sheet,parantLayout, false);
        dialog.setContentView(view);
       *//* Button button=view.findViewById(R.id.choose_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
//        dialog.show();
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, final int year, final int monthOfYear, final int dayOfMonth) {

        new AlertDialog.Builder(context).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                scheduleTask(year, monthOfYear, dayOfMonth);
            }
        }).setTitle("Confirm Schedule")
                .setNegativeButton("Cancel", null)
                .setMessage(R.string.booking_date_confirm_msg)
                .create()
                .show();
    }

    public void scheduleTask(int year, int monthOfYear, int dayOfMonth){


        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        String json=getArguments().getString("json2");

        Log.i("responseData", "Json 2 : "+json);
        try {
            JSONObject request=new JSONObject(json);
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("category", request.getString("category"));
            data.put("subcategory", request.getString("subcategory"));
            data.put("package", request.getString("package"));
            data.put("latitude", request.getString("lat"));
            data.put("longitude", request.getString("lng"));
            data.put("task_title", "");
            data.put("applied_coupen","");
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("sp_id", bookLaterServiceProvider.getId());
            data.put("created_by", preferences.getString("user_id", "0"));
            data.put("task_WDuration", request.getString("task_WDuration"));
            data.put("created_date", year+"-"+monthOfYear+"-"+dayOfMonth+" "+"00:00:00");
            requestData.put("RequestData", data);
            Log.i("requestData", "Book Letar Process : "+requestData.toString());

            new Retrofit.Builder()
                    .baseUrl(Utils.SITE_URL)
                    .addConverterFactory(new StringConvertFactory())
                    .build()
                    .create(ProjectAPI.class)
                    .bookLetarReq(requestData.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseString=response.body();
                            Log.i("responseData", "Book Letar Process : "+responseString);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class SectionPagerAdapter  extends FragmentPagerAdapter {
        List<Fragment> list;
        public SectionPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list=list;
        }


        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    public static ServiceProviderProfileFragment getInstance(BookLaterServiceProvider bookLaterServiceProvider, String json){
        ServiceProviderProfileFragment fragment=new ServiceProviderProfileFragment();
        Bundle bundle=new Bundle();
        bundle.putString("json", bookLaterServiceProvider.toJson());
        //json 2 for package category subcategory
        bundle.putString("json2", json);
        fragment.setArguments(bundle);
        return fragment;
    }

    class  MyReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase(Utils.CONFIRM_SP_TIME)) {
                String task_id=intent.getExtras().getString("task_id");
                showInvoice(task_id);
            }
        }
    }

    private void showInvoice(String task_id) {

        preferences.edit().putString("task_id", task_id).apply();

        JSONObject data=new JSONObject();
        JSONObject requestData=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", task_id);
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
                                    for(Iterator<String> keys = resObj.keys(); keys.hasNext();){
                                        String key=keys.next();
                                        bundle.putString(key, resObj.getString(key));

                                    }
                                    bundle.putBoolean("auto", false);
                                    bundle.putBoolean("paidStatus", isPaymentSucceed);
                                    startInvoice(bundle);
                                }
                            } catch (JSONException e) {
                                Log.i("responseDataError", e.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("responseDataError", "Job Notification : "+t.toString());
                        }
                    });
        } catch (JSONException e) {
            Log.e("responseDataError", "Job Notification : "+e.toString());
        }
    }


    private void startInvoice(Bundle bundle) {
        Intent intent=new Intent(context, InvoiceActivity.class);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, INVOICE_REQUEST_CODE);
    }
    private void registerReciever(){

        IntentFilter filter=new IntentFilter(Utils.CONFIRM_SP_TIME);
        myReciever=new MyReciever();
        LocalBroadcastManager.getInstance(context).registerReceiver(myReciever, filter);
    }
    public void unregisterProvider(){
        if (myReciever!=null)
        LocalBroadcastManager.getInstance(context).unregisterReceiver(myReciever);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterProvider();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==INVOICE_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                isPaymentSucceed=true;
                showAlert(getString(R.string.booking_confirmed_msg), getString(R.string.booking_confirm_title));
            }
        }
    }

    public void showAlert(String msg, String title){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, null).create().show();
    }
}