package com.depex.okeyclick.user.launch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.screens.ChoosLanguageActivity;
import com.depex.okeyclick.user.screens.HomeActivity;
import com.depex.okeyclick.user.screens.JobAssignedActivity;
import com.depex.okeyclick.user.screens.LoginActivity;
import com.depex.okeyclick.user.screens.ServiceProviderProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SecondSplashActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash);
        Button button=findViewById(R.id.next_btn_second_splash);
        button.setOnClickListener(this);
        preferences=getSharedPreferences(Utils.SERVICE_PREF, MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        if(preferences.getBoolean("isLogin", false)) {
            //startLoginActivity();
            checkLogin();
            view.setEnabled(false);
        }else {
            startLoginActivity();
        }
    }


    private void startHomeActivity() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoginActivity() {
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void checkLogin(){
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();

        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("deviceType", "android");
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("DeviceToken", "");
            requestData.put("RequestData", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Retrofit
                .Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .checkToken(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString=response.body();
                        Log.i("responseData", "Check Login : "+responseString);
                        if(responseString==null)return;
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                //dialog.dismiss();

                                Intent intent=new Intent(SecondSplashActivity.this, HomeActivity.class);
                                startActivity(intent);

                            }else{
                                //dialog.dismiss();
                                preferences.edit()
                                        .remove("isLogin")
                                        .remove("fullname")
                                        .remove("userToken")
                                        .remove("user_id")
                                        .apply();

                                Intent intent=new Intent(SecondSplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {

                            final Snackbar snackbar=Snackbar.make(findViewById(R.id.cont_layout_language_btn), "Please Check your Internet Connection !", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("responseDataError", t.toString());

                    }
                });
    }
}