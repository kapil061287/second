package com.depex.okeyclick.user.api;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by we on 1/17/2018.
 */

public interface ApiListener<T>   {
    public void success(Call<T> call, Response<T> response, Object... objects);
}
