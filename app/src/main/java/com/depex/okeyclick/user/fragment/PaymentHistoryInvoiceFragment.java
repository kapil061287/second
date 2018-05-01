package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.model.PaymentHistoryModal;
import com.depex.okeyclick.user.model.TaskDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PaymentHistoryInvoiceFragment extends Fragment {

    @BindView(R.id.service_hours_txt)
    TextView serviceHours;

    @BindView(R.id.basic_fare_txt)
    TextView baseFare;

    @BindView(R.id.sub_total_txt)
    TextView subTotal;

    @BindView(R.id.city_tex_txt)
    TextView cityText;

    @BindView(R.id.total_amount_txt2)
    TextView totalAmount2;

    @BindView(R.id.total_amount_txt)
    TextView totalAmount;

    @BindView(R.id.paid_image_view)
    ImageView paidImageView;

    @BindView(R.id.want_coupon)
    Button wantCoupen;


    @BindView(R.id.payment_btn)
    Button paymentBtn;
    private Context context;

    @BindView(R.id.job_id_txt)
    TextView jobId;

    @BindView(R.id.customer_name_txt)
    TextView serviceProviderName;

    @BindView(R.id.date_txt)
    TextView dateText;

    SpotsDialog dialog;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_invoice, container, false);
        ButterKnife.bind(this, view);
        setVisibility(View.GONE, paymentBtn, wantCoupen);
        setVisibility(View.VISIBLE, paidImageView);
        dialog=new SpotsDialog(context);
        dialog.show();
        GlideApp.with(context).load(R.drawable.paid).into(paidImageView);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            String json=bundle.getString("json");
            PaymentHistoryModal modal= new Gson().fromJson(json, PaymentHistoryModal.class);
            initScreen(modal);
        }
        return view;
    }

    private void initScreen(PaymentHistoryModal modal) {


        SharedPreferences preferences=context.getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", context.getString(R.string.v_code));
            data.put("apikey", context.getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", modal.getTaskId());
            requestData.put("RequestData", data);
        }catch(Exception e){
            Log.e("responseDataError", e.toString());
        }


        new Retrofit.Builder()
                .addConverterFactory(new StringConvertFactory())
                .baseUrl(Utils.SITE_URL)
                .build()
                .create(ProjectAPI.class)
                .taskDetails(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString=response.body();
                        Log.i("responseData","Task Details API : "+responseString);
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success) {
                                dialog.dismiss();
                                JSONObject resObj=res.getJSONObject("response");
                                TaskDetail detail=new GsonBuilder()
                                        .setDateFormat(getString(R.string.date_time_format_from_web))
                                        .create()
                                        .fromJson(resObj.toString(), TaskDetail.class);


                                totalAmount.setText(getString(R.string.uro)+detail.getTotal());
                                totalAmount2.setText(getString(R.string.uro)+detail.getTotal());
                                subTotal.setText(getString(R.string.uro)+detail.getSubTotal());
                                serviceProviderName.setText(detail.getSpName());
                                cityText.setText(detail.getCityTax()+"%");
                                baseFare.setText(getString(R.string.uro)+detail.getBaseFare());
                                serviceHours.setText(detail.getWorkDuration());
                                jobId.setText("JOB ID : "+detail.getTaskId());


                            }

                            }catch (Exception e){
                            Log.e("responseDataError", "payemnt invoice Error : "+e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    private void setVisibility(int visibility, View... views ){
        for(View view : views){
            view.setVisibility(visibility);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public static PaymentHistoryInvoiceFragment getInstance(PaymentHistoryModal modal){
        PaymentHistoryInvoiceFragment fragment=new PaymentHistoryInvoiceFragment();
        Bundle bundle=new Bundle();
        bundle.putString("json", new Gson().toJson(modal));
        fragment.setArguments(bundle);
        return fragment;
    }
}