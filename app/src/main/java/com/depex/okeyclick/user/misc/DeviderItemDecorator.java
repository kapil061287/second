package com.depex.okeyclick.user.misc;

import android.content.Context;

import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;



public class DeviderItemDecorator extends Y_DividerItemDecoration {



    public DeviderItemDecorator(Context context) {
        super(context);
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider=new Y_DividerBuilder()
                        .setLeftSideLine(true, 0x66bbbbbb, 3,0,0)
                        .create();

        return divider;
    }
}
