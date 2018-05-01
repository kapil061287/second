package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ApiListener;
import com.depex.okeyclick.user.api.CallbackApi;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.misc.StringUtils;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;


import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener , ApiListener<JsonObject>{

    @BindView(R.id.round_imge_signup)
    RoundedImageView roundImageSignup;

    @BindView(R.id.name_signup)
    EditText nameSignup;
    @BindView(R.id.password_signup)
    EditText passwordSignup;
    @BindView(R.id.last_name_signup)
    EditText lastName;
    @BindView(R.id.confim_password_signup)
    EditText confirmPassword;
    @BindView(R.id.mobile_num)
    EditText mobileNum;
    @BindView(R.id.email_signup)
    EditText emailSignup;
    String otpValue;
    @BindView(R.id.referral_code_signup)
    EditText referralCode;
    @BindView(R.id.otp_signup)
    EditText otpSignup;
    @BindView(R.id.request_otp)
    Button requestOtpBtn;
    @BindView(R.id.register_signup)
    Button registerSignup;
    ProjectAPI projectAPI;
    JSONObject sendJsonData;
    public final int PICK_IMAGE_CODE=1;

    SharedPreferences preferences;
    boolean bookLatar;
    boolean bookNow;

    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        setOnClickListener(roundImageSignup, registerSignup, requestOtpBtn);
        preferences=getSharedPreferences(Utils.SERVICE_PREF, MODE_PRIVATE);

        spotsDialog=new SpotsDialog(this);

        Bundle bundle=getIntent().getExtras();
        bookNow=bundle.getBoolean("bookNow");
        bookLatar=bundle.getBoolean("bookLater");
        Retrofit.Builder builder=new Retrofit.Builder();
        builder.baseUrl(Utils.SITE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        projectAPI=retrofit.create(ProjectAPI.class);

    }


    private void setOnClickListener(View... views) {
        for(View  view : views){
            view.setOnClickListener(this);
        }
    }


    String name;
    String password;
    String lastNameStr;
    String email;
    String confirmPass;
    String referralCodeStr;
    String mobile1;

    @Override
    public void onClick(View v) {
        boolean valid;
        switch (v.getId()){
            case R.id.round_imge_signup:
                Intent intent=new Intent();
                intent.setType("image/**");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_CODE);
                break;
            case R.id.register_signup:

                name=nameSignup.getText().toString();
                password=passwordSignup.getText().toString();
                lastNameStr=this.lastName.getText().toString();
                email=this.emailSignup.getText().toString();
                confirmPass=confirmPassword.getText().toString();
                referralCodeStr=this.referralCode.getText().toString();
                mobile1=mobileNum.getText().toString();
                String otp=otpSignup.getText().toString();

                if(!StringUtils.isValid(name)){
                    Toast.makeText(this, getString(R.string.fill_first_name), Toast.LENGTH_LONG).show();
                    return;
                }
                if(!StringUtils.isValid(lastNameStr)){
                    Toast.makeText(this, getString(R.string.valid_last_name), Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUtils.isEmpty(password)){
                    Toast.makeText(this, getString(R.string.password_must_6), Toast.LENGTH_LONG).show();
                    return;
                }
                if(!password.equals(confirmPass)){
                    Toast.makeText(this, getString(R.string.confirm_password_not_match), Toast.LENGTH_LONG).show();
                    return;
                }
                EmailValidator emailValidator=EmailValidator.getInstance();
                if(!emailValidator.isValid(email)){
                    Toast.makeText(this, getString(R.string.valid_email), Toast.LENGTH_LONG).show();
                    return;
                }
                if(!StringUtils.isMobile(mobile1)){
                    Toast.makeText(this, getString(R.string.valid_mobile_msg), Toast.LENGTH_LONG).show();
                    return;
                }

                if(!otp.equals(otpValue)){
                    Toast.makeText(this, getString(R.string.otp_invalid), Toast.LENGTH_LONG).show();
                    return;
                }
                spotsDialog.show();
                executeCreateCustomer();

                break;
            case R.id.request_otp:
                String mobile=mobileNum.getText().toString();
                Call<JsonObject> otpCall=projectAPI.getOtp(getString(R.string.apikey), mobile);
                CallbackApi<JsonObject> callbackApi=new CallbackApi<>(this);
                otpCall.enqueue(callbackApi);
                break;
        }
    }


    private void signup(String csId){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("v_code",getString(R.string.v_code));
            jsonObject.put("apikey", getString(R.string.apikey));
            jsonObject.put("deviceID", "82150528-23LG-4622-B303-68B4572F9305");
            jsonObject.put("deviceType", "android");
            jsonObject.put("accessType", "False");
            jsonObject.put("accessName", "Normal");
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("first_name", name);
            jsonObject.put("last_name", lastNameStr);
            jsonObject.put("mobile", mobile1);
            jsonObject.put("stripe_cs_id", csId);
            jsonObject.put("singUpWith","");
            sendJsonData=new JSONObject();
            sendJsonData.put("RequestData", jsonObject);
            Log.i("requestData", sendJsonData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit.Builder builder=new Retrofit.Builder();
        builder.baseUrl(Utils.SITE_URL);
        builder.addConverterFactory(new StringConvertFactory());
        Retrofit retrofit=builder.build();
        ProjectAPI projectAPI1=retrofit.create(ProjectAPI.class);
        Call<String> signupCall=projectAPI1.signUp(sendJsonData.toString());
        signupCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res=response.body();
                Log.i("responseData","Sign up process : "+ res);
                try {
                    JSONObject jsonObject1=new JSONObject(res);
                    boolean success=jsonObject1.getBoolean("successBool");
                    if(success){

                        //boolean createRequest=getSharedPreferences("service_pref_user", MODE_PRIVATE).getBoolean("createRequest", false);

                        JSONObject resObj=jsonObject1.getJSONObject("response");

                        preferences.edit().putString("user_id",resObj.getString("user_id") )
                                .putString("userToken", resObj.getString("userToken"))
                                .putString("fullname", resObj.getString("fullname"))
                                .putBoolean("isLogin", true).apply();
                    spotsDialog.dismiss();


                        if(bookLatar || bookNow){
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            startHomeActivity();
                        }

                        Toast.makeText(SignupActivity.this, "Your are successfully Registered", Toast.LENGTH_LONG).show();
                    }else{
                        JSONObject error=jsonObject1.getJSONObject("ErrorObj");
                        String errorCode=error.getString("ErrorCode");
                        if(errorCode.equals("104")){
                            String errorMsg=error.getString("ErrorMsg");
                            Toast.makeText(SignupActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("responseError", t.toString());
                Toast.makeText(SignupActivity.this, "No Internet Connectoin !", Toast.LENGTH_LONG);
            }
        });

    }


    private void executeCreateCustomer() {

        CreateCustomer createCustomer=new CreateCustomer();
        createCustomer.execute();
    }

    private void startHomeActivity() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void success(Call<JsonObject> call, Response<JsonObject> response, Object... objects) {
        if (response != null) {
            Log.i("resoponseBody", response.body().toString());
            JsonObject res = response.body();
            boolean success = res.get("successBool").getAsBoolean();
            if (success) {
                String responseType = res.get("responseType").getAsString();
                JsonObject responseData = res.getAsJsonObject("response");
                switch (responseType) {
                    case "send_otp":
                        otpValue = responseData.get("otp").getAsString();
                        mobileNum.setEnabled(false);
                        break;
                }
            }
        }
    }


    class  CreateCustomer extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {


            com.stripe.Stripe.apiKey=getString(R.string.stripe_api_secret_key);
            Map<String, Object> params=new HashMap<>();
            params.put("description", emailSignup.getText().toString());

            try {
                Customer customer=Customer.create(params);

                preferences.edit().putString("cs_id", customer.getId()).apply();
                signup(customer.getId());

                Log.i("responseDataCharge", customer.toJson());
                return customer.getId();
            } catch (AuthenticationException e) {
                Log.e("responseDataError","AuthenticationException" +e.toString());
                return e.toString();
            } catch (InvalidRequestException e) {
                Log.e("responseDataError","InvalidRequestException" +e.toString());
                return e.toString();
            } catch (APIConnectionException e) {
                Log.e("responseDataError","APIConnectionException" +e.toString());
                return e.toString();
            } catch (CardException e) {
                Log.e("responseDataError","CardException" +e.toString());
                return e.toString();
            } catch (APIException e) {
                Log.e("responseDataError","APIException" +e.toString());
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}