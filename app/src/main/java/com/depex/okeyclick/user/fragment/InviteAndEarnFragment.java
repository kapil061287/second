package com.depex.okeyclick.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.screens.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InviteAndEarnFragment extends Fragment implements View.OnTouchListener {

    @BindView(R.id.reffer_code)
    EditText refferCode;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_invite_and_earn_fragment, container, false);
        ButterKnife.bind(this,view);
        refferCode.setOnTouchListener(this);
        Toolbar toolbar=getActivity().getWindow().getDecorView().findViewById(R.id.toolbar);
        toolbar.setTitle("Invite & Earn");
        return view;
    }




    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
               /* final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;*/
        final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;

        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if(motionEvent.getRawX() >= (refferCode.getRight() -80)) {
                Log.i("onTouchLis", "Touch event occurd");
                //Toast.makeText(HomeActivity.this, findAutoTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                startShare();
                return true;
            }
        }
        return false;
    }

    private void startShare() {
        Intent sendData=new Intent(Intent.ACTION_SEND);
        sendData.putExtra(Intent.EXTRA_TEXT, "http://url-"+refferCode.getText().toString());
        sendData.setType("text/plain");
        startActivity(sendData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
