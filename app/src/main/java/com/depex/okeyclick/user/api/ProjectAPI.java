package com.depex.okeyclick.user.api;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProjectAPI {

    @GET("get_service_list.php")
    Call<JsonObject> getServices(@Query("apikey") String apikey);


    @GET("send_otp.php")
    Call<JsonObject> getOtp(@Query("apikey") String apikey, @Query("mobile") String mobile);

    @POST("customer_register.php")
    Call<String> signUp(@Body String body);


    @POST("user_login.php")
    Call<String> login(@Body String body);

    @GET("get_subservice.php")
    Call<JsonObject> getSubServices(@Query("apikey") String apikey, @Query("cat_id") String catid);

    @POST("findnearest_users.php")
    Call<String> availableServiceProvider(@Body String body);

    @GET("get_package.php")
    Call<JsonObject> getPackages(@Query("apikey") String apikey);

    @POST("create_request.php")
    Call<String> createRequest(@Body String  body);

    @POST("check_token.php")
    Call<String> checkToken(@Body String body);

    @POST("IS_Task_Accepted.php")
    Call<String> isTaskAccepted(@Body String body);

    @GET("maps/api/directions/json")
    Call<String> getPolyLineDirection(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String serverKey);


    @GET("userDetail.php")
    Call<String> getServiceProviderDetails(@Query("apikey") String apikey, @Query("sp_id") String spid);

    @GET("get_location.php")
    Call<String> getServiceProviderLocation(@Query("apikey") String apikey, @Query("user_id") String spid);

    @POST("update_DeviceToken.php")
    Call<String> updateFcmToken(@Body  String body);

    @POST("check_task_status.php")
    Call<String> checkSpStatus(@Body String body);

}