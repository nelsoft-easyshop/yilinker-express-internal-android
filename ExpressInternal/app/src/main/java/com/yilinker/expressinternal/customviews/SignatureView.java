package com.yilinker.expressinternal.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by J.Bautista
 */
public class SignatureView extends View {

    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private Paint paint = new Paint();
    private Path path = new Path();

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();

    private Context context;
    private Bitmap currentBitmap;


    public SignatureView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);

        this.context = context;
    }

    public void loadImage(String imageFile){

        currentBitmap = BitmapFactory.decodeFile(imageFile);
        invalidate();
    }

    public void save(File file)
    {

        View v= new View(context);

        Log.v("log_tag", "Width: " + v.getWidth());
        Log.v("log_tag", "Height: " + v.getHeight());

        Bitmap mBitmap =  Bitmap.createBitmap (getWidth(), getHeight(), Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(mBitmap);
        try
        {
            FileOutputStream mFileOutStream = new FileOutputStream(file);

            draw(canvas);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();

//            String url = MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, "title", null);
//            Log.v("log_tag","url: " + url);
            //In case you want to delete the file
            //boolean deleted = mypath.delete();
            //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
            //If you want to convert the image to string use base64 converter

        }
        catch(Exception e)
        {
            Log.v("log_tag", e.toString());
        }
    }

    public void clear()
    {
        path.reset();
        currentBitmap = null;

        invalidate();
    }

    public boolean isEmpty(){

        return path.isEmpty();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        if(currentBitmap != null){

            canvas.drawBitmap(currentBitmap, 0, 0, paint);
        }

        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;

            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++)
                {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;

            default:
                return false;
        }

        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }


    private void expandDirtyRect(float historicalX, float historicalY)
    {
        if (historicalX < dirtyRect.left)
        {
            dirtyRect.left = historicalX;
        }
        else if (historicalX > dirtyRect.right)
        {
            dirtyRect.right = historicalX;
        }

        if (historicalY < dirtyRect.top)
        {
            dirtyRect.top = historicalY;
        }
        else if (historicalY > dirtyRect.bottom)
        {
            dirtyRect.bottom = historicalY;
        }
    }

    private void resetDirtyRect(float eventX, float eventY)
    {
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

}
