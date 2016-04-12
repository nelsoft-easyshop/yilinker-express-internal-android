package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.GoogleMapConstant;
import com.yilinker.expressinternal.model.Branch;
import com.yilinker.expressinternal.utilities.DrawableHelper;

import java.util.List;

/**
 * Created by J.Bautista on 4/11/16.
 */
public class BranchMapMarkerAdapter extends GoogleMapMarkerAdapter<Branch> {

    private Context context;

    private static View pinView;

    public BranchMapMarkerAdapter(Context context, List<Branch> objects, GoogleMap map) {
        super(objects, map);

        this.context = context;
    }

    @Override
    protected MarkerOptions createMapMarker(Branch object) {

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng markerLocation = new LatLng(object.getLatitude(), object.getLongitude());
        markerOptions.position(markerLocation);
        markerOptions.title(String.valueOf(GoogleMapConstant.MARKER_TYPE_BRANCH));

        if(pinView == null) {
            pinView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_pin_branch, null);
        }

        TextView tvDropoff = (TextView) pinView.findViewById(R.id.tvDropoff);
        TextView tvClaiming = (TextView) pinView.findViewById(R.id.tvClaiming);

        tvDropoff.setText(String.valueOf(object.getForDropoffCount()));
        tvClaiming.setText(String.valueOf(object.getForClaimingCount()));

        Bitmap bitmap = createBitmapFromView(pinView);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        markerOptions.snippet(object.getName());

        return markerOptions;
    }

    private Bitmap createBitmapFromView(View view){

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Bitmap bitmap = DrawableHelper.createDrawableFromView(windowManager, view);

        return bitmap;
    }

}
