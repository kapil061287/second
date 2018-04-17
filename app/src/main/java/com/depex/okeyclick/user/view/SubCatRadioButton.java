package com.depex.okeyclick.user.view;
import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

public class SubCatRadioButton extends ViewRender<String> {


    public SubCatRadioButton(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    @Override
    public void bindView(View v, String data) {
        if(v instanceof RadioButton){
            RadioButton button= (RadioButton) v;
            button.setText(data);
        }
    }
}
