package com.depex.okeyclick.user.screens;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.view.SubCatRadioButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CancelTaskActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences preferences;
    @BindView(R.id.radio_grp)
    RadioGroup radioGroup;

    @BindView(R.id.cancel_task_btn)
    Button cancelTaskBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_task);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Cancel Reason");
        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        cancelTaskBtn.setOnClickListener(this);
        intitRadioBtn();
    }

    private void intitRadioBtn() {
        final JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            requestData.put("RequestData", data);
        } catch (JSONException e) {
            Log.e("responseDataError", e.toString());
        }

        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .cancelTaskReasons(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString=response.body();
                        Log.i("responseData", "Cancel Task : "+responseString);
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                JSONArray jsonArray=resObj.getJSONArray("list");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String question=jsonObject.getString("question");
                                    SubCatRadioButton radioButton=new SubCatRadioButton(CancelTaskActivity.this, R.layout.content_sub_cat_radio);
                                    RadioButton view= (RadioButton) radioButton.getView(radioGroup, question);
                                    radioGroup.addView(view);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                            Log.e("responseDataError", t.toString());
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_task_btn:
                int id=radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton=radioGroup.findViewById(id);
                String reason=radioButton.getText().toString();
                cancelTask(reason);
                break;
        }
    }

    private void cancelTask(String reason) {
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("task_id", preferences.getString("task_id", "0"));
            data.put("question_id", "");
            data.put("answer", reason);
            requestData.put("RequestData", requestData);

            new Retrofit.Builder()
                    .addConverterFactory(new StringConvertFactory())
                    .baseUrl(Utils.SITE_URL)
                    .build()
                    .create(ProjectAPI.class)
                    .cancelTask(requestData.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String responseString=response.body();
                            Log.i("responseData","Cancel Task : "+ responseString);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("responseDataError", "Cancel Task Error : "+t.toString());
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}