package com.depex.okeyclick.user.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.model.SubService;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubserviceOuterViewpagerFragment extends Fragment {

    @BindView(R.id.later_booking_fragment_inner_tabs)
    TabLayout innerTabs;

    @BindView(R.id.later_booking_fragment_inner_view_pager)
    ViewPager innerViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_subservice_outer_view_pager_fragment, container, false);
        ButterKnife.bind(this, view);
        innerTabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(innerViewPager));
        innerViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(innerTabs));
        innerTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        Bundle args=getArguments();
        Gson gson=new Gson();
        String json=args.getString("json");
        Service service=gson.fromJson(json, Service.class);

        initInnerTabs(service);
        return view;
    }

    private void initInnerTabs(Service service) {
            List<SubService> subServices=service.getSubServices();
            if(subServices!=null) {
                for (SubService subService : subServices) {

                    TabLayout.Tab tab = innerTabs.newTab();
                    tab.setText(subService.getSubServiceName().toUpperCase());
                    innerTabs.addTab(tab);
                }
                InnerViewPagerAdapter innerViewPagerAdapter=new InnerViewPagerAdapter(getChildFragmentManager(), subServices);
                innerViewPager.setAdapter(innerViewPagerAdapter);
            }
    }

    class InnerViewPagerAdapter extends FragmentPagerAdapter{

        List<SubService> subServices;
        public InnerViewPagerAdapter(FragmentManager fm, List<SubService> subServices) {
            super(fm);
            this.subServices=subServices;
        }

        @Override
        public Fragment getItem(int position) {
            LaterBookFragment laterBookFragment =new LaterBookFragment();
            Bundle bundle=new Bundle();
            Gson gson=new Gson();
            String json=gson.toJson(subServices.get(position));
            bundle.putString("json", json);
            laterBookFragment.setArguments(bundle);
            return laterBookFragment;
        }

        @Override
        public int getCount() {
            return subServices.size();
        }
    }
}