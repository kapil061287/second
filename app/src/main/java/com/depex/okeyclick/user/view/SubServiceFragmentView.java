package com.depex.okeyclick.user.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.model.SubService;
import com.depex.okeyclick.user.model.SubServiceFragmentData;
import com.depex.okeyclick.user.R;



public class SubServiceFragmentView extends ViewRender<SubServiceFragmentData> {
    private  Context context;

    public SubServiceFragmentView(Context context, int layoutRes) {
        super(context, layoutRes);
        this.context=context;
    }

    @Override
    public void bindView(View v, SubServiceFragmentData data) {
        ImageView headerImageViewPager=v.findViewById(R.id.header_image_viewpager);
        //RadioGroup radioGroup=v.findViewById(R.id.radiogroup_viewpager);
        Button nextButton=v.findViewById(R.id.next_btn_viewpager);

        GlideApp.with(context).load(data.getServiceImgUrl()).fitCenter().into(headerImageViewPager);
        for(SubService service : data.getSubCategories()){
            String name=service.getSubServiceName();
            RadioButton button=new RadioButton(context);
            button.setText(name);
            button.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));
           // radioGroup.addView(button);
        }
    }
}