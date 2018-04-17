package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.depex.okeyclick.user.R;
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
import com.stripe.net.RequestOptions;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.pay_btn)
    Button button;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.card)
    CardMultilineWidget cardMultilineWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        toolbar.setTitle("Payment");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //TODO Note : disable for testing purpose
       /* Card card=cardMultilineWidget.getCard();
        card.setName("Service Provider");
        if(!card.validateCard()){
            Toast.makeText(this, "Card is not validate ",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Card is validate ",Toast.LENGTH_LONG).show();
            Stripe stripe=new Stripe(this, "pk_test_qdtBZkSgzZkD36xIS8ASlKcm");
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
        }*/

       finishThis();
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

            com.stripe.Stripe.apiKey="sk_test_dYNtoGXjfL5Le0gABqZSvmzB";
            Map<String, Object> params = new HashMap<>();
            params.put("amount", 10000);
            params.put("currency", "usd");
            params.put("capture", true);
            //params.put("application_fee", 300);
            params.put("source", strings[0]);
            //RequestOptions requestOptions = RequestOptions.builder().setStripeAccount("acct_1CAwwuCoyEC3BPTg").build();
            try {
                Charge charge = Charge.create(params);
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
              Bundle bundle=new Bundle();
              bundle.putBoolean("pay", aBoolean);
            Intent intent=new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
