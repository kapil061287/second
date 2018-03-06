package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Collections;

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
    String createdDateStr;
    String taskKeyStr;
    String updateTotal;
    String updateSubTotal;
    private String subCategoryPriceStr;
    private String packagePriceStr;
    private String spPriceStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Invoice");
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        applyBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            Toast.makeText(this, "Sorry Invalid Invoice", Toast.LENGTH_LONG).show();
            return;
        }
        taskId=bundle.getString("task_id");
        subtotalStr=bundle.getString("subtotal");
        serviceTaxStr=bundle.getString("service_tax");
        baseFareStr=bundle.getString("base_fare");
        cityTaxStr=bundle.getString("city_tax");
        taskDurationStr=bundle.getString("task_WDuration");
        totalStr=bundle.getString("total");
        customerNameStr=bundle.getString("cs_name");
        createdDateStr=bundle.getString("created_date");
        taskKeyStr=bundle.getString("task_key");
        subCategoryPriceStr=bundle.getString("subcategory_price");
        packagePriceStr=bundle.getString("package_price");
        spPriceStr=bundle.getString("sp_price");




       // this.subTotal.setText(subtotalStr);
        this.subTotal.setText(formatIn2Digit(subtotalStr));
        this.serviceHours.setText(taskDurationStr);
        this.totalAmount.setText("Total Amount\n"+formatIn2Digit(totalStr));
        this.basicFare.setText(formatIn2Digit(baseFareStr));
        this.cityTex.setText(cityTaxStr+"%");
        this.serviceTex.setText(serviceTaxStr+"%");
        this.jobId.setText("Job ID : "+taskKeyStr);
        this.customerName.setText(customerNameStr);
        this.dateInvoice.setText(createdDateStr);
        this.totalAmount2.setText(formatIn2Digit(totalStr));
        paymentBtn.setOnClickListener(this);
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
                applyCoupen(applyCouponEdit.getText().toString());
                break;
            case R.id.payment_btn:
               // onBrainTreeSubmit();
                onPaid("Success");
                break;
        }
    }

    private void onBrainTreeSubmit() {

        String mAuthrization="eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI4YmUyZTcyODFmZDEyZTAzYWRmMWVmNTRlN2E2Mjg1YzgwNTI1ZDBiNzBmMmE5MjdmNjMyMGVjMGJlNTc1MWIwfGNyZWF0ZWRfYXQ9MjAxOC0wMi0yMlQwNTo1MTo0Mi40NzQyMDgyMjUrMDAwMFx1MDAyNm1lcmNoYW50X2lkPXFneWJobmdxd3R5bndibWtcdTAwMjZwdWJsaWNfa2V5PWdtaHltd3g2NnN4YjJ2ZGYiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvcWd5YmhuZ3F3dHlud2Jtay9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL3FneWJobmdxd3R5bndibWsvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tL3FneWJobmdxd3R5bndibWsifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiRGVwZXggVGVjaG5vbG9naWVzIiwiY2xpZW50SWQiOm51bGwsInByaXZhY3lVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vcHAiLCJ1c2VyQWdyZWVtZW50VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3RvcyIsImJhc2VVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFzc2V0c1VybCI6Imh0dHBzOi8vY2hlY2tvdXQucGF5cGFsLmNvbSIsImRpcmVjdEJhc2VVcmwiOm51bGwsImFsbG93SHR0cCI6dHJ1ZSwiZW52aXJvbm1lbnROb05ldHdvcmsiOnRydWUsImVudmlyb25tZW50Ijoib2ZmbGluZSIsInVudmV0dGVkTWVyY2hhbnQiOmZhbHNlLCJicmFpbnRyZWVDbGllbnRJZCI6Im1hc3RlcmNsaWVudDMiLCJiaWxsaW5nQWdyZWVtZW50c0VuYWJsZWQiOnRydWUsIm1lcmNoYW50QWNjb3VudElkIjoiZGVwZXh0ZWNobm9sb2dpZXMiLCJjdXJyZW5jeUlzb0NvZGUiOiJVU0QifSwibWVyY2hhbnRJZCI6InFneWJobmdxd3R5bndibWsiLCJ2ZW5tbyI6Im9mZiJ9";
        try {
            BraintreeFragment fragment=BraintreeFragment.newInstance(this, mAuthrization);
            PayPal.authorizeAccount(fragment);
        } catch (InvalidArgumentException e) {
           Log.e("responseData", "Error in Braintree Fragment : "+e.toString());
        }


        /*DropInRequest dropInRequest=new DropInRequest().clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI4YmUyZTcyODFmZDEyZTAzYWRmMWVmNTRlN2E2Mjg1YzgwNTI1ZDBiNzBmMmE5MjdmNjMyMGVjMGJlNTc1MWIwfGNyZWF0ZWRfYXQ9MjAxOC0wMi0yMlQwNTo1MTo0Mi40NzQyMDgyMjUrMDAwMFx1MDAyNm1lcmNoYW50X2lkPXFneWJobmdxd3R5bndibWtcdTAwMjZwdWJsaWNfa2V5PWdtaHltd3g2NnN4YjJ2ZGYiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvcWd5YmhuZ3F3dHlud2Jtay9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL3FneWJobmdxd3R5bndibWsvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tL3FneWJobmdxd3R5bndibWsifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiRGVwZXggVGVjaG5vbG9naWVzIiwiY2xpZW50SWQiOm51bGwsInByaXZhY3lVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vcHAiLCJ1c2VyQWdyZWVtZW50VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3RvcyIsImJhc2VVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFzc2V0c1VybCI6Imh0dHBzOi8vY2hlY2tvdXQucGF5cGFsLmNvbSIsImRpcmVjdEJhc2VVcmwiOm51bGwsImFsbG93SHR0cCI6dHJ1ZSwiZW52aXJvbm1lbnROb05ldHdvcmsiOnRydWUsImVudmlyb25tZW50Ijoib2ZmbGluZSIsInVudmV0dGVkTWVyY2hhbnQiOmZhbHNlLCJicmFpbnRyZWVDbGllbnRJZCI6Im1hc3RlcmNsaWVudDMiLCJiaWxsaW5nQWdyZWVtZW50c0VuYWJsZWQiOnRydWUsIm1lcmNoYW50QWNjb3VudElkIjoiZGVwZXh0ZWNobm9sb2dpZXMiLCJjdXJyZW5jeUlzb0NvZGUiOiJVU0QifSwibWVyY2hhbnRJZCI6InFneWJobmdxd3R5bndibWsiLCJ2ZW5tbyI6Im9mZiJ9")
                .amount(totalAmount2.getText().toString());
        dropInRequest
                .collectDeviceData(true)
                .googlePaymentRequest(getGooglePaymentRequest())
                .paypalAdditionalScopes(Collections.singletonList(PayPal.SCOPE_ADDRESS))
                .androidPayCart(getAndroidPayCart())
                .androidPayShippingAddressRequired(true)
                .androidPayPhoneNumberRequired(true)
                .androidPayAllowedCountriesForShipping("US");
        Intent dropInIntent=dropInRequest.getIntent(this);
        startActivityForResult(dropInIntent, 1);*/
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

                                    //if 1 than precentage if 2 than money
                                    switch (resObj.getInt("discount_type")){
                                        case 1:
                                            flatDiscount=  subTotalFloatValue*(flatDiscount/100);
                                            applyCoupenMinus.setText("-"+flatDiscount);
                                            /*if(subTotalFloatValue>0){
                                                minusAmount=subTotalFloatValue*(value/100);
                                                remainAmount=subTotalFloatValue-minusAmount;
                                            }*/
                                            break;
                                        case 2:
                                            applyCoupenMinus.setText("-"+flatDiscount);
                                            /*if(subTotalFloatValue>0) {
                                                remainAmount=subTotalFloatValue-value;
                                                minusAmount=value;
                                            }*/
                                            break;
                                    }

                                    updateSubTotal=resObj.getString("subtotal");
                                    updateTotal=resObj.getString("total");
                                    InvoiceActivity.this.subTotal.setText(updateSubTotal);
                                    totalAmount2.setText(updateTotal);
                                    totalAmount2.setText(updateTotal);


                                    //Remain amout show

                                    setVisiblity( View.VISIBLE ,applyCouponMinusLinearLayout, cancelCouponLinearLayout);
                                    setVisiblity(View.GONE, applyCouponLinearLayout);

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
        if(requestCode==1){
            if(resultCode==RESULT_OK){

                DropInResult dropInResult=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodType paymentMethodType=dropInResult.getPaymentMethodType();
                if(paymentMethodType==PaymentMethodType.PAYPAL){
                    Toast.makeText(this,"Payment method is paypal", Toast.LENGTH_LONG).show();
                }
                PaymentMethodNonce paymentMethodNonce=dropInResult.getPaymentMethodNonce();
                String nonce=paymentMethodNonce.getNonce();
                //OnSuccess when the payment is maid from user
                onPaid("success");

                Log.i("responseData", "Nonce : "+nonce);

            }else {
                Exception error=(Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("dropInError", error.toString());
            }
        }
    }

    private void onPaid(String success) {
            setVisiblity(View.GONE, applyCouponLinearLayout, paymentBtn, cancelBtn);
            setVisiblity(View.VISIBLE, findViewById(R.id.paid_txt));
            BottomSheetDialog dialog=new BottomSheetDialog(this);
            sendConfirmationToSp("success");
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
            data.put("payment_method", "COD");
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
                                startReviewBottomSheet();
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

    private void startReviewBottomSheet() {

    }

    public void setVisiblity(int visibility, View... v){
        for(View view : v){
            view.setVisibility(visibility);
        }
    }
    private GooglePaymentRequest getGooglePaymentRequest() {

        return new GooglePaymentRequest()
                .transactionInfo(TransactionInfo.newBuilder()
                        .setTotalPrice("1.00")
                        .setCurrencyCode("USD")
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .build())
                .emailRequired(true);
    }

    private Cart getAndroidPayCart() {
        return Cart.newBuilder()
                .setCurrencyCode("USD")
                .setTotalPrice("1.00")
                .addLineItem(LineItem.newBuilder()
                        .setCurrencyCode("USD")
                        .setDescription("Description")
                        .setQuantity("1")
                        .setUnitPrice("1.00")
                        .setTotalPrice("1.00")
                        .build())
                .build();
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
                                 totalAmount2.setText(totalStr);
                                 totalAmount.setText(totalStr);
                                 subTotal.setText(subtotalStr);
                                 setVisiblity(View.GONE, applyCouponMinusLinearLayout, cancelCouponLinearLayout);
                                 setVisiblity(View.VISIBLE, applyCouponLinearLayout);
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
         e.printStackTrace();
     }
 }
}