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

public class FixedCamerapreviewActivity extends AppCompatActivity implements View.OnClickListener {

    //  RelativeLayout mCameraLayout;

    @BindView(R.id.ivback)
    ImageView ivback;
    @BindView(R.id.cameralayout)
    RelativeLayout cameralayout;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.ivGalleryImage)
    ImageView ivGalleryImage;
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
    @BindView(R.id.gridlayout)
    RelativeLayout gridlayout;
    private CameraPreview mPreview;
    private int mCameraId;
    Camera mCamera;
    private CameraPreview.LayoutMode mLayoutMode;
    String mViewWidth, mViewHeight;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_camerapreview);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mViewWidth = intent.getStringExtra("width");
        mViewHeight = intent.getStringExtra("height");

       // relativeLayout.bringToFront();

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
        CameraPreview(FixedCamerapreviewActivity.this, 0, CameraPreview.LayoutMode.FitToParent);

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


        previewLayoutParams.height = Integer.parseInt(mViewHeight);
        previewLayoutParams.width = Integer.parseInt(mViewWidth);

        cameralayout.setGravity(RelativeLayout.CENTER_HORIZONTAL);
        cameralayout.addView(mPreview, 0, previewLayoutParams);

        cameralayout.setOnTouchListener(new MoveViewTouchListener(cameralayout, getWidth(), cameralayout.getHeight()));

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

                startActivity(new Intent(FixedCamerapreviewActivity.this, LargeImageViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;

            case R.id.ivback:
                finish();
                break;


        }

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

    public void captureimage() {

        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                imageBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                ivGalleryImage.setImageBitmap(imageBitmap);


            }
        });


    }
}
