package com.yilinker.expressinternal.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.yilinker.expressinternal.R;

/**
 * Created by J.Bautista
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private ImageView ivMenu;

    private PopupWindow menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBar();
    }

    public void hideMenuButton(){

        ivMenu.setVisibility(View.GONE);
    }

    public void dismissMenu(){

        menu.dismiss();
    }

    public void setMenu(final PopupWindow menu){

        this.menu = menu;

        //Hide menu when the user clicks outside the listview
        menu.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissMenu();
            }
        });
    }

    public void showMenu(){

        if(menu != null) {

            int location[] = new int[2];

            // Get the View's(the one that was clicked in the Fragment) location
            ivMenu.getLocationOnScreen(location);

            // Using location, the PopupWindow will be displayed right under anchorView
            menu.showAtLocation(ivMenu, Gravity.NO_GRAVITY, 0, 0);


        }
    }

    private void setActionBar(){

        final ActionBar ab = getSupportActionBar();

        ab.setElevation(0);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.layout_actionbar);

        ivBack = (ImageView) ab.getCustomView().findViewById(R.id.ivBack);
        ivMenu = (ImageView) ab.getCustomView().findViewById(R.id.ivMenu);

        ivBack.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ivBack:

                onBackPressed();
                break;

            case R.id.ivMenu:

                if(menu.isShowing()){

                    dismissMenu();
                }
                else {
                    showMenu();
                }
                break;

        }

    }
}
