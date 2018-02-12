package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.model.SubService;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.ServicesRecyclerSubServiceAdapter;
import com.depex.okeyclick.user.api.ApiListener;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.misc.DeviderItemDecorator;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class SubServiceFragment extends Fragment implements ApiListener<JsonObject>, View.OnClickListener {

    List<Service> services;
    RadioGroup radioGroup;
    RecyclerView recyclerView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //SubServiceFragmentView view=new SubServiceFragmentView(getActivity(), R.layout.content_sub_service_fragment);
        View view=inflater.inflate(R.layout.content_sub_service_fragment, container, false);
        recyclerView=view.findViewById(R.id.recycler_view_service);
        //View view1=view.getView(container, )
        Bundle bundle=getArguments();
        SubServiceFragment2 fragment2=new SubServiceFragment2();
        fragment2.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.nav_container, fragment2).commit();

        if(bundle==null){
            Log.i("bundleNull", "Bundle is null ");
            return view;
        }
        String json=bundle.getString("jsonServiceList", "");
        int position=bundle.getInt("position", 0);
        Log.i("positionElements", String.valueOf(position));
        Gson gson=new Gson();
        Log.i("tagFragment", getTag()+"");
        Service[]arr=gson.fromJson(json, Service[].class);
        List<Service> services=new ArrayList<>(Arrays.asList(arr));
        Service service=services.get(position);

        String catid=service.getId();
        ServicesRecyclerSubServiceAdapter adapter=new ServicesRecyclerSubServiceAdapter(getActivity(), services, position);

        adapter.setSelectedService(service);

        LinearLayoutManager manager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DeviderItemDecorator(getActivity()), RecyclerView.HORIZONTAL);
        GravitySnapHelper snapHelper=new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(position);
        ImageView imageView =view.findViewById(R.id.header_image_viewpager);
        radioGroup=view.findViewById(R.id.radiogroup_viewpager);


        GlideApp.with(getActivity())
                .load(Utils.IMAGE_URL+service.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .fitCenter().into(imageView);


        Retrofit.Builder builder=new Retrofit.Builder();
        builder.baseUrl(Utils.SITE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();

        ProjectAPI projectAPI=retrofit.create(ProjectAPI.class);
        Call<JsonObject> subServicesCall=projectAPI.getSubServices(getActivity().getString(R.string.apikey), catid);
        subServicesCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res=response.body();
                if(res==null) return;
                boolean success=res.get("successBool").getAsBoolean();
                if(success){
                    JsonObject resData=res.get("response").getAsJsonObject();
                    JsonArray subservicesList=resData.get("List").getAsJsonArray();
                    Gson gson=new Gson();
                    SubService[]subservicesarr=gson.fromJson(subservicesList, SubService[].class);
                    ArrayList<SubService> subServices=new ArrayList<>(Arrays.asList(subservicesarr));
                    Log.i("responseJson", subServices.toString());
                    radioGroup.removeAllViews();
                    createRadios(radioGroup, subServices);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("responseError", t.toString());
            }
        });

        Button nextButton=view.findViewById(R.id.next_btn_viewpager);
        nextButton.setOnClickListener(this);
        return view;
    }

    private void createRadios(RadioGroup radioGroup, ArrayList<SubService> subServices) {
        boolean checked=true;
        for(SubService sub_service : subServices){
            String subServiceName=sub_service.getSubServiceName();
            RadioButton radioButton=new RadioButton(context);
            radioButton.setText(subServiceName);
            radioButton.setTextSize(18);

            radioButton.setTag(sub_service);
            radioButton.setPadding(30, 0,0,0);
            radioButton.setButtonDrawable(R.drawable.radio_button_draw);
            RadioGroup.LayoutParams params=new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(getResources().getDimensionPixelSize(R.dimen.radio_left_margin), getResources().getDimensionPixelOffset(R.dimen.radio_top_margin),getResources().getDimensionPixelOffset(R.dimen.radio_right_margin),getResources().getDimensionPixelOffset(R.dimen.radio_bottom_margin));
            radioButton.setLayoutParams(params);
            radioGroup.addView(radioButton);
            if(!checked)
                continue;
            radioButton.setChecked(checked);
            checked=false;
        }
    }

    public SubServiceFragment newIntance(List<Service> services, int position){
        this.services=services;
        Gson gson=new Gson();
        String jsonServiceList=gson.toJson(services);
        Bundle bundle=new Bundle();
        bundle.putString("jsonServiceList", jsonServiceList);
        bundle.putInt("position", position);
        SubServiceFragment fragment=new SubServiceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void success(Call<JsonObject> call, Response<JsonObject> response, Object... objects)    {

    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.next_btn_viewpager:

                    int id=radioGroup.getCheckedRadioButtonId();

                  /*  ServicesRecyclerSubServiceAdapter adapter= (ServicesRecyclerSubServiceAdapter) recyclerView.getAdapter();

                    Service service=adapter.getSelectedService();

                    Log.i("serviceTag", service.getServiceName());*/

                    RadioButton radioButton=radioGroup.findViewById(id);
                    SubService service1= (SubService) radioButton.getTag();
                    String subServiceID=service1.getId();
                    String serviceID=service1.getServiceId();

                    JSONObject data=new JSONObject();
                    try {
                        data.put("v_code", getString(R.string.v_code));
                        data.put("apikey", getString(R.string.apikey));
                        data.put("category", serviceID);
                        data.put("subcategory", subServiceID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Fragment fragment=AvailServiceProviderFragment.newInstance(data.toString());
                    FragmentManager manager=getFragmentManager();
                    manager.beginTransaction().replace(R.id.nav_container, fragment).addToBackStack(null).commit();
                    break;
            }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
