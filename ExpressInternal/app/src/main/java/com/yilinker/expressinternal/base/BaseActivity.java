package com.yilinker.expressinternal.base;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.constants.ErrorMessages;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.Login;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;

/**
 * Created by J.Bautista
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, ResponseHandler {

    private ImageButton ivBack;
    private ImageButton ivMenu;
    private TextView tvTitle;

    private PopupWindow menu;

    private ActionBar actionBar;

    private int layoutActionBar = R.layout.layout_actionbar;

    private boolean isRetrievingToken;

    private int currentRequest;

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

    /**
     * Sets the PopupWindow to show when the menu button is clicked
     * @param menu PopupWindow view
     */
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

    /**
     * Sets the title of the action bar
     * @param title
     */
    public void setActionBarTitle(String title){

        tvTitle.setText(title);
    }

    /**
     * Sets the background color of the action bar
     * @param resId Color resource id
     */
    public void setActionBarBackgroundColor(int resId){

        int color = getResources().getColor(resId);

        actionBar.setBackgroundDrawable(new ColorDrawable(color));

    }

    /**
     * Sets the layout to be used for the actionbar,. Should be called before super.onCreate(savedInstanceState)
     * @param resId
     */
    public void setActionBarLayout(int resId){

        this.layoutActionBar = resId;
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

        actionBar = getSupportActionBar();

        actionBar.setElevation(0);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(layoutActionBar);

        ivBack = (ImageButton) actionBar.getCustomView().findViewById(R.id.ivBack);
        ivMenu = (ImageButton) actionBar.getCustomView().findViewById(R.id.ivMenu);
        tvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.tvTitle);

        ivBack.setOnClickListener(this);

        if(ivMenu != null)
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

    @Override
    public void onFailed(int requestCode, String message) {

        if(message.equalsIgnoreCase(ErrorMessages.ERR_EXPIRED_TOKEN)){

            if(!isRetrievingToken) {

                currentRequest = requestCode;
                isRetrievingToken = true;
                ApplicationClass.refreshToken(this);
                return;
            }
        }


    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        if(requestCode == ApplicationClass.REQUEST_CODE_REFRESH_TOKEN){

            Login oAuth = (Login) object;

            BaseApplication appClass = ApplicationClass.getInstance();

            appClass.saveAccessToken(oAuth.getAccess_token());
            appClass.saveRefreshToken(oAuth.getRefresh_token());

            handleRefreshToken();

            return;
        }

    }

    public int getCurrentRequest() {
        return currentRequest;
    }

    protected abstract void handleRefreshToken();
}
