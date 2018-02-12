package com.depex.okeyclick.user.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by we on 2/8/2018.
 */

public class OkeyClickTextView extends ViewRender<String> {

    public OkeyClickTextView(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    @Override
    public void bindView(View v, String data) {
        if(v instanceof TextView){
            TextView textView= (TextView) v;
            textView.setText(data);
        }
    }
}
