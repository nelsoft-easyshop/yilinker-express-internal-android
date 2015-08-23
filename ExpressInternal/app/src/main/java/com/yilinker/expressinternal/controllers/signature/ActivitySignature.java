package com.yilinker.expressinternal.controllers.signature;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.customviews.SignatureView;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;

/**
 * Created by J.Bautista
 */
public class ActivitySignature extends Activity implements  View.OnClickListener, DialogDismissListener{

    public static final String ARG_IMAGE_FILE = "filePath";
    public static final String ARG_RATING = "rating";

    private static final String TAG_RATING = "rating";

    private static final int REQUEST_CODE_RATING = 1001;

    private Button btnRating;
    private Button btnSubmit;
    private ImageButton btnBack;
    private SignatureView signatureView;

    private int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        initViews();
    }

    private void initViews(){

        btnRating = (Button) findViewById(R.id.btnRating);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        signatureView = (SignatureView) findViewById(R.id.viewSignature);


        btnRating.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void showRating(){

        FragmentDialogRating dialog = FragmentDialogRating.createInstance(REQUEST_CODE_RATING);
        dialog.show(getFragmentManager(), TAG_RATING);
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnSubmit:

                saveSignature();
                break;

            case R.id.btnRating:

                showRating();
                break;

            case R.id.btnBack:

                //TODO Show alert dialog when there's a signature
                onBackPressed();
                break;

        }

    }

    @Override
    public void onDialogDismiss(int requestCode, Bundle bundle) {

        rating = (int) bundle.getFloat(FragmentDialogRating.ARG_RATING);

    }

    private void saveSignature(){

        //Check for empty signature
        if(signatureView.isEmpty()){

            //TODO Show error message
            return;
        }

        //Check for rating
        if(rating <= 0){

            //TODO Show error message
            return;
        }


        //TODO Save signature temporarily
//        signatureView.save(n);

        Intent intent = new Intent();
        intent.putExtra(ARG_IMAGE_FILE, "");
        intent.putExtra(ARG_RATING, rating);
        setResult(RESULT_OK, intent);
        finish();
    }
}
