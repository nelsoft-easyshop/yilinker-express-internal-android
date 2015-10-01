package com.yilinker.expressinternal.controllers.navigation;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;

import java.util.List;

public class ActivityNavigation extends BaseActivity implements RoutingListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String ARG_DESTINATION_LAT = "latitude";
    public static final String ARG_DESTINATION_LONG = "longitude";

    private GoogleMap map; // Might be null if Google Play services APK is not available.

    private TextView tvDistance;

    private LatLng curentLocation;
    private LatLng destination;

    private GoogleApiClient mGoogleApiClient;

    private PolylineOptions polyRoute;
    private int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        getData();

        buildGoogleApiClient();

        initViews();

        setUpMapIfNeeded();

    }

    @Override
    protected void handleRefreshToken() {



    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
//            if (map != null) {
//                setUpMap();
//            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolylineOptions, Route route) {

        if(polyRoute == null) {

            distance = route.getLength();
            polyRoute = new PolylineOptions();
            polyRoute.color(Color.RED);
            polyRoute.width(10);
            polyRoute.addAll(mPolylineOptions.getPoints());

            resetRoute();
        }


    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onConnected(Bundle bundle) {

        Location mLastLocation  = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        curentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        if(curentLocation != null){

            if (polyRoute == null) {

                setupRoute();

            } else {

                resetRoute();
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void initViews(){

        tvDistance = (TextView) findViewById(R.id.tvDistance);

        //For Action Bar
        setActionBarTitle(getString(R.string.actionbar_title_navigation));
        setActionBarBackgroundColor(R.color.marigold);

    }

    private void getData(){

        Intent intent = getIntent();

//        TODO Uncomment this
        double destinationLat = intent.getDoubleExtra(ARG_DESTINATION_LAT, 0.0);
        double destinationLong = intent.getDoubleExtra(ARG_DESTINATION_LONG, 0.0);
        destination = new LatLng(destinationLat, destinationLong);

//        destination = new LatLng(14.4336373,121.0165277);
    }

    private void setupRoute(){

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.BIKING)
                .withListener(this)
                .waypoints(curentLocation, destination)
                .build();
        routing.execute();

    }

    private void resetRoute() {

        if (map != null) {

            map.clear();

            List<LatLng> list = polyRoute.getPoints();

            //Get previous location
            LatLng previous = list.get(0);

            if(previous.longitude > 0 && previous.latitude > 0 && curentLocation.latitude > 0 && curentLocation.longitude > 0) {
                float[] displacement = new float[3];
                Location.distanceBetween(previous.latitude, previous.longitude, curentLocation.latitude, curentLocation.longitude, displacement);

                distance -= (int) (displacement[0] / 1000);

                list.set(0, curentLocation);

                map.addMarker(new MarkerOptions().position(curentLocation).title("Marker")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.navigation_current));
                map.addMarker(new MarkerOptions().position(destination).title("Marker")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.navigation_destination));
                map.addPolyline(polyRoute);

                //Zoom to see the whole polyline
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(list.get(0));
                builder.include(list.get(list.size() - 1));
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
            }

            String text = null;

            if(distance > 0){

               text = String.format("%.2f KM", distance/1000.0);
            }
            else{

                text = "Route Not Available";
            }

            tvDistance.setText(text);
        }


    }

}
