package com.depex.okeyclick.user.fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.ServicesRecyclerAdapter;
import com.depex.okeyclick.user.api.ApiListener;
import com.depex.okeyclick.user.api.CallbackApi;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class HomeFragment extends Fragment implements ApiListener<JsonObject> {
    @BindView(R.id.recycler_view_services)
    RecyclerView services_recycler;
    private String tag="HomeFragment";
    private SpotsDialog spotsDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("tagWithTag", "Toast is cleared !");
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.content_home_fragment, container, false);
        ButterKnife.bind(this, view);
        spotsDialog=new SpotsDialog(getActivity());
        spotsDialog.show();
        Retrofit.Builder builder=new Retrofit.Builder();
        Retrofit retrofit=builder.baseUrl(Utils.SITE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ProjectAPI api=retrofit.create(ProjectAPI.class);
        Call<JsonObject> servicesCall=api.getServices(getString(R.string.apikey));
        CallbackApi callbackApi=new CallbackApi(this);
        servicesCall.enqueue(callbackApi);

        return view;
    }


    @Override
    public void success(Call<JsonObject> call, Response<JsonObject> response, Object... objects) {
        if(response!=null){
        JsonObject res=response.body();
        boolean success=res.get("successBool").getAsBoolean();
        String responseType=res.get("responseType").getAsString();
        Gson gson=new Gson();
        if(success) {
            JsonObject responseData = res.get("response").getAsJsonObject();
            switch (responseType) {
                case "get_category":
                    JsonArray service_list = responseData.get("List").getAsJsonArray();
                    Service[] arr = gson.fromJson(service_list, Service[].class);
                    ArrayList<Service> services = new ArrayList<>(Arrays.asList(arr));
//                    Log.i("responseData", "Services : "+services.get(0).getSubServices().get(0).getId());
                    ServicesRecyclerAdapter adapter = new ServicesRecyclerAdapter(getActivity(), services, getFragmentManager());
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    services_recycler.setLayoutManager(manager);
                    services_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                    services_recycler.setAdapter(adapter);
                    spotsDialog.dismiss();
                    break;
            }
        }


            if(objects.length>0){
                if(objects[0] instanceof String){
                    if(objects[0].equals("error")){
                        spotsDialog.dismiss();
                        Toast.makeText(getActivity(), "Network Error Please Try later !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        if(response!=null)
        Log.i(tag ,response.body().toString() );
    }
}