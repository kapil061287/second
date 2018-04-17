package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.model.Service;
import com.depex.okeyclick.user.model.SubService;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.screens.HomeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServicesRecyclerSubServiceAdapter  extends RecyclerView.Adapter<ServicesRecyclerSubServiceAdapter.ServiceRecyclerViewHolder>{
    private List<Service> services;
    Context context;
    View currentView;
    View lastView;
    int position;

    private Service selectedService;

    public ServicesRecyclerSubServiceAdapter(Context context, List<Service> services, int position){
        this.context=context;
        this.services=services;
        this.position=position;
    }


    public void setSelectedService(Service selectedService) {
        this.selectedService = selectedService;
    }

    public Service getSelectedService() {
        return selectedService;
    }

    @Override
    public ServiceRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.subservice_recycler_layout, parent, false);
        return new ServiceRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceRecyclerViewHolder holder, int position) {
        Service service=services.get(position);
        holder.itemView.setTag(service);
        String name=service.getServiceName();
        String url=service.getImageUrl();
        if(holder.serviceName==null){
            Toast.makeText(context, "Null is ", Toast.LENGTH_LONG).show();
            return;
        }
        holder.serviceName.setText(name.toUpperCase());
        if(this.position==position){
            setCurrentView(holder.serviceName);
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }



    public  class ServiceRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView serviceName;

        View itemView;

        public ServiceRecyclerViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            serviceName=itemView.findViewById(R.id.service_name_txt);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
                if(context instanceof HomeActivity){
                    HomeActivity homeActivity= (HomeActivity) context;
                    Fragment fragment=homeActivity.getSupportFragmentManager().findFragmentByTag("service");
                    View view1=fragment.getView();
                    ImageView imageView=view1.findViewById(R.id.header_image_viewpager);
                    //final RadioGroup radioGroup=view1.findViewById(R.id.radiogroup_viewpager);


                    Service service= (Service) view.getTag();

                    setSelectedService(service);

                    String catid=service.getId();

                    Retrofit.Builder builder=new Retrofit.Builder();
                    builder.baseUrl(Utils.SITE_URL);
                    builder.addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit=builder.build();

                    ProjectAPI projectAPI=retrofit.create(ProjectAPI.class);
                    Call<JsonObject> subServicesCall=projectAPI.getSubServices(context.getString(R.string.apikey), catid);
                    subServicesCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                JsonObject res=response.body();
                                boolean success=res.get("successBool").getAsBoolean();
                                if(success){
                                    JsonObject resData=res.get("response").getAsJsonObject();
                                    JsonArray subservicesList=resData.get("List").getAsJsonArray();
                                    Gson gson=new Gson();
                                    SubService[]subservicesarr=gson.fromJson(subservicesList, SubService[].class);
                                    ArrayList<SubService> subServices=new ArrayList<>(Arrays.asList(subservicesarr));
                                    Log.i("responseJson", subServices.toString());
                                    //radioGroup.removeAllViews();
                                    //createRadios(radioGroup, subServices);
                                }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("responseError", t.toString());
                        }
                    });


                   TextView textView=view.findViewById(R.id.service_name_txt);
                   setCurrentView(textView);
                    GlideApp.with(context)
                            .load(Utils.IMAGE_URL+service.getImageUrl())
                            .into(imageView);
                }
        }
    }

    private void createRadios(RadioGroup radioGroup, ArrayList<SubService> subServices) {
        boolean checked=true;
        for(SubService sub_service : subServices){
            String subServiceName=sub_service.getSubServiceName();
            RadioButton radioButton=new RadioButton(context);
            radioButton.setText(subServiceName);
            radioButton.setTag(sub_service);
            radioButton.setButtonDrawable(R.drawable.radio_button_draw);
            radioButton.setPadding(30, 0,0,0);
            RadioGroup.LayoutParams params=new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(context.getResources().getDimensionPixelSize(R.dimen.radio_left_margin), context.getResources().getDimensionPixelOffset(R.dimen.radio_top_margin), context.getResources().getDimensionPixelOffset(R.dimen.radio_right_margin),context.getResources().getDimensionPixelOffset(R.dimen.radio_bottom_margin));
            radioButton.setLayoutParams(params);
            radioGroup.addView(radioButton);

            if(!checked)
                continue;
            radioButton.setChecked(checked);
            checked=false;
        }
    }


    public void setCurrentView(View currentView) {
        setLastView(this.currentView);
        this.currentView = currentView;
        if(currentView!=null){
            if(currentView instanceof TextView){
                TextView t= (TextView) currentView;
                t.setTextColor(Color.parseColor("#ffffffff"));
                t.setBackgroundColor(context.getResources().getColor(R.color.tab_color));
            }
        }
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setLastView(View lastView) {
        this.lastView = lastView;
        if(currentView!=null){
            if(currentView instanceof TextView){
                TextView t= (TextView) currentView;
                t.setTextColor(Color.parseColor("#ff000000"));
                t.setBackgroundColor(Color.parseColor("#ffffffff"));
            }
        }
    }

    public View getLastView() {
        return lastView;
    }
}