package com.depex.okeyclick.user.contants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.model.ServiceHistory;
import com.depex.okeyclick.user.screens.JobAssignByNotification;
import com.depex.okeyclick.user.screens.StartJobActivity;
import com.depex.okeyclick.user.screens.TaskDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UtilMethods {


    public static void viewTaskProcess(final Context context, final ServiceHistory serviceHistory){
        final ServiceHistory serviceHistory1=serviceHistory;
        SharedPreferences preferences=context.getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", context.getString(R.string.v_code));
            data.put("apikey", context.getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id",preferences.getString("user_id","0"));
            data.put("task_id", serviceHistory.getId());
            requestData.put("RequestData", data);

            new Retrofit.Builder()
                    .addConverterFactory(new StringConvertFactory())
                    .baseUrl(Utils.SITE_URL)
                    .build()
                    .create(ProjectAPI.class)
                    .taskDetails(requestData.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseString=response.body();
                            Log.i("responseData","Task Details API : "+responseString);
                            String status=serviceHistory1.getStatus();
                            try {
                                JSONObject res=new JSONObject(responseString);
                                boolean success=res.getBoolean("successBool");
                                Bundle bundle=null;
                                Intent intent=null;
                                if(success){

                                    JSONObject jsonObject=res.getJSONObject("response");

                                    switch (status){
                                        case "1":
                                            break;
                                        case "2":
                                           /* bundle=new Bundle();
                                            bundle.putString("taskDetailsJson", jsonObject.toString());
                                            intent=new Intent(context, JobAssignByNotification.class);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);*/
                                            break;
                                        case "3":
                                         /*   bundle=new Bundle();
                                            bundle.putString("taskDetailsJson", jsonObject.toString());
                                            intent=new Intent(context, JobAssignByNotification.class);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);*/
                                            break;
                                        case "4":
                                          /*  bundle=new Bundle();
                                            bundle.putString("taskDetailsJson", jsonObject.toString());
                                            intent=new Intent(context, StartJobActivity.class);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);*/
                                            break;
                                        case "5":
                                          /*  bundle=new Bundle();
                                            bundle.putString("taskDetailsJson", jsonObject.toString());
                                            intent=new Intent(context, StartJobActivity.class);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);*/
                                            break;
                                        case "6":
                                          /*  bundle=new Bundle();
                                            bundle.putString("taskDetailsJson", jsonObject.toString());
                                            intent=new Intent(context, JobAssignByNotification.class);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);*/
                                            break;
                                        case "7":
                                            break;
                                        case "8":
                                           /* bundle=new Bundle();
                                            bundle.putString("taskDetailsJson", jsonObject.toString());
                                            intent=new Intent(context, TaskDetailsActivity.class);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);*/
                                            break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("responseDataError","Task Details API Error : "+t.toString());
                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void showDialog(Context context){

    }

}
