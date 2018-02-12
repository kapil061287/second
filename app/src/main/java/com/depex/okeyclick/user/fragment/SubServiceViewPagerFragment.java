package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.airbnb.lottie.animation.content.Content;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.model.SubService;
import com.depex.okeyclick.user.view.SubCatRadioButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubServiceViewPagerFragment extends Fragment implements View.OnClickListener {

    private Context context;
    RadioGroup group;
    Button nextButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_subcategory_view_pager_fragment, container, false);

        Bundle bundle=getArguments();
        nextButton=view.findViewById(R.id.next_btn_viewpager);
        nextButton.setOnClickListener(this);
        String json=bundle.getString("json", "");
        Log.i("responseJsonBundle", json);



        /*if(true)
        return view;*/
        try {
            group=view.findViewById(R.id.radiogroup_viewpager);
            JSONObject jsonObject=new JSONObject(json);
            String description=jsonObject.getString("description");
            String id=jsonObject.getString("id");
            String images=jsonObject.getString("images");
            String serviceName=jsonObject.getString("service_name");
            String updateDate=jsonObject.getString("update_date");
            JSONArray jsonArray=jsonObject.getJSONArray("subcategory");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String subServicename=jsonObject1.getString("service_name");
                SubCatRadioButton subCatRadioButton=new SubCatRadioButton(context, R.layout.content_sub_cat_radio);
                View view1=subCatRadioButton.getView(group, subServicename);
                SubService subService=new Gson().fromJson(jsonObject1.toString(), SubService.class);
                view1.setTag(subService);
                group.addView(view1);
            }




         String url=Utils.IMAGE_URL+images;
         TextView textView=view.findViewById(R.id.overlay_text_subcategory_fragment);
         textView.setText(serviceName);



       Log.i("responseDataBund", url);

        ImageView imageView=view.findViewById(R.id.header_image_viewpager);



        GlideApp.with(context).load(url).into(imageView);

        } catch (JSONException e) {
            Log.e("responseDataError", e.toString());
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onClick(View view) {
        int a=group.getCheckedRadioButtonId();
        RadioButton button=group.findViewById(a);
        SubService subService= (SubService) button.getTag();
        JSONObject data=new JSONObject();
        String subServiceID=subService.getId();
        String serviceID=subService.getServiceId();
        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("category", serviceID);
            data.put("subcategory", subServiceID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Fragment fragment=AvailServiceProviderFragment.newInstance(data.toString());
        FragmentManager manager=getParentFragment().getFragmentManager();
        manager.beginTransaction().replace(R.id.nav_container, fragment).addToBackStack(null).commit();

        Log.i("responseRadio", subService.getId()+"");
    }
}