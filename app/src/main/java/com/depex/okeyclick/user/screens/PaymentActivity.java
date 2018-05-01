package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.contants.Utils;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.pay_btn)
    Button button;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.card)
    CardMultilineWidget cardMultilineWidget;
    @BindView(R.id.total_amount)
    TextView totalAmountText;

    SharedPreferences preferences;
    String totalAmount;
    SpotsDialog dialog;
    String totalStr;
    String adminCommissionStr;
    String spTotalStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        toolbar.setTitle("Payment");
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        dialog=new SpotsDialog(this);
        button.setOnClickListener(this);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        totalStr=bundle.getString("total");
        adminCommissionStr=bundle.getString("admin_commission");
        spTotalStr=bundle.getString("sp_total");
        totalAmountText.setText(getString(R.string.uro)+totalStr);

        preferences=getSharedPreferences(Utils.SERVICE_PREF, MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        //TODO Note : disable for testing purpose
        Card card=cardMultilineWidget.getCard();
        dialog.show();
        card.setName("Service Provider");
        if(!card.validateCard()){
            Toast.makeText(this, "Card is not validate ",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Card is validate ",Toast.LENGTH_LONG).show();
            Stripe stripe=new Stripe(this, getString(R.string.stripe_api_key));
            stripe.createToken(card, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    Log.e("responseDataError", "Payment Error : "+error.toString());
                }

                @Override
                public void onSuccess(Token token) {
                    Log.i("tokenGen", token.getId());
                    String tokenStr=token.getId();
                    Asyn asyn=new Asyn();
                    asyn.execute(tokenStr);

                }
            });
        }

       //finishThis();
    }

    private void finishThis() {
        Bundle bundle=new Bundle();
        bundle.putBoolean("pay", true);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    class Asyn extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... strings) {
            float total=Float.parseFloat(totalStr);
            int amount= (int) (total*100);
            int spTotal=(int)(Float.parseFloat(spTotalStr)*100);
            int commission=(int)(Float.parseFloat(adminCommissionStr)*100);
            com.stripe.Stripe.apiKey=getString(R.string.stripe_api_secret_key);
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "eur");
            params.put("capture", false);
            Map<String, Object> destinationParams = new HashMap<>();
            destinationParams.put("amount", spTotal);
            destinationParams.put("account", "acct_1CDQ2yF6Xk0RlLeQ");
            params.put("destination", destinationParams);
//            params.put("application_fee", 300);
            params.put("source", strings[0]);
//            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount("acct_1CAwwuCoyEC3BPTg").build();

            try {
                Charge charge = Charge.create(params);
                preferences.edit().putString("charge_id", charge.getId()).apply();
                Log.i("responseDataCharge", charge.toJson());
                return charge.getPaid();
            } catch (AuthenticationException e) {
                Log.e("responseDataError","AuthenticationException" +e.toString());
            } catch (InvalidRequestException e) {
                Log.e("responseDataError","InvalidRequestException" +e.toString());
            } catch (APIConnectionException e) {
                Log.e("responseDataError","APIConnectionException" +e.toString());
            } catch (CardException e) {
                Log.e("responseDataError","CardException" +e.toString());
            } catch (APIException e) {
                Log.e("responseDataError","APIException" +e.toString());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
              Bundle bundle=new Bundle();
              bundle.putBoolean("pay", aBoolean);
            Intent intent=new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
