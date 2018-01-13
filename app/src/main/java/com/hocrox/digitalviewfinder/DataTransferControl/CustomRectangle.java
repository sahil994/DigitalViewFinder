package com.hocrox.digitalviewfinder.DataTransferControl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hocrox.digitalviewfinder.R;

public class CustomRectangle extends View {
    public static float x1, x2, y1, y2, x3, y3;
    public static int dx, dy, width, height;
    private int numColumns = 3, numRows = 3;

    private Bitmap bitTopLeft;
    private Bitmap bitTopRight;
    private Bitmap bitBottomLeft;
    private Bitmap bitBottomRight;
    private Paint rectAnglePaint;
    private Context context;
    private Paint bitmapPaint;
    private Rect rect;
    private int maxX;
    private int maxY,screeny;
    private int centerX;
    private int centerY;
    private Paint canvasPaint;
    private String direction;
    private boolean[][] cellChecked;
    private int cellWidth, cellHeight;
    private boolean gridValue;
    private Paint blackPaint = new Paint();
    public CustomRectangle(Context context, int widths, int heights,boolean gridValue) {
        super(context);
        this.context = context;
        this.gridValue=gridValue;
        Log.e("tesitng", "" + widths + ">>" + heights);
        Init(widths, heights);
    }

    private void Init(int widths, int heights) {
        bitTopLeft = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.circle_12);
        bitTopRight = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.circle_12);
        bitBottomLeft = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.circle_12);
        bitBottomRight = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.circle_12);

        blackPaint.setColor(Color.WHITE);
        rectAnglePaint = new Paint();
        rectAnglePaint.setColor(Color.WHITE);

        rectAnglePaint.setStrokeWidth(2);
        rectAnglePaint.setStyle(Paint.Style.STROKE);

        rectAnglePaint = new Paint();
        rectAnglePaint.setColor(Color.WHITE);

        rectAnglePaint.setStrokeWidth(5);
        rectAnglePaint.setStyle(Paint.Style.STROKE);

        bitmapPaint = new Paint();
        bitmapPaint.setColor(Color.WHITE);
        rect = new Rect();

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
       screeny=display.getHeight();
        Point size = new Point();
        display.getSize(size);
        maxX = size.x;
        maxY = size.y;

        centerX = (maxX / 2);
        centerY = (maxY) / 2 ;

        x3=centerX;
        y3=centerY;

        Log.e("centerx",">>>>"+x3+">>"+y3);

        rect.left = centerX / 2 + bitBottomRight.getWidth() / 2;
        rect.top = centerY / 2 + bitBottomRight.getWidth() / 2;
        rect.right = centerX + centerX / 2 + bitBottomRight.getWidth() / 2;
        rect.bottom = centerY + centerY / 2 + bitBottomRight.getWidth() / 2;



        rect.left = (int) (x3 - (widths / 2));
        rect.top = (int) (y3 - (heights / 2));
        rect.right = (int) (x3 + (widths / 2));
        rect.bottom = (int) (y3 + (heights / 2));

        canvasPaint = new Paint();
        // canvasPaint.setColor(Color.TRANSPARENT);
        canvasPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.parseColor("#7f282727"));
        canvas.drawPaint(canvasPaint);
        width = rect.width();
        height = rect.height();
        Log.e("inside",">>>>"+getMeasuredWidth()+">>"+getMeasuredHeight());

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        // Path p = new Path();
    canvas.drawBitmap(bitTopLeft,
            rect.left - bitBottomRight.getWidth() / 2, rect.top
                    - bitBottomRight.getWidth() / 2, bitmapPaint);

        canvas.drawBitmap(bitTopLeft,
                (rect.left+rect.width()/2) - bitBottomRight.getWidth() / 2, rect.top
                        - bitBottomRight.getWidth() / 2, bitmapPaint);

        canvas.drawBitmap(bitTopLeft,
                (rect.left+rect.width()/2) - bitBottomRight.getWidth() / 2, rect.bottom
                        - bitBottomRight.getWidth() / 2, bitmapPaint);
        canvas.drawBitmap(bitTopLeft,
                (rect.right) - bitBottomRight.getWidth() / 2, (rect.top+rect.height()/2)
                        - bitBottomRight.getWidth() / 2, bitmapPaint);

        canvas.drawBitmap(bitTopLeft,
                (rect.left) - bitBottomRight.getWidth() / 2, (rect.top+rect.height()/2)
                        - bitBottomRight.getWidth() / 2, bitmapPaint);

        canvas.drawBitmap(bitTopRight, rect.right - bitBottomRight.getWidth()
            / 2, rect.top - bitBottomRight.getWidth() / 2, bitmapPaint);
         canvas.drawBitmap(bitBottomLeft, rect.left - bitBottomRight.getWidth()
            / 2, rect.bottom - bitBottomRight.getWidth() / 2, bitmapPaint);
         canvas.drawBitmap(bitBottomRight,
            rect.right - bitBottomRight.getWidth() / 2, rect.bottom
                    - bitBottomRight.getWidth() / 2, bitmapPaint);






