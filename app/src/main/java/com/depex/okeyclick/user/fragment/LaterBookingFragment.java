package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.ServicesRecyclerAdapter;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.depex.okeyclick.user.model.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LaterBookingFragment extends Fragment {
    @BindView(R.id.later_booking_fragment_outer_tabs)
    TabLayout outerTabs;

    @BindView(R.id.later_booking_fragment_outer_view_pager)
    ViewPager outerViewPager;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_later_booking_fragment, container, false);
        ButterKnife.bind(this, view);
        outerViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(outerTabs));
        outerTabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(outerViewPager));
        outerTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        initOuterTabs();
        return view;
    }

    private void initOuterTabs() {
        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProjectAPI.class)
                .getServices(getString(R.string.apikey))
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res=response.body();
                        boolean success=res.get("successBool").getAsBoolean();
                        String responseType=res.get("responseType").getAsString();
                        Gson gson=new Gson();
                        if(success) {
                            JsonObject responseData = res.get("response").getAsJsonObject();
                            switch (responseType) {
                                case "get_category":
                                    JsonArray service_list = responseData.get("List").getAsJsonArray();
                                    Service[] arr = gson.fromJson(service_list, Service[].class);
                                    ArrayList<Service> services = new ArrayList<>(Arrays.asList(arr));
                                    fillOuterTabs(services, 0);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("responseDataError", "Later Booking : "+t.toString());
                            initOuterTabs();
                    }
                });
    }

    private void fillOuterTabs(List<Service> services, int position) {
        for(Service service : services){
            final TabLayout.Tab tab=outerTabs.newTab();
            tab.setText(service.getServiceName());
            String icon=service.getIcon();
            GlideApp
                    .with(context)
                    .asBitmap()
                    .load(icon)
                    .placeholder(R.drawable.service_history_icon)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Log.i("resourceTest", ""+resource);
                            tab.setIcon(new BitmapDrawable(getResources(), resource));
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            Drawable drawable=getResources().getDrawable(R.drawable.payment_history_icon);
                            tab.setIcon(errorDrawable);
                        }
                    });
            outerTabs.addTab(tab);
            //service.getServiceName();
        }
        MyAdpater adpater=new MyAdpater(getChildFragmentManager(), services);
        outerViewPager.setAdapter(adpater);
        outerTabs.getTabAt(position).select();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }



    class MyAdpater extends FragmentPagerAdapter {
        List<Service> services;
        public MyAdpater(FragmentManager fm, List<Service> list) {
            super(fm);
            services=list;
        }

        @Override
        public Fragment getItem(int position) {
            SubserviceOuterViewpagerFragment subServiceOuterViewPagerFragment=new SubserviceOuterViewpagerFragment();
            Bundle bundle=new Bundle();
            String json=new Gson().toJson(services.get(position));
            bundle.putString("json", json);
            subServiceOuterViewPagerFragment.setArguments(bundle);
            return subServiceOuterViewPagerFragment;
        }

        @Override
        public int getCount() {
            return services.size();
        }
    }
}
