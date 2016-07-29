package com.yilinker.expressinternal.controllers.signature;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.customviews.SignatureView;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by J.Bautista
 */
public class ActivitySignature extends Activity implements  View.OnClickListener, DialogDismissListener, RecyclerViewClickListener{

    public static final String ARG_IMAGE_FILE = "filePath";
    public static final String ARG_RATING = "rating";
    public static final String ARG_RECEIVED_BY = "received_by";
    public static final String ARG_RECIPIENT = "recipient";
    public static final String ARG_RELATIONSHIP = "relationship";

    private static final String TAG_RATING = "rating";

    private static final int REQUEST_CODE_RATING = 1001;

    private Button btnRating;
    private Button btnSubmit;
    private ImageButton btnBack;
    private SignatureView signatureView;
    private TextView tvClear, tvReceivedBy;
    private PopupWindow popupWindow;
    private EditText etReceivedBy, etRelationship;

    private int rating;

    private boolean hasImage;
    private String imageFile;
    private String recipient;
    private String relationship;

    private boolean isDropDownVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        initViews();
        loadReceivedBy();
        loadSignatureImage();
    }

    private void initViews(){

        btnRating = (Button) findViewById(R.id.btnRating);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        signatureView = (SignatureView) findViewById(R.id.viewSignature);
        tvClear = (TextView) findViewById(R.id.tvClear);
        tvReceivedBy = (TextView) findViewById(R.id.tvReceivedBy);
        etReceivedBy = (EditText) findViewById(R.id.etReceivedBy);
        etRelationship = (EditText) findViewById(R.id.etRelationShip);

        btnRating.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvReceivedBy.setOnClickListener(this);

        signatureView.setBackgroundResource(R.color.white);
        signatureView.setDrawingCacheEnabled(true);

        btnRating.setVisibility(View.GONE);
    }

    private void loadReceivedBy(){
        Intent data = getIntent();
        String receivedBy = data.getStringExtra(ARG_RECEIVED_BY);
        recipient = data.getStringExtra(ARG_RECIPIENT);
        relationship = data.getStringExtra(ARG_RELATIONSHIP);

        if (receivedBy != null){
            etReceivedBy.setText(receivedBy);
            if (receivedBy.equals(recipient)){
                tvReceivedBy.setText(getString(R.string.signature_consignee));
                toggleReceiverFields(false);
            }else {
                tvReceivedBy.setText(getString(R.string.signature_others));
                toggleReceiverFields(true);
                etReceivedBy.setText(receivedBy);

                if (relationship != null){
                    etRelationship.setText(relationship);
                }
            }
        }
    }

    private void toggleReceiverFields(boolean isEnable){
        etReceivedBy.setEnabled(isEnable);
        etReceivedBy.setBackgroundResource(isEnable? R.drawable.bg_signature_edittext
                : R.drawable.bg_signature_edittext_disabled);
        etReceivedBy.setText(isEnable? "": recipient);

        etRelationship.setVisibility(isEnable? View.VISIBLE:View.GONE);

    }

    private void showPopUpWindow(){
        List<String> receivers = Arrays.asList(getResources().getStringArray(R.array.received_by_array));
        View customView = getLayoutInflater().inflate(R.layout.layout_popup_window, null);

        RecyclerView rvStatus = (RecyclerView) customView.findViewById(R.id.rvItems);
        rvStatus.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        AdapterReceivedBy adapter = new AdapterReceivedBy(receivers,this);
        rvStatus.setAdapter(adapter);
        rvStatus.setHasFixedSize(true);

        popupWindow = new PopupWindow(customView, tvReceivedBy.getWidth(), RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(tvReceivedBy.getWidth());
        popupWindow.showAsDropDown(tvReceivedBy);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        isDropDownVisible = true;
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

            case R.id.tvReceivedBy:
                if (isDropDownVisible){
                    dismissPopUpWindow();

                }else {
                    showPopUpWindow();
                }
            break;
        }

    }

    private void dismissPopUpWindow(){

        if (popupWindow!=null){
            popupWindow.dismiss();
            isDropDownVisible = false;
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

        if (etReceivedBy.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.signature_error_received_by), Toast.LENGTH_LONG).show();
            return;

        }

        if (tvReceivedBy.getText().toString().equals(getString(R.string.signature_others))){

            if (etRelationship.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), getString(R.string.signature_error_relationship),
                        Toast.LENGTH_LONG).show();
                return;
            }
            relationship = etRelationship.getText().toString();

        }else {
            relationship = getString(R.string.signature_consignee);
        }

//        //Check for rating
//        if(rating <= 0){
//
//            Toast.makeText(getApplicationContext(), getString(R.string.signature_error_no_rating), Toast.LENGTH_LONG).show();
//            return;
//        }

        /***clear inputs*/
        relationship = relationship.replace("/","");
        String receivedBy = etReceivedBy.getText().toString().replace("/","");

        File outputDir = getExternalCacheDir();
        File outputFile = null;
        try {
            outputFile = File.createTempFile("signature", ".jpg", outputDir);

//            Uri photoUri = Uri.fromFile(outputFile);

            signatureView.save(outputFile);

            Intent intent = new Intent();
            intent.putExtra(ARG_IMAGE_FILE, outputFile.getAbsolutePath());
            intent.putExtra(ARG_RATING, rating);
            intent.putExtra(ARG_RECEIVED_BY, receivedBy);
            intent.putExtra(ARG_RELATIONSHIP, relationship);
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

    private void loadSignatureImage(){

        //Check if there is a signature image
        Intent intent = getIntent();
        hasImage = intent.hasExtra(ARG_IMAGE_FILE);

        if(hasImage){

            imageFile = intent.getStringExtra(ARG_IMAGE_FILE);

            if(imageFile != null) {

                //Load the image to the canvas
                signatureView.loadImage(imageFile);
            }
        }

    }

    @Override
    public void onItemClick(int position, Object object) {
        dismissPopUpWindow();
        String receivedBy = (String) object;
        tvReceivedBy.setText(receivedBy);
        toggleReceiverFields(!receivedBy.equals(getString(R.string.signature_consignee)));

    }
}
