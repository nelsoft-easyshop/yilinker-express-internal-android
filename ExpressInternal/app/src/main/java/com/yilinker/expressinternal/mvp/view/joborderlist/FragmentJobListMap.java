package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;

import java.util.List;

/**
 * Created by J.Bautista on 3/1/16.
 */
public class FragmentJobListMap extends Fragment implements IJobListMapView {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobs_map, container, false);

        return view;
    }

    @Override
    public void loadJobOrderList(List<JobOrder> jobOrders) {

    }

    @Override
    public void showJobOrderDetails(JobOrder joborder) {

    }


    @Override
    public void initializeMap() {

    }

    @Override
    public void addCurrentLocationMarker(JobOrder marker) {

    }

    @Override
    public void addMarkers(List<JobOrder> markers) {

    }

    @Override
    public void removeMarkers(List<JobOrder> markers) {

    }

    @Override
    public void centerMap(double latitude, double longitude) {

    }

    @Override
    public void zoomMap(double scale, double centerLatitude, double centerLongitude) {

    }
}
