package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class StripeCardAdapter extends BaseAdapter {


    List<ExternalAccount> externalAccounts;
    Context context;
    public StripeCardAdapter(Context context, ExternalAccountCollection collection){
        if(collection!=null)
        this.externalAccounts=collection.getData();
        this.context=context;
    }

    @Override
    public int getCount() {
        if(externalAccounts!=null)
        return externalAccounts.size();
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        return externalAccounts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return externalAccounts.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1=LayoutInflater.from(context).inflate(R.layout.content_stripe_single_card_layout, viewGroup, false);
        TextView textView=view1.findViewById(R.id.card_digit);
        ExternalAccount account=externalAccounts.get(i);
        ImageView imageView=view1.findViewById(R.id.brand_img);
        GlideApp.with(context).load(R.drawable.history_icon).into(imageView);
        Log.i("responseData", "Stripe Card Adapter : "+account.toJson());
        try {
            JSONObject jsonObject=new JSONObject(account.toJson());
            String last4=jsonObject.getString("last4");
            textView.setText("XXXX XXXX XXXX "+last4);

        } catch (JSONException e) {


        }

        return view1;
    }
}
