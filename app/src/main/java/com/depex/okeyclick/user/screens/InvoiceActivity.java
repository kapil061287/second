package com.depex.okeyclick.user.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Json;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.GooglePaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.CouponAdapter;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.model.BookLaterServiceProvider;
import com.depex.okeyclick.user.model.Coupon;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.job_id_txt)
    TextView jobId;

    @BindView(R.id.total_amount_txt)
    TextView totalAmount;

    @BindView(R.id.customer_name_txt)
    TextView customerName;

    @BindView(R.id.date_txt)
    TextView dateInvoice;

    @BindView(R.id.sub_total_txt)
    TextView subTotal;

    @BindView(R.id.basic_fare_txt)
    TextView basicFare;

    @BindView(R.id.service_hours_txt)
    TextView serviceHours;

    @BindView(R.id.service_tex_txt)
    TextView serviceTex;

    @BindView(R.id.city_tex_txt)
    TextView cityTex;

    @BindView(R.id.payment_btn)
    Button paymentBtn;

    @BindView(R.id.total_amount_txt2)
    TextView totalAmount2;

    @BindView(R.id.apply_btn)
    Button applyBtn;

    @BindView(R.id.cancel_coupen)
    Button cancelBtn;

    @BindView(R.id.apply_coupen)
    EditText applyCouponEdit;

    @BindView(R.id.parent_constraint_layout)
    ConstraintLayout parentConstraintLayout;

    @BindView(R.id.want_coupon)
    Button wantCoupon;
    @BindView(R.id.paid_image_view)
    ImageView paidImageView;

    @BindView(R.id.coupen_text)
    TextView coupenText;

    @BindView(R.id.apply_coupon_minus)
    TextView applyCoupenMinus;

    @BindView(R.id.apply_coupon_minus_linear_layout)
    LinearLayout applyCouponMinusLinearLayout;

    @BindView(R.id.cancel_coupon_linear_layout)
    LinearLayout cancelCouponLinearLayout;

    SharedPreferences preferences;

    @BindView(R.id.apply_coupon_linear_layout)
    LinearLayout applyCouponLinearLayout;

    String taskId;

    String subtotalStr;
    String serviceTaxStr;
    String cityTaxStr;
    String baseFareStr;
    String taskDurationStr;
    String totalStr;
    String customerNameStr;
    boolean automatic;
    String createdDateStr;
    String taskKeyStr;
    String updateTotal;
    String updateSubTotal;
    private String subCategoryPriceStr;
    private String packagePriceStr;
    private String spPriceStr;
    //private String paymentStatus;

    private String currencySymbol;

    private boolean isPaymentSucceed;
    private boolean paymentDone;
    String adminCommissionStr;
    String spTotalStr;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Invoice");
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title_color));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ButterKnife.bind(this);
        bundle=new Bundle();

        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        applyBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            Toast.makeText(this, "Sorry Invalid Invoice", Toast.LENGTH_LONG).show();
            return;
        }
        paymentDone=bundle.getBoolean("paidStatus");
        isPaymentSucceed=paymentDone;
        if(paymentDone){
            setVisiblity(View.GONE, paymentBtn, coupenText);
            setVisiblity(View.VISIBLE, paidImageView);
            GlideApp.with(this).load(R.drawable.paid).into(paidImageView);
        }

        taskId=preferences.getString("task_id", "");
        subtotalStr=bundle.getString("subtotal");
        currencySymbol=getString(R.string.uro);
        serviceTaxStr=bundle.getString("service_tax");
        baseFareStr=bundle.getString("base_fare");
        cityTaxStr=bundle.getString("city_tax");
        taskDurationStr=bundle.getString("task_WDuration");
        totalStr=bundle.getString("total");
        customerNameStr=bundle.getString("cs_name");
        createdDateStr=bundle.getString("created_date");
        taskKeyStr=taskId;
        //taskKeyStr=bundle.getString("task_id");
        subCategoryPriceStr=bundle.getString("subcategory_price");
        packagePriceStr=bundle.getString("package_price");
        spPriceStr=bundle.getString("sp_price");
       // paymentStatus=bundle.getString("paymentStatus");
        spTotalStr=bundle.getString("sp_total");
        adminCommissionStr=bundle.getString("admin_commission");
        //initPaymentStatus(paymentStatus);

        initPaymentStatus(isPaymentSucceed);


       // this.subTotal.setText(subtotalStr);
        this.subTotal.setText(currencySymbol+formatIn2Digit(subtotalStr));
        this.serviceHours.setText(taskDurationStr);
        this.totalAmount.setText("Total Amount\n"+currencySymbol+formatIn2Digit(totalStr));
        this.basicFare.setText(currencySymbol+formatIn2Digit(baseFareStr));
        this.cityTex.setText(cityTaxStr+"%");
       // this.serviceTex.setText(serviceTaxStr+"%");
        this.jobId.setText("Job ID : "+taskKeyStr);
        this.customerName.setText(customerNameStr);
        this.dateInvoice.setText(createdDateStr);
        this.totalAmount2.setText(currencySymbol+formatIn2Digit(totalStr));
        paymentBtn.setOnClickListener(this);
        wantCoupon.setOnClickListener(this);
    }

   /* private void initPaymentStatus(String paymentStatus) {
        if(paymentStatus!=null){
            if(paymentStatus.equalsIgnoreCase("paid")){
                setVisiblity(View.GONE, applyCouponLinearLayout, paymentBtn, cancelBtn, wantCoupon);
                //setVisiblity(View.VISIBLE, findViewById(R.id.paid_txt));
            }
        }
    }*/
   private void initPaymentStatus(boolean paidStatus) {
           if(paidStatus){
               setVisiblity(View.GONE, applyCouponLinearLayout, paymentBtn, cancelBtn, wantCoupon);
               setVisiblity(View.VISIBLE,paidImageView);
               GlideApp.with(this).load(R.drawable.paid).into(paidImageView);
               //setVisiblity(View.VISIBLE, findViewById(R.id.paid_txt));
           }
   }

    private String formatIn2Digit(String subtotalStr) {
        double d=Double.parseDouble(subtotalStr);
        NumberFormat numberFormat=NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(d);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_coupen:
                cancelCoupon(coupenText.getText().toString());
                break;
            case R.id.apply_btn:
                //applyCoupen(applyCouponEdit.getText().toString());
                break;
            case R.id.payment_btn:
               // onBrainTreeSubmit();
                bundle.putString("total", totalStr);
               bundle.putString("admin_commission", adminCommissionStr);
               bundle.putString("sp_total", spTotalStr);
                if(!isPaymentSucceed) {
                    onStripePayment(bundle);
                }else {
                    setResult(RESULT_OK);
                    finish();
                }

                break;
            case R.id.want_coupon:
                showCouponList();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(isPaymentSucceed) {
            setResult(RESULT_OK);
        }else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void onStripePayment(Bundle bundle) {

        Intent intent=new Intent(this, PaymentActivity.class);
        if(bundle!=null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, 2);
    }

    private void showCouponList() {
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            requestData.put("RequestData", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Retrofit.Builder()
                    .addConverterFactory(new StringConvertFactory())
                    .baseUrl(Utils.SITE_URL)
                    .build()
                    .create(ProjectAPI.class)
                    .getCouponList(requestData.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseString=response.body();
                            Log.i("responseData", "Invoice CouponList : "+responseString );
                            try {
                                JSONObject res=new JSONObject(responseString);
                                boolean success=res.getBoolean("successBool");
                                if(success){
                                    JSONObject resObj=res.getJSONObject("response");
                                    JSONArray arr=resObj.getJSONArray("List");
                                    Gson gson=new Gson();
                                    Coupon[]couponArr=gson.fromJson(arr.toString(), Coupon[].class);
                                    List<Coupon> couponList= Arrays.asList(couponArr);
                                    showAlertForCoupon(couponList);
                                }else {
                                    showAlertForNoCoupon();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            showCouponList();
                        }
                    });
    }

    private void showAlertForNoCoupon() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_coupon))
                .setMessage(getString(R.string.no_coupon_msg))
                .setPositiveButton("OK", null)
                .create().show();
    }

    private void showAlertForCoupon(List<Coupon> couponList) {
        final List<Coupon> coupons=couponList;
        final BottomSheetDialog dialog=new BottomSheetDialog(this);
        dialog.setTitle("Choose Coupon");
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.content_bottom_sheet_coupon_layout, parentConstraintLayout, false);
        ListView listView=view.findViewById(R.id.list_content_bottom_sheet_coupen);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(InvoiceActivity.this,coupons.get(i).getCouponKey(), Toast.LENGTH_LONG ).show();
                String coupenKey=coupons.get(i).getCouponKey();
                dialog.dismiss();
                applyCoupen(coupenKey);
            }
        });
        CouponAdapter adapter=new CouponAdapter(couponList, this);
        listView.setAdapter(adapter);
        dialog.setContentView(view);
        dialog.show();
    }



    public void applyCoupen(final String coupen){
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();

        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", taskId);
            data.put("subtotal", subtotalStr);
            data.put("total", totalStr);
            data.put("city_tax", cityTaxStr);
            data.put("service_tax", serviceTaxStr);
            data.put("applied_coupen", coupen);
            requestData.put("RequestData", data);
            Log.i("requestData", "Coupon Request : "+requestData.toString());


            new Retrofit.Builder()
                    .baseUrl(Utils.SITE_URL)
                    .addConverterFactory(new StringConvertFactory())
                    .build()
                    .create(ProjectAPI.class)
                    .appliedCoupen(requestData.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseString=response.body();
                            Log.i("responseData", "Apply Coupon : "+responseString);
                            try {
                                JSONObject res=new JSONObject(responseString);
                                boolean success=res.getBoolean("successBool");

                                if(success){
                                    coupenText.setText(coupen);
                                    JSONObject resObj=res.getJSONObject("response");
                                    //double value=resObj.getDouble("flat_discount");
                                    //String subTotal=InvoiceActivity.this.subTotal.getText().toString();
                                    //double subTotalFloatValue=Float.parseFloat(subTotal);
                                   /* double remainAmount=0;
                                    double minusAmount=0;*/
                                    float subTotalFloatValue=Float.parseFloat(subtotalStr);
                                   float flatDiscount= (float) resObj.getDouble("flat_discount");
                                    NumberFormat numberFormat=NumberFormat.getInstance();
                                    numberFormat.setMaximumFractionDigits(2);

                                    //if 1 than precentage if 2 than money
                                    switch (resObj.getInt("discount_type")){
                                        case 1:
                                            flatDiscount=  subTotalFloatValue*(flatDiscount/100);

                                            applyCoupenMinus.setText("-"+numberFormat.format(flatDiscount));
                                            /*if(subTotalFloatValue>0){
                                                minusAmount=subTotalFloatValue*(value/100);
                                                remainAmount=subTotalFloatValue-minusAmount;
                                            }*/
                                            break;
                                        case 2:
                                            applyCoupenMinus.setText("-"+numberFormat.format(flatDiscount));
                                            /*if(subTotalFloatValue>0) {
                                                remainAmount=subTotalFloatValue-value;
                                                minusAmount=value;
                                            }*/
                                            break;
                                    }

                                    updateSubTotal=numberFormat.format(resObj.getDouble("subtotal"));
                                    updateTotal=numberFormat.format(resObj.getDouble("total"));
                                    InvoiceActivity.this.subTotal.setText(updateSubTotal);
                                    totalAmount2.setText(updateTotal);
                                    totalAmount2.setText(updateTotal);
                                    wantCoupon.setVisibility(View.GONE);

                                    //Remain amout show

                                    setVisiblity( View.VISIBLE ,applyCouponMinusLinearLayout, cancelCouponLinearLayout);
                                    //setVisiblity(View.GONE, applyCouponLinearLayout);

                                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                               cancelCoupon(coupenText.getText().toString());
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            applyCoupen(coupen);
                                Log.e("responseDataError", t.toString());
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2){
            if(resultCode==RESULT_OK){
                isPaymentSucceed=data.getExtras().getBoolean("pay");
                if(isPaymentSucceed){
                    paymentBtn.setText("Done");

                    paymentBtn.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                setResult(RESULT_OK);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    onPaid(isPaymentSucceed);
                }else {
                    onPaid(isPaymentSucceed);
                }

            }
        }
    }

    private void onPaid(boolean paid) {
        if(paid) {
            setVisiblity(View.GONE, applyCouponLinearLayout, cancelBtn,paymentBtn, wantCoupon);
            setVisiblity(View.VISIBLE, findViewById(R.id.paid_txt));
            sendConfirmationToSp("success");
            GlideApp.with(this).load(R.drawable.paid).into(paidImageView);
        }else {
                Toast.makeText(this, "Payment is not succeed !", Toast.LENGTH_LONG).show();
        }
    }

    private void sendConfirmationToSp(final String success) {
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", preferences.getString("task_id", "0"));
            data.put("subcategory_price", subCategoryPriceStr);
            data.put("package_price", packagePriceStr);
            data.put("sp_price", spPriceStr);
            data.put("city_tax", cityTaxStr);
            data.put("service_tax", serviceTaxStr);
            data.put("base_fare", baseFareStr);
            data.put("subtotal", subtotalStr);
            data.put("total", totalStr);
            data.put("charge_id", preferences.getString("charge_id", "0"));
            data.put("payment_method", "Stripe");
            data.put("payment_status", success);
            data.put("applied_coupon", applyCouponEdit.getText().toString());
            data.put("task_WDuration", taskDurationStr);
            requestData.put("RequestData", data);
            Log.i("requestData", "Confirm Payment API : "+requestData.toString());

        } catch (JSONException e) {
            Log.e("responseDataError", "Send Confirmation To Sp : "+e.toString());
        }

        new Retrofit.Builder()
                .addConverterFactory(new StringConvertFactory())
                .baseUrl(Utils.SITE_URL)
                .build()
                .create(ProjectAPI.class)
                .paymentProcess(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString=response.body();
                        Log.i("responseData", "Confirm Sp Payment : "+responseString+"");
                        if(responseString==null)return;
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                //startReviewBottomSheet();
                            }
                        } catch (JSONException e) {
                            Log.e("responseDataError", "Send Confirmation to SP : "+e.toString());
                        }
                    }


                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        sendConfirmationToSp(success);
                            Log.i("responseDataError", "Send Confirmation TO SP : "+t.toString());
                    }
                });
    }



    public void setVisiblity(int visibility, View... v){
        for(View view : v){
            view.setVisibility(visibility);
        }
    }



 public void  cancelCoupon(final String coupon){
        JSONObject requestData=new JSONObject();
         JSONObject data=new JSONObject();
     try {
         data.put("v_code", getString(R.string.v_code));
         data.put("apikey", getString(R.string.apikey));
         data.put("userToken", preferences.getString("userToken", "0"));
         data.put("user_id", preferences.getString("user_id", "0"));
         data.put("task_id",taskId);
         data.put("applied_coupen", coupon);
         data.put("subtotal", subtotalStr);
         data.put("total", totalStr);
         requestData.put("RequestData", data);
         Log.i("requestData", "Cancel Coupon : "+requestData.toString());

         new Retrofit.Builder()
                 .addConverterFactory(new StringConvertFactory())
                 .baseUrl(Utils.SITE_URL)
                 .build()
                 .create(ProjectAPI.class)
                 .cancelCoupen(requestData.toString())
                 .enqueue(new Callback<String>() {
                     @Override
                     public void onResponse(Call<String> call, Response<String> response) {
                         String responseString=response.body();
                         try {
                             JSONObject res=new JSONObject(responseString);
                             boolean success=res.getBoolean("successBool");
                             if(success){
                                 updateTotal=null;
                                 updateSubTotal=null;
                                 totalAmount2.setText(formatIn2Digit(totalStr));
                                 totalAmount.setText(formatIn2Digit(totalStr));
                                 subTotal.setText(formatIn2Digit(subtotalStr));
                                 setVisiblity(View.GONE, applyCouponMinusLinearLayout, cancelCouponLinearLayout);
                                 //setVisiblity(View.VISIBLE, applyCouponLinearLayout);
                                 setVisiblity(View.VISIBLE, wantCoupon);
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                     }

                     @Override
                     public void onFailure(Call<String> call, Throwable t) {
                         cancelCoupon(coupon);
                            Log.e("responseDataError","Cancel Coupon : "+t.toString());
                     }
                 });

     } catch (JSONException e) {
         Log.e("responseDataError", "Invoice Activity"+e.toString());
     }
 }

}