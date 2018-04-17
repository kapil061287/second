package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.ReviewAdapter;
import com.depex.okeyclick.user.model.BookLaterServiceProvider;
import com.depex.okeyclick.user.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewsServiceProviderFragment extends Fragment {

    @BindView(R.id.recycler_view_review)
    RecyclerView recyclerView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_review_sp_profile_fragment, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle=getArguments();
        String json=bundle.getString("json");
        BookLaterServiceProvider bookLaterServiceProvider=BookLaterServiceProvider.fromJson(json);

        ReviewAdapter adapter=new ReviewAdapter(context, bookLaterServiceProvider.getReviews());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
//        Log.i("responseData", "From Review : "+bookLaterServiceProvider.getReviews().get(0).getCreatedDate());
        return view;
    }

    public static ReviewsServiceProviderFragment getInstance(BookLaterServiceProvider provider){
        ReviewsServiceProviderFragment fragment=new ReviewsServiceProviderFragment();
        Bundle bundle=new Bundle();
        bundle.putString("json", provider.toJson());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
