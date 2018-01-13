package com.hocrox.digitalviewfinder.DataTransferControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;

import com.hocrox.digitalviewfinder.R;

public class OvalView extends View {

    private Rect rectangle;
    private Paint paint;
    Paint[] pDraw = null;
    Bitmap bm = null;
    public static float x = 50;
    public static float y = 50;
    public static float finalx = 50;
    public static float finaly = 50;

    Bitmap bmsss;
    Bitmap result;
    Bitmap mask;
    float degrees;
    int width = 200, height = 200;
    int size = 200;
    boolean check = true;
    boolean heartCheck = false;

    public static  float WIDTH, HEIGHT;

    public OvalView(Context context, int mViewWidth, int mViewHeight) {
        super(context);
        int x = 2;
        int y = 2;
        int sideLength = 120;
        width = mViewWidth;
        height = mViewHeight;
        bmsss = BitmapFactory.decodeResource(getResources(), R.drawable.jellyfish);


        mask = BitmapFactory.decodeResource(getResources(), R.drawable.heart_q80_filled);
        result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bmsss, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        // image.setImageBitmap(result);


        // create a rectangle that we'll draw later
        rectangle = new Rect(x, y, sideLength, sideLength);

        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        // Generate array of paints
        pDraw = new Paint[16];

        for (int i = 0; i < pDraw.length; i++) {
            pDraw[i] = new Paint();
            pDraw[i].setARGB(255, 255, 255, 0);
            pDraw[i].setStrokeWidth(20);
            pDraw[i].setStyle(Paint.Style.FILL);
        }

        // Set all transfer modes
        pDraw[0].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canv) {


        canv.drawColor(Color.parseColor("#212121"));


        float w, h;
        width = getMeasuredWidth()-100;
        height = getMeasuredHeight()-200;


        x = getMeasuredWidth() / 2 - (width) / 2;
        y = getMeasuredHeight() / 2 - (height) / 2;


        finalx = x;
        finaly = y;

        WIDTH = width;
        HEIGHT = height;

        RectF rect = new RectF(x, y, x + width, y + height);
        canv.drawOval(rect, pDraw[0]);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                invalidate();
                break;
        }
        return true;

    }


}