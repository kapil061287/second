package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.Service;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        initTabs();
        return view;
    }

    private void initTabs() {
        Bundle bundle=getArguments();
        if(bundle==null){
            Log.i("bundleNull", "Bundle is null ");
            return;
        }
        String json=bundle.getString("jsonServiceList", "");
        int position=bundle.getInt("position", 0);
        Log.i("positionElements", String.valueOf(position));
        Gson gson=new Gson();
        //Log.i("tagFragment", getTag()+"");
        Service[]arr=gson.fromJson(json, Service[].class);
        List<Service> services=new ArrayList<>(Arrays.asList(arr));
        for(Service service : services){
            final TabLayout.Tab tab=outerTabs.newTab();
            tab.setText(service.getServiceName());
            String icon=service.getIcon();
            GlideApp
                    .with(context)
                    .asBitmap()
                    .load(icon)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            tab.setIcon(new BitmapDrawable(getResources(), resource));
                        }
                    });
            outerTabs.addTab(tab);
            service.getServiceName();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
