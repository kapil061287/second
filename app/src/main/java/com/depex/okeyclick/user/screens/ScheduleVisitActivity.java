package com.depex.okeyclick.user.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.depex.okeyclick.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleVisitActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_visit);
        ButterKnife.bind(this);
    }
}