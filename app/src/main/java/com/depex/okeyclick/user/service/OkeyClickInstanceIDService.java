package com.depex.okeyclick.user.service;


import android.content.SharedPreferences;
import android.util.Log;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by we on 2/9/2018.
 */

public class OkeyClickInstanceIDService extends FirebaseInstanceIdService {

    SharedPreferences preferences;
    @Override
    public void onTokenRefresh() {
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.i("tokenRefreshed", token);
        preferences=getSharedPreferences("service_pref", MODE_PRIVATE);
        preferences.edit().putString("deviceToken", token).apply();
        sendTokenToserver(token);
    }
    public void sendTokenToserver(String token){

        if(!preferences.getBoolean("isLogin", false)){
            return;
        }

        JSONObject requestData=new JSONObject();
        try {
            String userToken=preferences.getString("userToken", "0");
            JSONObject data=new JSONObject();
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("deviceType", "android");
            data.put("userToken", userToken);
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("DeviceToken", token);
            requestData.put("RequestData" , data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit.Builder builder=new Retrofit.Builder();
        builder.baseUrl(Utils.SITE_URL);
        builder.addConverterFactory(new StringConvertFactory());
        Retrofit retrofit=builder.build();
        ProjectAPI projectAPI=retrofit.create(ProjectAPI.class);


        Call<String> updateCall=projectAPI.updateFcmToken(requestData.toString());
        updateCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("responseData", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
