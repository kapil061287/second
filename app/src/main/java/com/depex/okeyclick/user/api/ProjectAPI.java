package com.depex.okeyclick.user.api;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProjectAPI {

    @GET("okey-click/api/get_service_list.php")
    Call<JsonObject> getServices(@Query("apikey") String apikey);


    @GET("okey-click/api/send_otp.php")
    Call<JsonObject> getOtp(@Query("apikey") String apikey, @Query("mobile") String mobile);

    @POST("okey-click/api/customer_register.php")
    Call<String> signUp(@Body String body);


    @POST("okey-click/api/user_login.php")
    Call<String> login(@Body String body);

    @GET("okey-click/api/get_subservice.php")
    Call<JsonObject> getSubServices(@Query("apikey") String apikey, @Query("cat_id") String catid);

    @POST("okey-click/api/findnearest_users.php")
    Call<String> availableServiceProvider(@Body String body);

    @GET("okey-click/api/get_package.php")
    Call<JsonObject> getPackages(@Query("apikey") String apikey);

    @POST("okey-click/api/create_request.php")
    Call<String> createRequest(@Body String  body);

    @POST("okey-click/api/check_token.php")
    Call<String> checkToken(@Body String body);

    @POST("okey-click/api/IS_Task_Accepted.php")
    Call<String> isTaskAccepted(@Body String body);

    @GET("maps/api/directions/json")
    Call<String> getPolyLineDirection(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String serverKey);


    @GET("okey-click/api/userDetail.php")
    Call<String> getServiceProviderDetails(@Query("apikey") String apikey, @Query("sp_id") String spid);

    @GET("okey-click/api/get_location.php")
    Call<String> getServiceProviderLocation(@Query("apikey") String apikey, @Query("user_id") String spid);

    @POST("okey-click/api/update_DeviceToken.php")
    Call<String> updateFcmToken(@Body  String body);

}