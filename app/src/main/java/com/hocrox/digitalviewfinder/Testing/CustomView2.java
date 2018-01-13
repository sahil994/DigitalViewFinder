package com.hocrox.digitalviewfinder.Testing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.hocrox.digitalviewfinder.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class CustomView2 extends View {

    private Rect rectangle;
    private Paint paint;
    Paint pDraw = null;
    Bitmap bm = null;
    float x = 200;
    float y = 200;
    Bitmap bmsss;
    Bitmap result;
    Bitmap mask;
    Path path;
    private float mScaleFactor;
    List<Path> listPath;
    private int mActivePointerId;
    private float mLastTouchY;
    private float mLastTouchX;
    private float mPosY, mPosX;
    private ClipDrawable mIcon;
    private ScaleGestureDetector mScaleDetector;

    public CustomView2(Context context) {
        super(context);
        int x = 2;
        int y = 2;
        int sideLength = 200;
        listPath = new ArrayList<>();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

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

        pDraw = new Paint();
        pDraw.setARGB(255, 255, 255, 0);
        pDraw.setStrokeWidth(20);
        pDraw.setStyle(Paint.Style.FILL);

        // Set all transfer modes
        pDraw.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));


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

    public CustomView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();
        pDraw.setARGB(255, 255, 255, 0);
        pDraw.setStrokeWidth(20);
        pDraw.setStyle(Paint.Style.FILL);

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        //canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor, super.getWidth() * 0.5f,
                super.getHeight() * 0.5f);
        //mIcon.draw(canvas);
        for (Path path : listPath) {
            canvas.drawPath(path, paint);
        }
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.

        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        float objectNewX, objectNewY;
        if (mScaleFactor >= 1) {
            objectNewX = ev.getX() + (ev.getX() - super.getWidth() * 0.5f) * (mScaleFactor - 1);
            objectNewY = ev.getY() + (ev.getY() - super.getHeight() * 0.5f) * (mScaleFactor - 1);
        } else {
            objectNewX = ev.getX() - (ev.getX() - super.getWidth() * 0.5f) * (1 - mScaleFactor);
            objectNewY = ev.getY() - (ev.getY() - super.getHeight() * 0.5f) * (1 - mScaleFactor);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            path = new Path();
            path.moveTo(objectNewX, objectNewY);
            path.lineTo(objectNewX, objectNewY);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            path.lineTo(objectNewX, objectNewY);
            listPath.add(path);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            path.lineTo(objectNewX, objectNewY);
            listPath.add(path);
        }

        return true;
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }
}