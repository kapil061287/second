package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.ServiceHistoryAdapter;
import com.depex.okeyclick.user.adpater.ServiceHistoryItemClickListener;
import com.depex.okeyclick.user.contants.UtilMethods;
import com.depex.okeyclick.user.model.ServiceHistory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ServiceHistoryUpcomingFragment extends Fragment implements ServiceHistoryItemClickListener {

    @BindView(R.id.service_history_upcoming_recy)
    RecyclerView serviceHistoryUpcomingRec;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_service_history_upcoming_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle=getArguments();
        String json=bundle.getString("json");
        Log.i("responseData", "Service History Upcoming : "+json);

        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-d H:m:s").create();
        ServiceHistory[]arr=gson.fromJson(json, ServiceHistory[].class);
        List<ServiceHistory> list= new ArrayList<>(Arrays.asList(arr));
        ServiceHistoryAdapter adapter=new ServiceHistoryAdapter(context, list, this);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(context, manager.getOrientation());
        serviceHistoryUpcomingRec.addItemDecoration(dividerItemDecoration);
        serviceHistoryUpcomingRec.setLayoutManager(manager);
        serviceHistoryUpcomingRec.setAdapter(adapter);
        return view;
    }

    public static ServiceHistoryUpcomingFragment newInstance(String json){
        ServiceHistoryUpcomingFragment fragment=new ServiceHistoryUpcomingFragment();
        Bundle bundle=new Bundle();
        bundle.putString("json", json);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onServiceHistoryItemClickListener(ServiceHistory serviceHistory) {
        UtilMethods.viewTaskProcess(context,serviceHistory);
    }
}
