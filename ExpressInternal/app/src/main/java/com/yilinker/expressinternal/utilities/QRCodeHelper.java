package com.yilinker.expressinternal.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yilinker.expressinternal.R;

/**
 * Created by J.Bautista
 */
public class QRCodeHelper {

    public static Bitmap generateQRCode(Context context, String text, int sizeResId){

        QRCodeWriter writer = new QRCodeWriter();
        int dimension = context.getResources().getDimensionPixelSize(sizeResId);

        try {

            BitMatrix matrix = writer.encode(
                    text, BarcodeFormat.QR_CODE, dimension, dimension
            );

            Bitmap bitmap = toBitmap(matrix);

            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap generateBarode(Context context, String text, int heightResId, int widthResId){

        MultiFormatWriter writer = new MultiFormatWriter();
        int height = context.getResources().getDimensionPixelSize(heightResId);
        int width = context.getResources().getDimensionPixelSize(widthResId);

        try {

            BitMatrix matrix = writer.encode(
                    text, BarcodeFormat.CODE_39, width, height
            );

            Bitmap bitmap = toBitmap(matrix);

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Bitmap toBitmap(BitMatrix matrix){

        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.TRANSPARENT);
            }
        }
        return bmp;
    }


}
