package com.hocrox.digitalviewfinder.DataTransferControl;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hocrox.digitalviewfinder.R;
import com.hocrox.digitalviewfinder.scaling.MultiTouchListener;

import static com.hocrox.digitalviewfinder.R.id.imageView;

public class DemoActivity extends AppCompatActivity implements View.OnTouchListener {
    private Matrix matrix = new Matrix();

    private Matrix savedMatrix = new Matrix();


    private static final int NONE = 0;

    private static final int DRAG = 1;

    private static final int ZOOM = 2;

    private int mode = NONE;

    private PointF start = new PointF();

    private PointF mid = new PointF();

    private float oldDist = 1f;

    private float d = 0f;

    private float newRot = 0f;

    private float[] lastEvent = null;
    ImageView view;
    float height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        view = (ImageView) findViewById(imageView);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("testing", "" + view.getHeight() + ">>>" + view.getWidth() + ">>>" + view.getX() + ">>>" + view.getY());

          /*      Drawable drawable = view.getDrawable();
                Rect imageBounds = drawable.getBounds();
                float[] values = new float[9];

                view.getImageMatrix().getValues(values);*/


            }

        });
        view.setOnTouchListener(new MultiTouchListener(view, getWidth(), 300));


    }

    public int getWidth() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        // width = getWindowManager().getDefaultDisplay().getWidth();

        return width;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        height = view.getHeight();
        width = view.getWidth();

        Log.e("view", "" + height + ">>>" + width);
    }

    public boolean onTouch(View v, MotionEvent event) {


        ImageView view = (ImageView) v;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                savedMatrix.set(matrix);

                start.set(event.getX(), event.getY());

                mode = DRAG;

                lastEvent = null;

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                oldDist = spacing(event);


                if (oldDist > 10f) {

                    savedMatrix.set(matrix);

                    midPoint(mid, event);

                    mode = ZOOM;

                }

                lastEvent = new float[4];

                lastEvent[0] = event.getX(0);

                lastEvent[1] = event.getX(1);

                lastEvent[2] = event.getY(0);

                lastEvent[3] = event.getY(1);

                d = rotation(event);

                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_POINTER_UP:

                mode = NONE;

                lastEvent = null;

                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {

                    matrix.set(savedMatrix);

                    float dx = event.getX() - start.x;

                    float dy = event.getY() - start.y;

                    matrix.postTranslate(dx, dy);

                } else if (mode == ZOOM) {

                    float newDist = spacing(event);

                    if (newDist > 10f) {


                        matrix.set(savedMatrix);

                        float scale = (newDist / oldDist);

                        matrix.postScale(scale, scale, mid.x, mid.y);

                    }

                    if (lastEvent != null && event.getPointerCount() == 3) {

                        newRot = rotation(event);

                        float r = newRot - d;

                        float[] values = new float[9];

                        matrix.getValues(values);

                        float tx = values[2];

                        float ty = values[5];

                        float sx = values[0];

                        float xc = (view.getWidth() / 2) * sx;

                        float yc = (view.getHeight() / 2) * sx;

                        matrix.postRotate(r, tx + xc, ty + yc);

                    }

                }

                break;

        }


        view.setImageMatrix(matrix);

        return true;

    }

    private float spacing(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);

        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);

    }


    private void midPoint(PointF point, MotionEvent event) {

        float x = event.getX(0) + event.getX(1);

        float y = event.getY(0) + event.getY(1);

        point.set(x / 2, y / 2);

    }


    private float rotation(MotionEvent event) {

        double delta_x = (event.getX(0) - event.getX(1));

        double delta_y = (event.getY(0) - event.getY(1));

        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);


    }
}
