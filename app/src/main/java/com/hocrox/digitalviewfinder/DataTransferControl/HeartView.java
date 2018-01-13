package com.hocrox.digitalviewfinder.DataTransferControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hocrox.digitalviewfinder.R;

public class HeartView extends View {

    private Rect rectangle;
    private Paint paint;
    Paint[] pDraw = null;
    Bitmap bm = null;
    public static float x = 50;
    public static float y = 50;
    Bitmap bmsss;
    Bitmap result;
    Bitmap mask;
    float degrees;
    int width, height;
    int size = 300;
    boolean check = true;
    boolean heartCheck = false;
    public static float finalx = 50;
    public static float finaly = 50;
    public static float WIDTH = 700;
    public static float HEIGHT = 500;
    private int top;
    private int left;

    public HeartView(Context context, int mViewWidth, int mViewHeight) {
        super(context);
        int x = 2;
        int y = 2;
        int sideLength = 120;
      //  width = 500;
       // height = 700;

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
        if (android.os.Build.VERSION.SDK_INT >= 11) {
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
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        pDraw[0].setAntiAlias(true);

        Path oval = new Path();
        Matrix matrix = new Matrix();
        Region region = new Region();


        RectF ovalRect = new RectF(width / 6,height / 7,
                width - (width / 6), height - (height / 7));

        Log.e("rectangle",""+ovalRect.width()+">>"+ovalRect.height()+">>"+ovalRect.centerX()+">>"+ovalRect.centerY()+">>"+ovalRect.left);

        WIDTH= ovalRect.width()+120;
        HEIGHT=ovalRect.height()+120;
      /*  WIDTH= width - 2*(width / 6);
        HEIGHT=height - 2*(height / 6);
*/
      /*  finalx=width/6;
        finaly=height/7;*/


       /* finalx=width/6;
        finaly=height/7;
*/


        finalx=ovalRect.left-70;
        finaly=ovalRect.top-70;

        oval.addOval(ovalRect, Path.Direction.CW);

        matrix.postRotate(40, width / 2, height / 2);
        oval.transform(matrix, oval);

        region.setPath(oval, new Region((int)width / 2, 0,
                (int)width, (int)height));


        canv.drawPath(region.getBoundaryPath(), pDraw[0]);



        matrix.reset();
        oval.reset();


        oval.addOval(ovalRect, Path.Direction.CW);
        matrix.postRotate(-40, width / 2, height / 2);
        oval.transform(matrix, oval);
        region.setPath(oval,
                new Region(0, 0, (int)width / 2, (int)height));
        //  canv.drawPath(region.getBoundaryPath(), paint);

        canv.drawPath(region.getBoundaryPath(), pDraw[0]);



        Log.e("testing",""+width+">>>"+height+">>>"+ovalRect.width()+">>"+ovalRect.height());


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                Log.e("testing",">>"+x+">>>"+y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                Log.e("testing",">>"+x+">>>"+y);

                invalidate();
                break;
        }
        return true;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("testing",""+w+">>>"+h);

    }
}