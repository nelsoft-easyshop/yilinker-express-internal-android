package com.yilinker.expressinternal.controllers.signature;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.customviews.SignatureView;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;

import java.io.File;
import java.io.IOException;

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
    private TextView tvClear;

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
        tvClear = (TextView) findViewById(R.id.tvClear);


        btnRating.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvClear.setOnClickListener(this);
    }

    private void showRating(){

        FragmentDialogRating dialog = FragmentDialogRating.createInstance(REQUEST_CODE_RATING, rating);
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

                if(signatureView.isEmpty()) {
                    onBackPressed();
                }
                else{
                    showWarning();
                }

                break;

            case R.id.tvClear:

                signatureView.clear();
                break;

        }

    }

    @Override
    public void onDialogDismiss(int requestCode, Bundle bundle) {

        rating = (int) bundle.getFloat(FragmentDialogRating.ARG_RATING);

        String text = null;
        if(rating == 0){
            text = getString(R.string.signature_rating);
        }
        else{
            text = getString(R.string.signature_rating_added);
        }

        btnRating.setText(text);
    }

    private void saveSignature(){

        //Check for empty signature
        if(signatureView.isEmpty()){

            Toast.makeText(getApplicationContext(), getString(R.string.signature_error_empty), Toast.LENGTH_LONG).show();
            return;
        }

        //Check for rating
        if(rating <= 0){

            Toast.makeText(getApplicationContext(), getString(R.string.signature_error_no_rating), Toast.LENGTH_LONG).show();
            return;
        }


        File outputDir = getExternalCacheDir();
        File outputFile = null;
        try {
            outputFile = File.createTempFile("signature", ".jpg", outputDir);

//            Uri photoUri = Uri.fromFile(outputFile);

            signatureView.save(outputFile);

            Intent intent = new Intent();
            intent.putExtra(ARG_IMAGE_FILE, outputFile.getAbsolutePath());
            intent.putExtra(ARG_RATING, rating);
            setResult(RESULT_OK, intent);
            finish();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showWarning(){

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySignature.this);
        builder.setMessage(getString(R.string.signature_warning));

        builder.setPositiveButton(getString(R.string.signature_submit),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        saveSignature();
                    }
                });

        builder.setNegativeButton(getString(R.string.signature_exit),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                        finish();
                    }
                });

        builder.show();

    }
}
