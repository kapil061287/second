package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.fragment.ContactUsFragment;
import com.depex.okeyclick.user.fragment.HomeFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.List;

import butterknife.BindView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.nav_container)
    FrameLayout container_layout;
    FragmentManager fragmentManager;
    SharedPreferences preferences;

    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager=getSupportFragmentManager();
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.i("tokenR", token);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        preferences=getSharedPreferences("service_pref_user", MODE_PRIVATE);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_container, new HomeFragment())
                .commit();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        int count=navigationView.getHeaderCount();
        for(int i=0;i<count;i++){
            View view=navigationView.getHeaderView(i);
            TextView textView=view.findViewById(R.id.view_profile);
            TextView textView1=view.findViewById(R.id.username_text_nav_header);
            if(textView!=null){
                if(preferences.getBoolean("isLogin", false)){
                    textView.setText("View Profile");
                    textView.setVisibility(View.VISIBLE);
                }
            }
            if(textView1!=null){
                    textView1.setText(preferences.getString("fullname", "Guest !"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification_menu) {
            Intent intent=new Intent(this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

    switch (id){
        case R.id.contact_us_menu:
            Toast.makeText(this, "Contactus menu called", Toast.LENGTH_LONG).show();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_container, new ContactUsFragment())
                    .addToBackStack("ContactUs")
                    .commit();
            break;
        case R.id.home_menu:
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_container, new HomeFragment(), "Home").addToBackStack("Home");
            fragmentTransaction.commit();
            break;
    }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }
}