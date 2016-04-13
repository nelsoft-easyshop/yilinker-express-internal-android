package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.GoogleMapConstant;
import com.yilinker.expressinternal.model.Branch;
import com.yilinker.expressinternal.model.Warehouse;
import com.yilinker.expressinternal.utilities.DrawableHelper;

import java.util.List;

/**
 * Created by J.Bautista on 4/12/16.
 */
public class WarehouseMarkerAdapter extends GoogleMapMarkerAdapter<Warehouse> {

    private Context context;

    private static View pinView;

    public WarehouseMarkerAdapter(Context context, List<Warehouse> objects, GoogleMap map) {
        super(objects, map);

        this.context = context;
    }

    @Override
    protected MarkerOptions createMapMarker(Warehouse object) {

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng markerLocation = new LatLng(object.getLatitude(), object.getLongitude());
        markerOptions.position(markerLocation);
        markerOptions.title(String.valueOf(GoogleMapConstant.MARKER_TYPE_WAREHOUSE));

        if(pinView == null) {
            pinView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_pin_warehouse2, null);
        }

        TextView tvDropoff = (TextView) pinView.findViewById(R.id.tvDropoff);
        TextView tvClaiming = (TextView) pinView.findViewById(R.id.tvClaiming);

        tvDropoff.setText(String.valueOf(object.getForPickup()));
        tvClaiming.setText(String.valueOf(object.getForClaiming()));

        Bitmap bitmap = createBitmapFromView(pinView);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        markerOptions.snippet(String.valueOf(object.getId()));

        return markerOptions;
    }

    private Bitmap createBitmapFromView(View view){

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Bitmap bitmap = DrawableHelper.createDrawableFromView(windowManager, view);

        return bitmap;
    }

}
