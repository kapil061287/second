package com.depex.okeyclick.user.adpater;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by we on 4/14/2018.
 */

public class GoogleInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    public GoogleInfoWindowAdapter(Context context){

    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
