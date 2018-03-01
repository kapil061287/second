package com.depex.okeyclick.user.screens;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;

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

public class ChoosLanguageActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.spanish_lang_btn)
    Button spanish_btn;
    @BindView(R.id.eng_lng_button)
    Button eng_button;
    SharedPreferences preferences;


    SpotsDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choos_language);
        ButterKnife.bind(this);
        //spanish_btn=findViewById(R.id.spanish_lang_btn);
        //eng_button=findViewById(R.id.eng_lng_button);
        //set onClick listener
        onClickListener(spanish_btn, eng_button);
        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        /*AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.progressbar_layout, null, false));
        dialog=builder.create();*/
        dialog=new SpotsDialog(this);

        dialog.setTitle("Please Wait...");

    }


    /**
     * on Click Listeneter method for views
     * @param views
     */
    private void onClickListener(View... views) {
        for(View view : views){
            view.setOnClickListener(this);
        }
    }



    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        boolean isLogin=false;
        Resources resources=getResources();
        Configuration configuration=resources.getConfiguration();
        DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        Intent loginIntent=new Intent(this, LoginActivity.class);
        switch (v.getId()){

            case R.id.spanish_lang_btn:

                isLogin=preferences.getBoolean("isLogin", false);
                preferences.edit().putString("locale", "es_us").apply();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLocale(new Locale("es"));
                    resources.updateConfiguration(configuration, displayMetrics);
                }

                if (isLogin){
                    checkLogin();
                }else {
                    bundle.putString("locale", "es_es");
                    loginIntent.putExtras(bundle);
                    startActivity(loginIntent);
                }
                break;
            case R.id.eng_lng_button:
                isLogin=preferences.getBoolean("isLogin", false);
                preferences.edit().putString("locale", "en_us").apply();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLocale(new Locale("en"));
                }
                resources.updateConfiguration(configuration, displayMetrics);

                if (isLogin){
                    checkLogin();
                }else {
                    bundle.putString("locale", "en_us");
                    loginIntent.putExtras(bundle);
                    startActivity(loginIntent);
                }
                break;
        }
    }

    public void checkLogin(){
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        dialog.show();
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
                        if(responseString==null)return;
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                dialog.dismiss();

                                Intent intent=new Intent(ChoosLanguageActivity.this, HomeActivity.class);
                                startActivity(intent);

                            }else{
                                dialog.dismiss();
                                preferences.edit()
                                        .remove("isLogin")
                                        .remove("fullname")
                                        .remove("userToken")
                                        .remove("user_id")
                                        .apply();

                                Intent intent=new Intent(ChoosLanguageActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
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
                        dialog.dismiss();
                    }
                });
    }
}