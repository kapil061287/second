package com.depex.okeyclick.user.screens;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.fragment.LaterBookingFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Fragment fragment=new LaterBookingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.test_container, fragment).commit();
        getSupportActionBar().setTitle("Book Later");
    }
}
