package com.hocrox.digitalviewfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fujiyuu75.sequent.Sequent;
import com.hocrox.digitalviewfinder.DataTransferControl.Events;
import com.hocrox.digitalviewfinder.DataTransferControl.GlobalBus;
import com.hocrox.digitalviewfinder.DataTransferControl.SendEvents;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActiivty extends AppCompatActivity {

    @BindView(R.id.backIamgeview)
    ImageView backIamgeview;
    @BindView(R.id.ivImageView)
    ImageView ivImageView;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.btnClick)
    Button btnClick;
    @BindView(R.id.layoutShapes)
    LinearLayout shapesLayout;
    int viewPortHeight, viewPortWidth;
    Bitmap bitmap;
    Bitmap result;
    @BindView(R.id.ivSmallHeart)
    ImageView ivSmallHeart;
    @BindView(R.id.ivSmallCircle)
    ImageView ivSmallCircle;
    @BindView(R.id.ivSmallOval)
    ImageView ivSmallOval;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.ivLargestIamge)
    ImageView ivLargestIamge;
    @BindView(R.id.ivSecondImage)
    ImageView ivSecondImage;
    @BindView(R.id.ivSmallestImage)
    ImageView ivSmallestImage;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_actiivty);
        ButterKnife.bind(this);
        ivImageView.setOnTouchListener(new MoveViewTouchListenerHeart(ivImageView, getWidth(), (int) ((getHeight() * 2) / 3)));


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

    public int getHeight() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        // width = getWindowManager().getDefaultDisplay().getWidth();

        return height;

    }

    @OnClick({R.id.btnClick, R.id.image, R.id.ivSmallHeart, R.id.ivSmallOval, R.id.ivSmallCircle, R.id.ivMore, R.id.ivLargestIamge, R.id.ivSecondImage, R.id.ivSmallestImage})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnClick:

                viewPortHeight = (int) relativeLayout.getHeight();
                viewPortWidth = (int) relativeLayout.getWidth();
                Log.e("viewport", "" + viewPortWidth + ">>>" + viewPortHeight);
                Log.e("viewport", "" + bitmap.getWidth() + ">>>" + bitmap.getHeight());

                float ratioHeight = bitmap.getHeight() / viewPortHeight;
                float ratioWidth = bitmap.getWidth() / viewPortWidth;

                float mFinalY = ratioHeight * (ivImageView.getY());
                float mFinalX = ratioWidth * (ivImageView.getX());

                Log.e("finalx", "" + mFinalX + ">>>" + mFinalY);

                Bitmap finalBitmap = null;


                finalBitmap = Bitmap.createBitmap(bitmap, (int) (mFinalX), (int) (mFinalY), ivImageView.getWidth(), ivImageView.getHeight());
                //  image.setImageBitmap(finalBitmap);
                cropBitmap(finalBitmap);


                break;
            case R.id.image:
                GlobalBus.getBus().post(new Events(result, "dsfdsaf"));
                startActivity(new Intent(GalleryActiivty.this, LargeImageViewActivity.class));
                finish();
                break;

            case R.id.ivSmallHeart:


                position = 0;
                ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                break;

            case R.id.ivSmallCircle:

                position = 1;
                ivImageView.setBackground(getResources().getDrawable(R.drawable.circle_120));

                break;

            case R.id.ivSmallOval:
                position = 2;
                ivImageView.setBackground(getResources().getDrawable(R.drawable.oval_120));

                break;
            case R.id.ivMore:

                shapesLayout.setVisibility(View.VISIBLE);
                Sequent.origin(shapesLayout).anim(GalleryActiivty.this, R.anim.fadeup).start();

                break;

            case R.id.ivLargestIamge:

                switch (position) {

                    case 0:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;
                    case 1:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;
                    case 2:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;

                }

                break;

            case R.id.ivSecondImage:

                switch (position) {

                    case 0:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;
                    case 1:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;
                    case 2:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;

                }
                break;
            case R.id.ivSmallestImage:

                switch (position) {

                    case 0:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;
                    case 1:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;
                    case 2:

                        ivImageView.setBackground(getResources().getDrawable(R.drawable.heart_180));

                        break;
                }
                break;
        }
    }

    @Produce
    public SendEvents produce() {

        return new SendEvents(result, "sdfd");

    }

    public void cropBitmap(Bitmap bitmap) {

        //Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.nature);
        //   Bitmap original = bitmap;


 /*       //Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth(), bitmap.getHeight(), false);*/

        Bitmap mask = null;
        switch (position) {
            case 0:
                mask = BitmapFactory.decodeResource(getResources(), R.drawable.heart_q80_filled);
                break;
            case 1:
                mask = BitmapFactory.decodeResource(getResources(), R.drawable.filledcircle_120);
                break;
            case 2:
                mask = BitmapFactory.decodeResource(getResources(), R.drawable.filledoval_120);
                break;


        }

        result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(bitmap, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        image.setImageBitmap(result);

        //   ivGalleryImage.setImageBitmap(finalBitmap);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);


    }


    @Subscribe
    public void getBitmap(Events events) {

        Log.e("testig", "" + events.getBitmap() + ">>>" + events.getName());
        bitmap = events.getBitmap();

        Log.e("testig", "" + events.getBitmap() + ">>>" + events.getName());

        //   Bitmap resize = Bitmap.createScaledBitmap(bitmap,500,500, false);
        //  Log.e("bitmap", "" + resize.getHeight() + ">>" + resize.getWidth());

        backIamgeview.setImageBitmap(bitmap);

    }
}
