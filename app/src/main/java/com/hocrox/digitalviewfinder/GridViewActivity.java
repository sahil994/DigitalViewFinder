package com.hocrox.digitalviewfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hocrox.digitalviewfinder.DataTransferControl.MyEventModel;
import com.hocrox.digitalviewfinder.scaling.PixelGridView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GridViewActivity extends AppCompatActivity {

    @BindView(R.id.ivback)
    ImageView ivback;
    /*  @BindView(R.id.cameralayout)
      RelativeLayout cameralayout;
    */
    @BindView(R.id.viewLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.ivGalleryImage)
    ImageView ivGalleryImage;
    /*    @BindView(R.id.linearlayout)
        RelativeLayout linearlayout;*/
    @BindView(R.id.clickImage)
    FloatingActionButton clickImage;

    @BindView(R.id.ivReplay)
    ImageView ivReplay;

  /*  @BindView(R.id.bottomLayout)
    RelativeLayout bottomLayout;
*/
/*
    @BindView(R.id.gridLayout)
    RelativeLayout gridlayout;

*/

    Camera mCamera;
    private CameraPreview.LayoutMode mLayoutMode;
    String mViewWidth, mViewHeight;
    Bitmap imageBitmap;
    private int mCameraId;
    private CameraPreview mPreview;

    int rows;
    int column;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        rows = intent.getIntExtra("row", 0);
        column = intent.getIntExtra("column", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Camera.getNumberOfCameras() > 0) {
                ivReplay.setVisibility(View.VISIBLE);
            } else {
                ivReplay.setVisibility(View.GONE);

            }
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }


    public void CameraPreview(Activity activity, int cameraId, CameraPreview.LayoutMode mode) {

        mLayoutMode = mode;

        //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
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
        } catch (Exception e) {

        }
        if (mCamera != null) {
            mPreview = new CameraPreview(GridViewActivity.this, 0, CameraPreview.LayoutMode.FitToParent, mCamera);
            RelativeLayout.LayoutParams previewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);



     /*   previewLayoutParams.height = Integer.parseInt(mViewHeight);
        previewLayoutParams.width = Integer.parseInt(mViewWidth);
     */   // Un-comment below line to specify the position.

            relativeLayout.addView(mPreview, 0, previewLayoutParams);

        }


        PixelGridView pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(column);
        pixelGrid.setNumRows(rows);

        relativeLayout.addView(pixelGrid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clickImage.setEnabled(true);
        CameraPreview(GridViewActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);
    }


    @OnClick({R.id.clickImage, R.id.ivGalleryImage, R.id.ivback, R.id.ivReplay})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.clickImage:
                clickImage.setEnabled(false);
                captureimage();

                break;
            case R.id.ivGalleryImage:

                startActivity(new Intent(GridViewActivity.this, LargeImageViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);


                break;

            case R.id.ivback:
                finish();
                break;
            case R.id.ivReplay:
                if (mCameraId == 1) {
                    if (mCamera != null) {

                        mPreview.stop();
                        relativeLayout.removeView(mPreview); // This is necessary.
                        mPreview = null;
                        mCamera.release();
                        mCameraId = 0;

                        CameraPreview(GridViewActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {
                        mCameraId = 0;

                        CameraPreview(GridViewActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    }

                } else {
                    // mCameraId = 0;
                    if (mCamera != null) {

                        mPreview.stop();
                        relativeLayout.removeView(mPreview); // This is necessary.
                        mPreview = null;
                        mCamera.release();
                        mCameraId = 1;

                        CameraPreview(GridViewActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {

                        mCameraId = 1;

                        CameraPreview(GridViewActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);
                    }
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageBitmap != null) {
            imageBitmap.recycle();

        }

    }
/*

    @Produce
    public SendEvents produce() {

        return new SendEvents(imageBitmap, "sdfd");

    }
*/

    public void captureimage() {

        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                Matrix matrix = new Matrix();

                if (mCameraId == 1) {
                    float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
                    Matrix matrixMirrorY = new Matrix();
                    matrixMirrorY.setValues(mirrorY);

                    matrix.postConcat(matrixMirrorY);
                }

                Display display = GridViewActivity.this.getWindowManager().getDefaultDisplay();
                int angle;
                switch (display.getRotation()) {
                    case Surface.ROTATION_0: // This is display orientation
                        angle = 90; // This is camera orientation
                        break;
                    case Surface.ROTATION_90:
                        angle = 0;
                        break;
                    case Surface.ROTATION_180:
                        angle = 270;
                        break;
                    case Surface.ROTATION_270:
                        angle = 180;
                        break;
                    default:
                        angle = 90;
                        break;

                }

                matrix.postRotate(angle);


                //    matrix.postRotate(90);

                imageBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                //GlobalBus.getBus().post(imageBitmap);

                EventBus.getDefault().postSticky(new MyEventModel(imageBitmap, "FixedCameraPreview"));

                //   ivGalleryImage.setImageBitmap(imageBitmap);

                startActivity(new Intent(GridViewActivity.this, LargeImageViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
            relativeLayout.removeView(mPreview); // This is necessary.

        }
        mPreview = null;

        if (mCamera != null) {

            mCamera.release();

        }
    }
    @org.greenrobot.eventbus.Subscribe
    public void onMyEventModel(MyEventModel myEventModel) {

    }

}
