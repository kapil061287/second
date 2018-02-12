package com.depex.okeyclick.user.screens;

import android.content.Intent;
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

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        setOnClickListener(roundImageSignup, registerSignup, requestOtpBtn);


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

                String name=nameSignup.getText().toString();
                String password=passwordSignup.getText().toString();
                String lastName=this.lastName.getText().toString();
                String email=this.emailSignup.getText().toString();
                String confirmPass=confirmPassword.getText().toString();
                String referralCode=this.referralCode.getText().toString();
                String mobile1=mobileNum.getText().toString();
                String otp=otpSignup.getText().toString();

                if(!StringUtils.isValid(name)){
                    Toast.makeText(this, "Please fill valid first name !", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!StringUtils.isValid(lastName)){
                    Toast.makeText(this, "Please fill valid last name !", Toast.LENGTH_LONG).show();
                    return;
                }
                if(StringUtils.isEmpty(password)){
                    Toast.makeText(this, "Password must be length of 6 !", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!password.equals(confirmPass)){
                    Toast.makeText(this, "confirm password does not match !", Toast.LENGTH_LONG).show();
                    return;
                }
                EmailValidator emailValidator=EmailValidator.getInstance();
                if(!emailValidator.isValid(email)){
                    Toast.makeText(this, "Entor a valid email !", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!StringUtils.isMobile(mobile1)){
                    Toast.makeText(this, "Entor a valid mobile number !", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!otp.equals(otpValue)){
                    Toast.makeText(this, "OTP is invalid !", Toast.LENGTH_LONG).show();
                    return;
                }

                 JSONObject jsonObject=new JSONObject();
                try {

                    jsonObject.put("v_code",getString(R.string.v_code));
                    jsonObject.put("apikey", getString(R.string.apikey));
                    jsonObject.put("deviceID",
                            "82150528-23LG-4622-B303-68B4572F9305");
                    jsonObject.put("deviceType", "android");
                    jsonObject.put("accessType", "False");
                    jsonObject.put("accessName", "Normal");
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
                    jsonObject.put("first_name", name);
                    jsonObject.put("last_name", lastName);
                    jsonObject.put("mobile", mobile1);
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
                        Log.i("responseData", res);
                        try {
                            JSONObject jsonObject1=new JSONObject(res);
                            boolean success=jsonObject1.getBoolean("successBool");
                            if(success){

                                boolean createRequest=getSharedPreferences("service_pref_user", MODE_PRIVATE).getBoolean("createRequest", false);

                                Toast.makeText(SignupActivity.this, "Your are successfully Registered", Toast.LENGTH_LONG).show();
                                Intent intent1=new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent1);
                                finish();
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
                    }
                });

                break;
            case R.id.request_otp:
                String mobile=mobileNum.getText().toString();
                Call<JsonObject> otpCall=projectAPI.getOtp(getString(R.string.apikey), mobile);
                CallbackApi<JsonObject> callbackApi=new CallbackApi<>(this);
                otpCall.enqueue(callbackApi);
                break;
        }
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
}