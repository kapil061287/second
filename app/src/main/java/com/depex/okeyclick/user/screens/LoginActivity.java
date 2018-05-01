package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.Toast;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.signup_btn)
    Button signup_btn;
    @BindView(R.id.skip_btn)
    Button skip_btn;
    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.text_username)
    TextInputLayout text_username;

    @BindView(R.id.text_password)
    TextInputLayout text_password;
    SharedPreferences preferences;
    boolean bookLatar=false;
    boolean bookNow=false;

    SpotsDialog spotsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        spotsDialog=new SpotsDialog(this);
        if(bundle!=null){
            bookLatar=bundle.getBoolean("isBookLetar", false);
            bookNow=bundle.getBoolean("isBookNow", false);
        }
        skip_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);

        if(bookNow  || bookLatar ){
            skip_btn.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.skip_btn:
                Intent homeIntent=new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.signup_btn:
                startSignupProcess();
                break;
            case R.id.login_btn:
                spotsDialog.show();
                String username=text_username.getEditText().getText().toString();
                String password=text_password.getEditText().getText().toString();
                JSONObject requestData=new JSONObject();
                JSONObject data=new JSONObject();
                try {
                    data.put("v_code", getString(R.string.v_code));
                    data.put("apikey", getString(R.string.apikey));
                    data.put("deviceType", "android");
                    data.put("deviceID", "82150528-23LG-4622-B303-68B4572F9305");
                    data.put("username", username);
                    data.put("password", password);
                    data.put("device_token", FirebaseInstanceId.getInstance().getToken());
                    data.put("loginWith", "");
                    requestData.put("RequestData", data);

                    Retrofit.Builder builder=new Retrofit.Builder();
                    builder.baseUrl(Utils.SITE_URL);
                    builder.addConverterFactory(new StringConvertFactory());
                    Retrofit retrofit=builder.build();

                    ProjectAPI projectAPI=retrofit.create(ProjectAPI.class);
                    Call<String> loginCall=projectAPI.login(requestData.toString());
                    loginCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseString=response.body();
                            Log.i("responseData", responseString+"");
                            try {
                                JSONObject res=new JSONObject(responseString);
                                boolean success=res.getBoolean("successBool");
                                if(success){
                                    JSONObject responseData=res.getJSONObject("response");

                                    preferences.edit().putString("user_id",responseData.getString("user_id") )
                                    .putString("userToken", responseData.getString("userToken"))
                                            .putString("fullname", responseData.getString("fullname"))
                                            .putString("cs_id", responseData.getString("stripe_cs_id"))
                                            .putString("profile_pic", responseData.getString("profile_pic"))
                                            .putBoolean("isLogin", true).apply();
                                    spotsDialog.dismiss();

                                    if(bookLatar || bookNow){
                                        setResult(RESULT_OK);
                                        finish();
                                    }else{
                                        Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }else{
                                    spotsDialog.dismiss();
                                    JSONObject errorObj=res.getJSONObject("ErrorObj");
                                    String errorCode=errorObj.getString("ErrorCode");
                                    if(errorCode.equals("108")){


                                        String errorMsg=errorObj.getString("ErrorMsg");
                                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            spotsDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Please Check your internet connection !", Toast.LENGTH_LONG).show();
                                Log.e("responseError", t.toString());
                        }
                    });



                } catch (JSONException e) {
                    spotsDialog.dismiss();
                    e.printStackTrace();
                }
                break;

        }
    }

    private void startSignupProcess() {
        Intent signupIntent=new Intent(this, SignupActivity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("bookLater", bookLatar);
        bundle.putBoolean("bookNow", bookNow);
        signupIntent.putExtras(bundle);
            startActivity(signupIntent);
    }

    @Override
    public void onBackPressed() {
        if(bookNow || bookLatar){
            setResult(RESULT_CANCELED);
            finish();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(preferences.getBoolean("isLogin", false)){
            setResult(RESULT_OK);
            finish();
        }
    }
}