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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        skip_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        boolean requestGen=preferences.getBoolean("createRequest", false);
        if(requestGen){
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
                Intent signupIntent=new Intent(this, SignupActivity.class);
                startActivity(signupIntent);
                break;
            case R.id.login_btn:
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
                                    .putBoolean("isLogin", true).apply();
                                    if(preferences.getBoolean("createRequest", false)){
                                        String requestjson=preferences.getString("from_book_screen", "0");
                                        JSONObject jsonObject=new JSONObject(requestjson);
                                        jsonObject.put("userToken", preferences.getString("userToken", "0"));
                                        jsonObject.put("created_by", preferences.getString("user_id", "0"));
                                        JSONObject jsonObject1=new JSONObject();
                                        jsonObject1.put("RequestData", jsonObject);
                                        sendHttpRequest(jsonObject1);
                                       Intent intent=new Intent(LoginActivity.this, JobAssignedActivity.class);
                                       startActivity(intent);
                                       finish();
                                    }else{
                                        Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }else{
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
                                Log.e("responseError", t.toString());
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
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

                        Intent intent=new Intent(LoginActivity.this, JobAssignedActivity.class);
                        preferences.edit().putString("task_id", task_id).apply();
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    Log.e("responseDataError", e.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("responseError", t.toString());
            }
        });
    }

}