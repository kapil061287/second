package com.depex.okeyclick.user.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.adpater.StripeCardAdapter;
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
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccountCollection;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;
    @BindView(R.id.no_save_card)
    TextView noSaveCards;

    @BindView(R.id.cancel_account)
    Button cancelAccBtn;

    @BindView(R.id.add_account)
    Button addAccBtn;

    @BindView(R.id.card_list_view)
    SwipeMenuListView cardListView;

    @BindView(R.id.card)
    CardMultilineWidget cardMultilineWidget;
    private Context context;

    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_account_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        preferences=context.getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        addAccBtn.setOnClickListener(this);
        CardListTask task=new CardListTask();
        task.execute();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.account_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.add_account:
                        parentLayout.setVisibility(View.VISIBLE);
                        noSaveCards.setVisibility(View.GONE);
                    break;
            }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_account:
                    initSaveCard();
                break;
            case R.id.cancel_account:
                break;
        }
    }

    private void initSaveCard() {
        final Card card=cardMultilineWidget.getCard();
        if(!card.validateCard()){
            Toast.makeText(context, "Card is not validate ",Toast.LENGTH_LONG).show();
        }else {
            Stripe stripe=new Stripe(context, getString(R.string.stripe_api_key));
            stripe.createToken(card, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    Log.e("responseDataError", "Card Error : "+error.toString());
                }

                @Override
                public void onSuccess(Token token) {
                    Log.i("responseData","String Token Card Save : "+ token.getId());
                        String tokenStr=token.getId();
                        CardSaveTask task=new CardSaveTask();
                        task.execute(tokenStr);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    class CardSaveTask extends AsyncTask<String, String , String >{

        @Override
        protected String doInBackground(String... strings) {
            com.stripe.Stripe.apiKey=getString(R.string.stripe_api_secret_key);
            String csId=preferences.getString("cs_id", "0");
            Map<String ,Object> params=new HashMap<>();
            params.put("source", strings[0]);
            try {
                Customer customer=Customer.retrieve(csId);
                customer.getSources().create(params);
                CardListTask task=new CardListTask();
                task.execute();
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


            return null;
        }
    }



    private class  CardListTask extends AsyncTask<String, String, ExternalAccountCollection>{

        @Override
        protected ExternalAccountCollection doInBackground(String... strings) {

            com.stripe.Stripe.apiKey=getString(R.string.stripe_api_secret_key);
            String csId=preferences.getString("cs_id", "0");
            Map<String ,Object> params=new HashMap<>();
            params.put("object", "card");
            try {
                Customer customer=Customer.retrieve(csId);
                ExternalAccountCollection collection=customer.getSources().list(params);
        return collection;
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

            return null;
        }

        @Override
        protected void onPostExecute(ExternalAccountCollection externalAccountCollection) {
            StripeCardAdapter adapter=new StripeCardAdapter(context, externalAccountCollection);
            cardListView.setAdapter(adapter);
            noSaveCards.setVisibility(View.GONE);
        }
    }
}
