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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class SubServiceFragment2 extends Fragment {

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.content_subservice_fragment, null, false);
        TabLayout tabLayout=view.findViewById(R.id.tabs1);
        ViewPager viewPager=view.findViewById(R.id.view_pager_profile1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        Bundle bundle=getArguments();
        if(bundle==null){
            Log.i("bundleNull", "Bundle is null ");
            return view;
        }
        String json=bundle.getString("jsonServiceList", "");
        int position=bundle.getInt("position", 0);
        Log.i("positionElements", String.valueOf(position));
        Gson gson=new Gson();
        Log.i("tagFragment", getTag()+"");
        Service[]arr=gson.fromJson(json, Service[].class);
        List<Service> services=new ArrayList<>(Arrays.asList(arr));
        for(Service service : services){
            final TabLayout.Tab tab=tabLayout.newTab();
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
                                tab.setIcon(new BitmapDrawable(getResources(), resource));
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            tab.setIcon(errorDrawable);
                        }
                    });
            tabLayout.addTab(tab);
            service.getServiceName();
        }
        Log.i("responseDataPosition", position+"");

        MyAdpater adpater=new MyAdpater(getChildFragmentManager(), services);
        viewPager.setAdapter(adpater);
        tabLayout.getTabAt(position).select();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    class MyAdpater extends FragmentPagerAdapter{

        List<Service> services;
        public MyAdpater(FragmentManager fm, List<Service> list) {
            super(fm);
            services=list;
        }

        @Override
        public Fragment getItem(int position) {
            SubServiceViewPagerFragment subServiceViewPagerFragment=new SubServiceViewPagerFragment();
            String url=services.get(position).getImageUrl();
            Bundle bundle=new Bundle();
            String json=new Gson().toJson(services.get(position));
            bundle.putString("json", json);
            subServiceViewPagerFragment.setArguments(bundle);
            return subServiceViewPagerFragment;
        }

        @Override
        public int getCount() {
            return services.size();
        }
    }
}