package com.hocrox.digitalviewfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView, smallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageView = (ImageView) this.findViewById(R.id.ivImageView);


        smallView = (ImageView) findViewById(R.id.ivSmallView);
        Button photoButton = (Button) this.findViewById(R.id.btnClick);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            //    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
              //  startActivityForResult(cameraIntent, CAMERA_REQUEST);

/*
                CameraPreview CameraPreview = new CameraPreview(MainActivity.this);
                //surfaceView = new MySurfaceView(this, null , mCamera ,width , height);

                ((FrameLayout) findViewById(R.id.preview)).addView(CameraPreview);*/


            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(getCroppedBitmap(photo));

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    photo, 50, 50, false);

            smallView.setImageBitmap(resizedBitmap);
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, photo.getWidth(), photo.getHeight()), new RectF(0, 0, 30, 30), Matrix.ScaleToFit.CENTER);
            Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), m, true);

        }
    }
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }




 /*   public void cropImage(){


        ImageView mImageView = (ImageView) findViewById(R.id.myImageView);
        Bitmap original = BitmapFactory.decodeResource(getResources(),
                R.drawable.random_drawable);
        Bitmap mask = BitmapFactory.decodeResource(getResources(),
                R.drawable.mask_drawable);
        original = Bitmap.createScaledBitmap(original, mask.getWidth(),
                mask.getHeight(), true);

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        mImageView.setImageBitmap(result);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageView.setBackgroundResource(R.drawable.background_drawable);

    }*/






}
