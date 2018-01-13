package com.hocrox.digitalviewfinder;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MoveViewTouchListenerHeart
        implements View.OnTouchListener {
    private GestureDetector mGestureDetector;
    private View mView;
    Drawable drawable;
    public static float x, y;
    int height, width, screenWidth, screenHeight;

    public MoveViewTouchListenerHeart(View view, int widths, int heights) {
        mGestureDetector = new GestureDetector(view.getContext(), mGestureListener);
        mView = view;

        screenHeight = heights;
        screenWidth = widths;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        height = mView.getHeight();
        width = mView.getWidth();

        Log.e("ondown", "" + width);


        //Adding comment to take the right gapo inside circle

        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        private float mMotionDownX, mMotionDownY;

        @Override
        public boolean onDown(MotionEvent e) {
            mMotionDownX = e.getRawX() - mView.getTranslationX();
            mMotionDownY = e.getRawY() - mView.getTranslationY();
            Log.e("ondown", "" + mMotionDownX + ">>>>>" + mMotionDownY + ">>" + mView.getTranslationY() + ">>" + mView.getTranslationX());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)

        {
            x = e2.getRawX() - mMotionDownX;
            y = e2.getRawY() - mMotionDownY;

            if (x > 0 && y > 0) {

                if (x + mView.getWidth() < screenWidth && y + mView.getHeight() < screenHeight) {

                    mView.setTranslationX(e2.getRawX() - mMotionDownX);
                    mView.setTranslationY(e2.getRawY() - mMotionDownY);
                }

            }


            //   x = (mView.getTranslationX());
            //  y = (mView.getTranslationY());
            //  x = (e2.getRawX());
            //y = (e2.getRawY());


            //  Log.e("testetrt", "" + viewwidth);

            return true;
        }
    };


}