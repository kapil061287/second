package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.DetailsServiceAdapter;
import com.depex.okeyclick.user.model.BookLaterServiceProvider;
import com.depex.okeyclick.user.model.Service;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsServiceProviderFragment extends Fragment {

    @BindView(R.id.details_recycler)
    RecyclerView recyclerView;
    private Context context;

    @BindView(R.id.about_details_sp)
    TextView textView;

    @BindView(R.id.exp_details)
    TextView expDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.details_sp_profile_fragment_fr, container, false);
        ButterKnife.bind(this, view);

        String json=getArguments().getString("json");
        BookLaterServiceProvider bookLaterServiceProvider=BookLaterServiceProvider.fromJson(json);
        List<Service> services=bookLaterServiceProvider.getServices();
        DetailsServiceAdapter adapter=new DetailsServiceAdapter(context, services);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        textView.setText(bookLaterServiceProvider.getAboutCompany());
        expDetails.setText(bookLaterServiceProvider.getExp()+" Years");
        return view;
    }

    public static DetailsServiceProviderFragment getInstance(BookLaterServiceProvider bookLaterServiceProvider){
        DetailsServiceProviderFragment detailsServiceProviderFragment=new DetailsServiceProviderFragment();
        Bundle bundle=new Bundle();
        bundle.putString("json", bookLaterServiceProvider.toJson());
        detailsServiceProviderFragment.setArguments(bundle);
        return detailsServiceProviderFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}