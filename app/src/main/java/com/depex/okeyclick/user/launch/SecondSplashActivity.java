package com.depex.okeyclick.user.launch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.screens.ChoosLanguageActivity;
import com.depex.okeyclick.user.screens.JobAssignedActivity;
import com.depex.okeyclick.user.screens.ServiceProviderProfileActivity;

public class SecondSplashActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash);
        Button button=findViewById(R.id.next_btn_second_splash);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this, ChoosLanguageActivity.class);
        startActivity(intent);
        finish();

        /*Intent intent=new Intent(this, ServiceProviderProfileActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("sp_id", "11");
        intent.putExtras(bundle);
        startActivity(intent);*/
    }
}