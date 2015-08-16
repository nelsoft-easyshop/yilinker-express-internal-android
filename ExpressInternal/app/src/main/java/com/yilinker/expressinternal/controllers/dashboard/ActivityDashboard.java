package com.yilinker.expressinternal.controllers.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;

public class ActivityDashboard extends AppCompatActivity implements View.OnClickListener, FragmentNavigationDrawer.NavigationDrawerCallbacks, ResponseHandler {

    private View btnJobOrders;

    private FragmentNavigationDrawer mNavigationDrawerFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (FragmentNavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.fragNavDrawer);
        mNavigationDrawerFragment.setUp(R.id.fragNavDrawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set the menu icon instead of the launcher icon.

        final ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("");

        btnJobOrders = findViewById(R.id.btnJobOrders);

        btnJobOrders.setOnClickListener(this);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.flMainContainer, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnJobOrders:

                goToJobOrderList();
                break;

        }
    }

    private void goToJobOrderList(){

        Intent intent = new Intent(getApplicationContext(), ActivityJobOrderList.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

    }

    @Override
    public void onFailed(int requestCode, String message) {

    }
}