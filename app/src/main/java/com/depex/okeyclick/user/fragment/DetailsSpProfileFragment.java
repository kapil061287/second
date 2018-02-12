package com.depex.okeyclick.user.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.view.OkeyClickTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsSpProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.details_sp_profile_fragment, container, false);
        String res=getArguments().getString("resObj", "Empty response");
        TextView categoryNameProviderProfile=view.findViewById(R.id.category_name_profile_txt);

        try {
            JSONObject response=new JSONObject(res);
            Log.i("responseData", "Response User Details : "+response);
            JSONArray arr=response.getJSONArray("List");
            JSONObject resObjDetails=arr.getJSONObject(0);
            JSONArray array=resObjDetails.getJSONArray("subcategory");
            for(int i=0;i<array.length();i++){
                JSONObject subCat=array.getJSONObject(i);
                String category=resObjDetails.getString("category");
                String service_name=subCat.getString("service_name");
                String user_image=resObjDetails.getString("user_images");
                OkeyClickTextView v=new OkeyClickTextView(getActivity(), R.layout.okey_click_text_view);
                LinearLayout linearLayout=view.findViewById(R.id.service_linear_layout);
                View okeyClickView=v.getView(linearLayout, service_name);
                categoryNameProviderProfile.setText(category);
                linearLayout.addView(okeyClickView);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  view;

    }

}