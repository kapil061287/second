package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.listener.OnSubserviceSelectListener;
import com.depex.okeyclick.user.model.SubService;

import java.util.List;


public class SubserviceSelectAdapter extends BaseExpandableListAdapter {

    private List<SubService> subServices;
    private Context context;
    public static final int DEFAULT_CONST_SELECT=2898;
    int expandGroupPosition;
    CheckBox selectBox;
    OnSubserviceSelectListener onSubserviceSelectListener;
    int[]selectBoxIds=new int[]{DEFAULT_CONST_SELECT, DEFAULT_CONST_SELECT};
    private ExpandableListView listView;
    public SubserviceSelectAdapter(List<SubService> subServices, Context context, ExpandableListView listView, OnSubserviceSelectListener onSubserviceSelectListener){
        this.context=context;
        this.subServices=subServices;
        this.listView=listView;
        this.onSubserviceSelectListener=onSubserviceSelectListener;
    }

    @Override
    public int getGroupCount() {
        if(subServices!=null)
        return subServices.size();
        else return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        SubService subService=subServices.get(i);
        int maxLimit=subService.getMaxLimit();
        return maxLimit;
    }

    @Override
    public Object getGroup(int i) {
        return subServices.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return subServices.get(i).getMaxLimit();
    }

    @Override
    public long getGroupId(int i) {
        return subServices.get(i).hashCode();
    }

    @Override
    public long getChildId(int i, int i1) {
        return subServices.get(i).getMaxLimit();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View view1=LayoutInflater.from(context).inflate(R.layout.content_sub_service_select_group_view, viewGroup, false);
        TextView textView=view1.findViewById(R.id.select_view_group_text);
        TextView priceText=view1.findViewById(R.id.price_select_view);
        SubService subService=subServices.get(i);
        String minHrPrice=subService.getMin_hr_price();
        priceText.setText("$"+minHrPrice+"/hr");
        String subServiceName=subService.getSubServiceName();
        textView.setText(subServiceName);
        return view1;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        View view1=LayoutInflater.from(context).inflate(R.layout.content_sub_service_select_child_view, viewGroup, false);
        final CheckBox checkBox=view1.findViewById(R.id.checkbox_in_sub_service_select);
        final SubService subService=subServices.get(i);
        String type=subService.getServiceType();
        checkBox.setText(String.valueOf(i1+1)+" "+type);
        if(selectBoxIds[0]==i && selectBoxIds[1]==i1){
            checkBox.setChecked(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSelectBox(checkBox);
                if(b){
                    onSubserviceSelectListener.onSubserviceSelect(subService, i1+1);
                    selectBoxIds[0]=i;
                    selectBoxIds[1]=i1;
                }
            }
        });
        return view1;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
            if(expandGroupPosition!=groupPosition){
                listView.collapseGroup(expandGroupPosition);
                expandGroupPosition=groupPosition;
            }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    public void setSelectBox(CheckBox selectBox) {
        if(this.selectBox!=null){
            this.selectBox.setChecked(false);
            this.selectBox=selectBox;
        }else {
            this.selectBox=selectBox;
        }
    }
}
