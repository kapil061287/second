package com.depex.okeyclick.user.launch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ProgressBar;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.screens.ChoosLanguageActivity;
import com.depex.okeyclick.user.screens.JobAssignedActivity;
import com.depex.okeyclick.user.screens.LoginActivity;
import com.depex.okeyclick.user.screens.SignupActivity;
import com.depex.okeyclick.user.screens.TestActivity;

public class SplashActivity extends AppCompatActivity {

    //ProgressBar progressBar;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=new Intent(this, TestActivity.class);
        startActivity(intent);
        if(1==1)return;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_splash);
        //progressBar = findViewById(R.id.progressBar);
        getSharedPreferences("service_pref_user", MODE_PRIVATE).edit().remove("from_book_screen").remove("createRequest").remove("requestTime").apply();
        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        if(preferences.getBoolean("isAccept", false)){
            startJobAssignActivity();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    Intent choolLanguageIntent = new Intent(SplashActivity.this, SecondSplashActivity.class);
                    startActivity(choolLanguageIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startJobAssignActivity() {
        //Intent intent=new Intent(this, JobAssignedActivity.class);
        //startActivity(intent);
    }
}