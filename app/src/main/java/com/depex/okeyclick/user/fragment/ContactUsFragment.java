package com.depex.okeyclick.user.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.depex.okeyclick.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by we on 1/17/2018.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.term_condition_textview)
    TextView termCondition;
    @BindView(R.id.data_protection_low_textView)
    TextView dataProtectionLow;

    @BindView(R.id.web_site_link_text_view)
    TextView webSiteLinkView;
    @BindView(R.id.faq_text)
    TextView faqTextView;

    String webSiteLink="http://www.okeyclick.com";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_contact_us_fragment, container, false);
        ButterKnife.bind(this, view);
        String termConditionText="<u>"+getString(R.string.term_and_condition)+"</u>";
        String dataProtectionLowtext="<u>"+getString(R.string.data_protection_low)+"</u>";
        String webSiteLinkText="<u> www.okeyclick.com</u>";
        String faqText="<u>"+getString(R.string.faq)+"</u>";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            termCondition.setText(Html.fromHtml(termConditionText, Html.FROM_HTML_MODE_LEGACY));
            dataProtectionLow.setText(Html.fromHtml(dataProtectionLowtext, Html.FROM_HTML_MODE_LEGACY));
            webSiteLinkView.setText(Html.fromHtml(webSiteLinkText, Html.FROM_HTML_MODE_LEGACY));
            faqTextView.setText(Html.fromHtml(faqText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            termCondition.setText(Html.fromHtml(termConditionText));
            dataProtectionLow.setText(Html.fromHtml(dataProtectionLowtext));
            webSiteLinkView.setText(Html.fromHtml(webSiteLinkText));
            faqTextView.setText(Html.fromHtml(faqText));
        }

        webSiteLinkView.setTextColor(Color.BLUE);
        webSiteLinkView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.web_site_link_text_view:
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webSiteLink));
                startActivity(intent);
                break;
        }
    }
}