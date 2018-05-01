package com.depex.okeyclick.user.fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.listener.OnSubserviceSelectListener;
import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.model.SubService;
import com.depex.okeyclick.user.view.SubserviceSelectView;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class SubServiceViewPagerFragment extends Fragment implements View.OnClickListener, OnSubserviceSelectListener {

    private Context context;
   // RadioGroup group;
    @BindView(R.id.next_btn_viewpager)
    Button nextButton;
    @BindView(R.id.parent_linear_select_view)
    LinearLayout parentLinear;
    SubService subService;
    int quantityOfWork;
    SharedPreferences preferences;

    SpotsDialog spotsDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_subcategory_view_pager_fragment, container, false);
        ButterKnife.bind(this, view);
        spotsDialog=new SpotsDialog(context);
        Bundle bundle=getArguments();
        nextButton.setOnClickListener(this);
        String json=bundle.getString("json", "");
        Log.i("responseJsonBundle", json);
        preferences=context.getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        /*if(true)
        return view;*/
        try {

            JSONObject jsonObject=new JSONObject(json);
            Service service=new Gson().fromJson(jsonObject.toString(), Service.class);
            String serviceName=service.getServiceName();
            List<SubService> subServices=service.getSubServices();
            String image=service.getImageUrl();
            SubserviceSelectView subserviceSelectView=new SubserviceSelectView(context);
            View view1=subserviceSelectView.getView(parentLinear,subServices, this);
            parentLinear.addView(view1);
         String url=Utils.IMAGE_URL+image;
         TextView textView=view.findViewById(R.id.overlay_text_subcategory_fragment);
         textView.setText(serviceName);
        Log.i("responseDataBund", url);
        ImageView imageView=view.findViewById(R.id.header_image_viewpager);

        GlideApp.with(context)
                .load(url)

                .into(imageView);

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


        if(subService==null && quantityOfWork ==0){
            Toast.makeText(context, "Please Choose minimum one service !", Toast.LENGTH_LONG).show();
            return;
        }

        spotsDialog.show();

            preferences.edit().putString("quanOfWork", String.valueOf(quantityOfWork)).apply();

            JSONObject data = new JSONObject();
            String subServiceID = subService.getId();
            String serviceID = subService.getServiceId();
            try {
                data.put("v_code", getString(R.string.v_code));
                data.put("apikey", getString(R.string.apikey));
                data.put("category", serviceID);
                data.put("subcategory", subServiceID);
                data.put("quanOfWork", quantityOfWork);
                data.put("subserviceName", subService.getSubServiceName());

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            Fragment fragment = AvailServiceProviderFragment.newInstance(data.toString());
            fragment.setHasOptionsMenu(true);
            FragmentManager manager = getParentFragment().getFragmentManager();
            manager.beginTransaction().replace(R.id.nav_container, fragment).addToBackStack(null).commit();
            spotsDialog.dismiss();

            Log.i("responseRadio", subService.getId() + "");


       /* int a=group.getCheckedRadioButtonId();
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

        Log.i("responseRadio", subService.getId()+"");*/
    }

    @Override
    public void onSubserviceSelect(SubService subService, int quantityOfWork) {

        this.subService=subService;
        this.quantityOfWork=quantityOfWork;
        Log.i("responseData", "OnSubservice select : "+subService+" Quan : "+quantityOfWork);

    }
}