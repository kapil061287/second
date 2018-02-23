package com.depex.okeyclick.user.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.braintreepayments.api.Json;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportAndIssue extends Fragment implements View.OnClickListener {

    @BindView(R.id.report_issue_submit_btn)
    Button reportIssueSubmitButton;

    @BindView(R.id.message_report_issue)
    EditText messageReportIssue;

    SharedPreferences preferences;

    @BindView(R.id.report_issue_btn)
    Button reportIssueBtn;

    @BindView(R.id.service_feedback_btn)
    Button serviceFeedbackBtn;

    @BindView(R.id.other_btn)
    Button otherBtn;

    Context context;

    boolean reporBtn, other, serviceFeedback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_report_and_issue_layout, container, false);
        Toolbar toolbar=getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        toolbar.setTitle("Report And Issue");
        ButterKnife.bind(this, view);
        preferences=context.getSharedPreferences("service_pref_user", Context.MODE_PRIVATE);
        otherBtn.setOnClickListener(this);
        serviceFeedbackBtn.setOnClickListener(this);
        reportIssueBtn.setOnClickListener(this);

        reportIssueSubmitButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.report_issue_submit_btn:
                sendFeedback(messageReportIssue.getText().toString());
                break;
            case R.id.report_issue_btn:
                setButtonStatus(view);
                break;
            case R.id.other_btn:
                setButtonStatus(view);
                break;
            case R.id.service_feedback_btn:
                setButtonStatus(view);
                break;
        }
    }

    private void sendFeedback(String msg) {
        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("comment", msg);
            requestData.put("RequestData", data);

            new Retrofit.Builder()
                    .baseUrl(Utils.SITE_URL)
                    .addConverterFactory(new StringConvertFactory())
                    .build()
                    .create(ProjectAPI.class)
                    .reportIssue(requestData.toString())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String reponseString=response.body();
                            try {
                                JSONObject res=new JSONObject(reponseString);
                                boolean success=res.getBoolean("successBool");
                                if(success) {
                                 String msg=res.getJSONObject("response").getString("msg");
                                    showDialog(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String msg) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm Feedback")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().onBackPressed();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public void updateUI(){
        if(reporBtn){
                reportIssueBtn.setBackgroundResource(R.drawable.radio_button_report_complaint_checked);
                reportIssueBtn.setTextColor(Color.parseColor("#ffffffff"));
        } if (serviceFeedback){
                serviceFeedbackBtn.setBackgroundResource(R.drawable.radio_button_report_complaint_checked);
                serviceFeedbackBtn.setTextColor(Color.parseColor("#ffffffff"));
        } if (other){
            otherBtn.setBackgroundResource(R.drawable.radio_button_report_complaint_checked);
            otherBtn.setTextColor(Color.parseColor("#ffffffff"));
        }

        if(!reporBtn){
            reportIssueBtn.setBackgroundResource(R.drawable.radio_button_report_complaint_unchecked);
            reportIssueBtn.setTextColor(Color.parseColor("#ff000000"));
        } if (!serviceFeedback){
            serviceFeedbackBtn.setBackgroundResource(R.drawable.radio_button_report_complaint_unchecked);
            serviceFeedbackBtn.setTextColor(Color.parseColor("#ff000000"));
        } if (!other){
            otherBtn.setBackgroundResource(R.drawable.radio_button_report_complaint_unchecked);
            otherBtn.setTextColor(Color.parseColor("#ff000000"));
        }
    }

    public boolean getButtonStatus(){
        if(reporBtn)
            return reporBtn;
        if(serviceFeedback)
            return serviceFeedback;
        if(other)
            return other;
        return false;
    }

    public void setButtonStatus(View v){
        switch (v.getId()){
            case R.id.report_issue_btn:
                    other=false;
                    reporBtn=true;
                    serviceFeedback=false;
                    updateUI();
                break;
            case R.id.other_btn:
                other=true;
                reporBtn=false;
                serviceFeedback=false;
                updateUI();
                break;
            case R.id.service_feedback_btn:
                other=false;
                reporBtn=false;
                serviceFeedback=true;
                updateUI();
                break;
        }
    }
}
