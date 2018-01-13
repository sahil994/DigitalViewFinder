package com.hocrox.digitalviewfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hocrox.digitalviewfinder.DataTransferControl.Events;
import com.hocrox.digitalviewfinder.DataTransferControl.GlobalBus;
import com.squareup.otto.Produce;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.ivback)
    ImageView ivback;
    @BindView(R.id.cameralayout)
    RelativeLayout cameralayout;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;

    @BindView(R.id.linearlayout)
    RelativeLayout linearlayout;
    @BindView(R.id.clickImage)
    FloatingActionButton clickImage;
    @BindView(R.id.shimmer_text)
    ShimmerLayout shimmerText;
    @BindView(R.id.coordinatorview)
    CoordinatorLayout coordinatorview;
    @BindView(R.id.bottomLayout)
    RelativeLayout bottomLayout;
    private CameraPreview mPreview;
    private int mCameraId;
    Camera mCamera;
    private CameraPreview.LayoutMode mLayoutMode;
    String mViewWidth, mViewHeight;
    Bitmap imageBitmap;

    ImageView mGalleryIamge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_camerapreview);
        ButterKnife.bind(this);
        mGalleryIamge = (ImageView) findViewById(R.id.ivGalleryImage);
        mGalleryIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, GalleryActiivty.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);


            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        cameralayout.removeView(mPreview); // This is necessary.
        mPreview = null;
        mCamera.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CameraPreview(TestActivity.this, 0, CameraPreview.LayoutMode.FitToParent);

    }

    public void CameraPreview(Activity activity, int cameraId, CameraPreview.LayoutMode mode) {

        mLayoutMode = mode;

        //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Camera.getNumberOfCameras() > cameraId) {
                mCameraId = cameraId;
            } else {
                mCameraId = 0;
            }
        } else {
            mCameraId = 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(mCameraId);
        } else {
            mCamera = Camera.open();
        }
        mPreview = new CameraPreview(this, 0, CameraPreview.LayoutMode.FitToParent, mCamera);
        RelativeLayout.LayoutParams previewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        previewLayoutParams.height = getHeight();
        previewLayoutParams.width = getWidth();

        Log.e("testing", "" + getHeight() + ">>>" + getWidth());

        cameralayout.addView(mPreview, 0, previewLayoutParams);

        //   cameralayout.setOnTouchListener(new MoveViewTouchListener(cameralayout));


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

    @Produce
    public Events events() {

        return new Events(imageBitmap, "FixedCameraPreview");

    }


    @OnClick({R.id.clickImage, R.id.ivGalleryImage, R.id.ivback})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.clickImage:


                captureimage();

                break;
            case R.id.ivGalleryImage:

                startActivity(new Intent(TestActivity.this, GalleryActiivty.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.ivback:

                Log.e("bixcdscdscdsfc", "fdasc");

                finish();
                break;
        }

    }


    public void captureimage() {

        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                Log.e("bitmap", "" + bitmap.getHeight() + ">>" + bitmap.getWidth());

                imageBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                float height = (getHeight()) / 2;

                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, getWidth(), (int) height, false);


                //   Bitmap bitmap1=Bitmap.createBitmap(resizedBitmap,0,0,300,300,null,false);


                Log.e("bitmap", "" + imageBitmap.getHeight() + ">>" + imageBitmap.getWidth());
                //imageBitmap = bitmap;
                GlobalBus.getBus().post(events());

                mGalleryIamge.setImageBitmap(imageBitmap);


            }
        });


    }

}
