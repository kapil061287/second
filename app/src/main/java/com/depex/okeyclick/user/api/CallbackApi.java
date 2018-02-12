package com.depex.okeyclick.user.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by we on 1/17/2018.
 */

public class CallbackApi<T> implements Callback<T> {

    ApiListener<T> apiListener;
    Object[]objects;
    public CallbackApi(ApiListener<T> apiListener, Object... objects){
        this.apiListener=apiListener;
        this.objects=objects;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        apiListener.success(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        apiListener.success(call, null, "error");
        Log.e("retrofitApiError", t.toString());
    }
}
