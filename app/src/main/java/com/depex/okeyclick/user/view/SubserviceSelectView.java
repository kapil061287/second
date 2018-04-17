package com.depex.okeyclick.user.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.SubserviceSelectAdapter;
import com.depex.okeyclick.user.listener.OnSubserviceSelectListener;
import com.depex.okeyclick.user.model.SubService;

import java.util.List;


public class SubserviceSelectView extends ViewRender<List<SubService>>{

    Context context;
    private OnSubserviceSelectListener listener;

    public SubserviceSelectView(Context context) {
        super(context, R.layout.content_sub_service_select_view);
        this.context=context;
    }

    @Override
    public void bindView(View v, List<SubService> data) {
        if(v instanceof ExpandableListView){
            ExpandableListView listView= (ExpandableListView) v;
            SubserviceSelectAdapter adapter=new SubserviceSelectAdapter(data, context, listView, this.listener);
            listView.setAdapter(adapter);
        }
    }


    public View getView(ViewGroup parent, List<SubService> data, OnSubserviceSelectListener listener) {
        this.listener=listener;
        return getView(parent, data);
    }
}
