package com.yilinker.expressinternal.controllers.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.model.JobOrder;

/**
 * Created by J.Bautista
 */
public class ActivityQRCode extends BaseActivity {

    public static final String ARG_JOB_ORDER = "jobOrder";

    private ImageView ivQRCode;
    private TextView tvWaybillNo;

    private JobOrder jobOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        getData();

        initViews();

        //        generateQRCode();

        //For generating QR code image in the background
        TaskGenerateQR task = new TaskGenerateQR();
        task.execute();

    }

    @Override
    protected void handleRefreshToken() {

    }


    private void getData(){

        jobOrder = getIntent().getParcelableExtra(ARG_JOB_ORDER);

    }

    private void initViews(){

        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
        tvWaybillNo = (TextView) findViewById(R.id.tvWaybillNo);

        //For Action Bar
        setActionBarTitle(getString(R.string.actionbar_title_qrcode));
        setActionBarBackgroundColor(R.color.marigold);

    }

//    private void generateQRCode(){
//
//        QRCodeWriter writer = new QRCodeWriter();
//        int dimension = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
//
//        try {
//
//            BitMatrix matrix = writer.encode(
//                    jobOrder.getWaybillNo(), BarcodeFormat.QR_CODE, dimension, dimension
//            );
//
//            Bitmap bitmap = toBitmap(matrix);
//
//            ivQRCode.setImageBitmap(bitmap);
//
//            tvWaybillNo.setText(jobOrder.getWaybillNo());
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//    }

    private Bitmap generateQRCode(){

        QRCodeWriter writer = new QRCodeWriter();
        int dimension = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
        Bitmap bitmap = null;

        try {

            BitMatrix matrix = writer.encode(
                    jobOrder.getWaybillNo(), BarcodeFormat.QR_CODE, dimension, dimension
            );

            bitmap = toBitmap(matrix);

//            ivQRCode.setImageBitmap(bitmap);
//
//            tvWaybillNo.setText(jobOrder.getWaybillNo());

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    private Bitmap toBitmap(BitMatrix matrix){

        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private class TaskGenerateQR extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            tvWaybillNo.setText(getString(R.string.generating_qr));
        }

        @Override
        protected Bitmap doInBackground(Void... params) {


            Bitmap bitmap = generateQRCode();

            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(bitmap != null) {
                ivQRCode.setImageBitmap(bitmap);
            }

            tvWaybillNo.setText(jobOrder.getWaybillNo());

        }
    }
}
