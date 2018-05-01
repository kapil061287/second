package com.depex.okeyclick.user.screens;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.depex.okeyclick.user.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WebInnerViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.innner_web_view)
    WebView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_inner_view);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title_color));
        String title=getIntent().getExtras().getString("title");
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent=getIntent();
        String url=intent.getExtras().getString("url");

        view.loadUrl(url);

        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed() {
        if(view.canGoBack()){
            view.goBack();
        }else {
            super.onBackPressed();
        }
    }
}