Log.e("Tesitnf rextff",""+rect.top+">>>"+rect.bottom+">>>"+canvas.getHeight()+">>"+maxY);

 if(rect.top>0&&rect.left>0&&rect.right<canvas.getWidth()){

     canvas.drawRect(rect, rectAnglePaint);

     if(gridValue){
         if (numColumns < 1 || numRows < 1) {
             return;
         }

         cellWidth = width / numColumns;
         cellHeight = (height) / numRows;

         cellChecked = new boolean[numColumns][numRows];


         for (int i = 1; i < numColumns; i++) {
             canvas.drawLine((i * cellWidth) + (x3-width/2),(y3-height/2), i * cellWidth + (x3-width/2), (y3-height/2) + height, blackPaint);
         }

         for (int i = 1; i < numRows; i++) {
             canvas.drawLine((x3-width/2), (i * cellHeight) + (y3-height/2),(x3-width/2) + width, (i * cellHeight) + (y3-height/2), blackPaint);
         }
     }
 }

else{

     Toast.makeText(context,"UnRechable Area",Toast.LENGTH_SHORT).show();
 }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getRawX();
                y1 = event.getRawY();
             /*   x3 = event.getX();
                y3 = event.getY();
              */  invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = event.getRawX();
                y2 = event.getRawY();

               /* x3 = event.getX();
                y3 = event.getY();
              */  int currentx = (int) event.getX();
                int currenty = (int) event.getY();

                Calculatingpoint(currentx, currenty);
                width = rect.width();
                height = rect.height();

                Log.e("custom", "" + rect.width() + ">>>>" + rect.height());


                break;

            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return true;
    }

    private void Calculatingpoint(int x, int y) {
        if (x2 + dx < maxX && y2 + dy < maxY) {


            // dx = rect.right - (int)x2 ;
            // dy = (int)y2 - rect.top ;
            //
            // Log.e("dx", ""+dx);
            // Log.e("dy", ""+dy);

            boolean isLeft = false;
            boolean isTop = false;

            if (x2 > maxX / 2) {
                direction = "right";
                isLeft = false;
            } else {
                direction = "left";
                isLeft = true;
            }

            if (y2 > maxY / 2) {
                direction = "bottom";
                isTop = false;
            } else {
                direction = "top";
                isTop = true;
            }

            if (!isLeft && !isTop) {
                dx = rect.right - (int) x2;

                dy = rect.bottom - (int) y2;
            }

            if (isLeft && !isTop) {
                dx = (int) x2 - rect.left;
                dy = rect.bottom - (int) y2;
            }

            if (isLeft && isTop) {
                dx = (int) x2 - rect.left;
                dy = (int) y2 - rect.top;
            }

            if (!isLeft && isTop) {
                dx = rect.right - (int) x2;
                dy = (int) y2 - rect.top;
            }


// this will perfect 
            rect.inset(dx, dy);
            Log.e("customdsadssdads", "" + dx + ">>>>" + dy);

            invalidate();

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChange", "" + w + ">>>>" + h + ">>" + oldw + ">>" + oldh);

    }
}