package com.depex.okeyclick.user.launch;

import android.content.Intent;
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

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        getSharedPreferences("service_pref_user", MODE_PRIVATE).edit().remove("from_book_screen").remove("createRequest").apply();
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
}