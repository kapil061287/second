package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.fragment.DetailsServiceProviderFragment;
import com.depex.okeyclick.user.fragment.DetailsSpProfileFragment;
import com.depex.okeyclick.user.fragment.ReviewsServiceProviderFragment;
import com.depex.okeyclick.user.fragment.ReviewsSpProfileFragment;
import com.depex.okeyclick.user.model.BookLaterServiceProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hedgehog.ratingbar.RatingBar;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceProviderProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    String spId;
    SectionPagerAdapter pagerAdapter;
    ProgressBar progressBar;
    RoundedImageView roundedImageView;
    TextView serviceProviderName;
    TextView perHourPrice;
    RatingBar ratingBar;

    @BindView(R.id.cancel_btn)
    Button cancelBtn;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);
        viewPager=findViewById(R.id.view_pager_profile);
        tabLayout=findViewById(R.id.tabs);
        ButterKnife.bind(this);
        serviceProviderName=findViewById(R.id.service_provider_name);
        perHourPrice=findViewById(R.id.per_hour_price);
        ratingBar=findViewById(R.id.star_view_profile);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        roundedImageView=findViewById(R.id.user_image_provider_profile);
        Bundle bundle=getIntent().getExtras();
        cancelBtn.setOnClickListener(this);
        if(bundle!=null){
            spId=bundle.getString("sp_id");
        }
        progressBar=findViewById(R.id.progress_bar_service_provider_profile);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        getProfile(spId);
    }


    private void getProfile(final String spId) {
        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .getServiceProviderDetails(getString(R.string.apikey), spId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i("responseData", response.body());
                        String responseString=response.body();
                        try {
                            progressBar.setVisibility(View.GONE);
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");

                                Bundle bundle=new Bundle();
                                bundle.putString("resObj", resObj.toString());

                                //JSONArray resList=resObj.getJSONArray("List");

                                JSONObject resObjDetails=resObj.getJSONObject("List");

                                String user_image_url=resObjDetails.getString("user_images");
                                String providerFirstName=resObjDetails.getString("first_name");
                                String providerLastName=resObjDetails.getString("last_name");
                                String name=providerFirstName+" "+(providerLastName!=null ? providerLastName : "");

                                String perHourPriceText=resObjDetails.getString("pac_price_per_hr");
                                perHourPrice.setText(perHourPriceText);


                                ratingBar.setEnabled(false);
                                ratingBar.setStar(3);

                                Log.i("responseData", "User Image : "+user_image_url);

                                serviceProviderName.setText(name);

                                GlideApp.with(ServiceProviderProfileActivity.this).load(user_image_url).into(roundedImageView);

                                /*
                                DetailsSpProfileFragment detailsSpProfileFragment=new DetailsSpProfileFragment();
                                detailsSpProfileFragment.setArguments(bundle);*/

                              /*  ReviewsSpProfileFragment reviewsSpProfileFragment=new ReviewsSpProfileFragment();
                                reviewsSpProfileFragment.setArguments(bundle);*/


                                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-d H:m:s").create();
                                BookLaterServiceProvider serviceProvider=gson.fromJson(resObjDetails.toString(), BookLaterServiceProvider.class);


                                DetailsServiceProviderFragment fragment=DetailsServiceProviderFragment.getInstance(serviceProvider);
                                ReviewsServiceProviderFragment fragment2=ReviewsServiceProviderFragment.getInstance(serviceProvider);


                                List<Fragment> list=new ArrayList<>();
                                list.add(fragment2);
                                list.add(fragment);

                                pagerAdapter =new SectionPagerAdapter(getSupportFragmentManager(), list);
                                viewPager.setAdapter(pagerAdapter);
                            }
                        } catch (JSONException e) {
                            Log.e("responseData", e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //TODO Check internet connectivity
                            getProfile(spId);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_btn:
                    //startReasonActivity();
                break;
        }
    }

    private void startReasonActivity() {
            Intent intent=new Intent(this, CancelTaskActivity.class);
            startActivity(intent);
    }

    private class SectionPagerAdapter  extends FragmentPagerAdapter{

        List<Fragment> list;
        public SectionPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list=list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}