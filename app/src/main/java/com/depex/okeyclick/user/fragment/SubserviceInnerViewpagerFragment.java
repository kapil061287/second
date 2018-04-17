package com.depex.okeyclick.user.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.braintreepayments.api.PayPal;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.InnerViewPagerRecyclerItemClickListener;
import com.depex.okeyclick.user.adpater.InnerViewPagerRecyclerViewAdapter;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.model.BookLaterServiceProvider;
import com.depex.okeyclick.user.model.SubService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public  class SubserviceInnerViewpagerFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener , InnerViewPagerRecyclerItemClickListener {


    private Context context;
    @BindView(R.id.date_btn)
    Button dateBtn;

    String[]arr;

    @BindView(R.id.time_btn)
    Button timeBtn;

    @BindView(R.id.inner_view_pager_recycler)
    RecyclerView innerViewPagerRecycler;

    SharedPreferences preferences;

    //Json From previous screen from book later btn category subcategory package
    String json;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_book_later_most_inner_fragment, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle=getArguments();
        json=bundle.getString("json");
       // Gson gson=new Gson();
        preferences=context.getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        arr=new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        //SubService subService=gson.fromJson(json, SubService.class);
        dateBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);
        initServiceProvider(json);
        return view;
    }

    private void initServiceProvider(final String json) {

        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();

        try {
            JSONObject request=new JSONObject(json);
            double lat=77.391;
            double lng=28.5355;
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("latitude", request.getString("lat"));
            data.put("package", request.getString("package"));
            data.put("longitue", request.getString("lng"));
            data.put("category", request.getString("category"));
            data.put("subcategory",request.getString("subcategory"));
            requestData.put("RequestData", data);
            Log.i("requestData", "BookLater Inner Fragment : "+requestData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Retrofit.Builder()
                .addConverterFactory(new StringConvertFactory())
                .baseUrl(Utils.SITE_URL)
                .build()
                .create(ProjectAPI.class)
                .getAllSp(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        String responseString=response.body();
                        Log.i("responseData", "Inner View Pager Fragment : "+responseString);
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                JSONArray arr=resObj.getJSONArray("List");

                                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-d H:m:s").create();
                                BookLaterServiceProvider[]bookLaterServiceProvidersArr=gson.fromJson(arr.toString(), BookLaterServiceProvider[].class);
                                List<BookLaterServiceProvider> bookLaterServiceProviders=new ArrayList<>(Arrays.asList(bookLaterServiceProvidersArr));
                                InnerViewPagerRecyclerViewAdapter adapter=new InnerViewPagerRecyclerViewAdapter(bookLaterServiceProviders, context, SubserviceInnerViewpagerFragment.this);
                                LinearLayoutManager manager=new LinearLayoutManager(context);
                                innerViewPagerRecycler.setLayoutManager(manager);
                                innerViewPagerRecycler.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            Log.e("responseData", "get_all_sp : "+e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("responseDataError", "Inner View Pager Fragment : "+t.toString());
                        initServiceProvider(json);
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onClick(View view) {
        Calendar calendar=Calendar.getInstance();
        switch (view.getId()){
            case R.id.date_btn:
                //DatePickerDialog

                int day=calendar.get(Calendar.DATE);
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);

                DatePickerDialog dialog=new DatePickerDialog(context, this, year, month, day);
                dialog.show();

                break;
            case R.id.time_btn:
                int hour=calendar.get(Calendar.HOUR);
                int min=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(context, this, hour, min, false);
                timePickerDialog.show();

                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dateBtn.setText(new StringBuilder().append(i2).append(" ").append(arr[i1]).append(" ").append(i).toString());
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        timeBtn.setText(new StringBuilder().append(i).append(":").append(i1).toString());
    }


    //Fragemnt click listener
    @Override
    public void onInnerViewPagerRecyclerItemClick(BookLaterServiceProvider provider) {
            ServiceProviderProfileFragment fragment=ServiceProviderProfileFragment.getInstance(provider, json);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_container, fragment)
                    .addToBackStack(null)
                    .commit();
    }
    public static SubserviceInnerViewpagerFragment getInstance(String json){
        SubserviceInnerViewpagerFragment fragment=new SubserviceInnerViewpagerFragment();
        Bundle bundle=new Bundle();
        bundle.putString("json", json);
        fragment.setArguments(bundle);
        return fragment;
    }
}