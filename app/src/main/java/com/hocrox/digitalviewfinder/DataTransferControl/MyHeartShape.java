package com.hocrox.digitalviewfinder.DataTransferControl;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.View;

public class MyHeartShape extends View {

    private static int WIDTH = 0;
    private static int HEIGHT = 0;

    private Path path;
    private Paint paint;

    private int top;
    private int left;
    int x = 300, y = 300;


    public MyHeartShape(Activity context) {
        super(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        WIDTH = width;
        HEIGHT = height;
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        top = 0;
        left = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fill the canvas with background color
        canvas.drawColor(Color.GRAY);

        paint.setShader(null);

        // Defining of  the heart path starts
 /* path.moveTo((left+WIDTH)/2, top+HEIGHT/4); // Starting point
  // Create a cubic Bezier cubic left path 
  path.cubicTo(left+WIDTH/8,top,
    left+WIDTH/4,top+4*HEIGHT/5,
    left+WIDTH/2, top+HEIGHT);
  // This is right Bezier cubic path
  path.cubicTo(left+3*WIDTH/4,top+4*HEIGHT/5,
    left+4*WIDTH/3,top,
    left+WIDTH/2, top+HEIGHT/4);

*/





 /* WIDTH=2*(x-500)+200;
  HEIGHT=2*(y-500);
*/

        WIDTH = getMeasuredWidth();
        HEIGHT = getMeasuredHeight();
        path.moveTo(left + WIDTH / 2, top + HEIGHT / 8); // Starting point
        path.cubicTo(left,HEIGHT/9,
                WIDTH/6 ,(7*HEIGHT)/8,
                left + WIDTH / 2, top + HEIGHT-40);

        path.moveTo(WIDTH / 2, top + HEIGHT / 5); // Starting point

    /*    path.cubicTo(left+WIDTH, top,
                left + (WIDTH - (WIDTH / 9)), top + 4 * HEIGHT / 5,
                left + WIDTH / 2, top + HEIGHT-40);

*/
        paint.setColor(Color.RED); // Set with heart color
        //paint.setShader(shader);
        paint.setStyle(Style.FILL); // Fill with heart color
        canvas.drawPath(path, paint); // Actual drawing happens here

        // Draw Blue Boundary
  paint.setShader(null);
  paint.setColor(Color.BLUE); // Change the boundary color
  paint.setStrokeWidth(4);
  paint.setStyle(Style.STROKE);
  canvas.drawPath(path, paint);

    }
}