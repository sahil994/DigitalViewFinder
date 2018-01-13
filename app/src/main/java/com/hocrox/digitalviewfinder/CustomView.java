package com.hocrox.digitalviewfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

    private Rect rectangle;
    private Paint paint;
    Paint[] pDraw = null;
    Bitmap bm = null;
    public static float x = 200;
    public static float y = 200;
    Bitmap bmsss;
    Bitmap result;
    Bitmap mask;
    int width, height;


    public CustomView(Context context, int mViewWidth, int mViewHeight) {
        super(context);
        int x = 2;
        int y = 2;
        int sideLength = 200;
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


     /*//   pDraw[1].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        pDraw[2].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));
        pDraw[3].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        pDraw[4].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        pDraw[5].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        pDraw[6].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        pDraw[7].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        pDraw[8].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        pDraw[9].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        pDraw[10].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        pDraw[11].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        pDraw[12].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        pDraw[13].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        pDraw[14].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        pDraw[15].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));*/


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canv) {


        canv.drawColor(Color.parseColor("#7f282727"));


        // Draw the bitmap leaving small outside border to see background
        //      canv.drawBitmap(bm, null, new RectF(15, 15, getMeasuredWidth() - 15, getMeasuredHeight() - 15), null);

        //  canv.drawRect(0,0,200,200,null);

        float w, h;
        w = getMeasuredWidth();
        h = getMeasuredHeight();

        //canv.drawBitmap(result,x,y,new Paint());

        //    result.recycle();

        //   canv.drawBitmap(result, x, y, x+200,y+200,new Paint());
        // Loop, draws 4 circles per row, in 4 rows on top of bitmap
        // Drawn in the order of pDraw (alphabetical)

//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.heart_180);

        //canv.drawCircle(x, y, 100, pDraw[0]);

        //   canv.drawBitmap(result, x, y, new Paint());


        /*Point firstTri = new Point(50, 100);
        Point secondTri = new Point(150, 100);

        Point thirdTri = new Point(100, 50);


        Path path = new Path();
        //path.setFillType(FillType.EVEN_ODD);
        path.lineTo(firstTri.x, firstTri.y);
        path.lineTo(secondTri.x, secondTri.y);
        path.lineTo(thirdTri.x, thirdTri.y);
        path.close();*/

        Point point1_draw = new Point(240,140);
        Point point2_draw = new Point(50,280);
        Point point3_draw = new Point(300,500);

        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point1_draw.x, point1_draw.y);
        path.close();
        canv.drawARGB(0, 0, 0, 0);
        pDraw[0].setColor(Color.parseColor("#BAB399"));
        canv.drawPath(path, pDraw[0]);



        final RectF oval = new RectF();
        oval.set(50, 100, 250, 300);


        final RectF oval1 = new RectF();
        oval1.set(222, 100, 422, 300);



        /*  Path path = new Path();
         path.moveTo(200,100);
        path.close();
      */


        //



        x=x-(width/2);
        y=y-(height/2);
        canv.drawRect(x,y, x + width, y + height, pDraw[0]);



/*

        canv.drawArc(oval, -30, -180, true, pDraw[0]);
        canv.drawArc(oval1, 30, -180, true, pDraw[0]);
*/


        //   canv.drawArc(oval,-45, -180, false, pDraw[0]);


        // canv.drawArc(200,200,150,70,0,45,true,pDraw[0]);





        /*for (int i = 0; i < 4; i++) {
            for (int ii = 0; ii < 4; ii++) {
                canv.drawCircle((w / 8) * (ii * 2 + 1), (h / 8) * (i * 2 + 1), w / 8 * 0.8f, pDraw[i * 4 + ii]);
            }
        }*/
    }

    //    canvas.drawRect(rectangle, paint);


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