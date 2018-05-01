package com.depex.okeyclick.user.api;
import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<JsonObject> getPackages(@Query("apikey") String apikey,
                                 @Query("unit") String unit,
                                 @Query("city_name") String cityName,
                                 @Query("subcategory") String subCategory);

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

    @POST("apply_coupon.php")
    Call<String> appliedCoupen(@Body String body);

    @POST("cancel_coupon.php")
    Call<String> cancelCoupen(@Body String body);

    @POST("report_issue.php")
    Call<String> reportIssue(@Body String body);

    @POST("payment_process.php")
    Call<String> paymentProcess(@Body String body);

    @POST("get_all_taskCS.php")
    Call<String> getServiceHistory(@Body String body);

    @POST("resend_request.php")
    Call<String> resendRequest(@Body String body);

    @POST("task_cancel_question_list.php")
    Call<String> cancelTaskReasons(@Body String body);

    @POST("task_cancel.php")
    Call<String> cancelTask(@Body String body);

    @POST("get_all_sp.php")
    Call<String> getAllSp(@Body String body);

    @POST("get_coupon_list.php")
    Call<String> getCouponList(@Body String body);

    @POST("generate_task_invoice.php")
    Call<String> generateInvoice(@Body String body);

    @POST("book_latter.php")
    Call<String> bookLetarReq(@Body String body);

    @POST("confirm_complete.php")
    Call<String> confirmComplete(@Body String body);

    @POST("rating.php")
    Call<String> rating(@Body String body);

    @POST("taskDetail.php")
    Call<String> taskDetails(@Body String body);

    //http://depextechnologies.org/okey-click/api/edit_profile_pic.php

    @Multipart
    @POST("edit_profile_pic.php")
    Call<ResponseBody> upload(@Part("v_code")RequestBody v_code, @Part("apikey")RequestBody apikey,
                              @Part MultipartBody.Part file, @Part("userToken")RequestBody userToken,
                              @Part("user_id")RequestBody user_id);

    @POST("cs_task_payment_history.php")
    Call<String> getPaymentHistory(@Body String body);

    @POST("edit_customer_profile.php")
    Call<String> updateUserDetails(@Body String body);
}