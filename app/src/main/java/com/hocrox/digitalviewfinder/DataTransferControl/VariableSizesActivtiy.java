package com.hocrox.digitalviewfinder.DataTransferControl;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hocrox.digitalviewfinder.CameraPreview;
import com.hocrox.digitalviewfinder.LargeImageViewActivity;
import com.hocrox.digitalviewfinder.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VariableSizesActivtiy extends AppCompatActivity {
    private int mCameraId;
    @BindView(R.id.gridlayout)
    RelativeLayout gridlayout;

    @BindView(R.id.cameralayout)
    RelativeLayout cameralayout;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.ivGalleryImage)
    ImageView ivGalleryImage;
    @BindView(R.id.clickImage)
    FloatingActionButton clickImage;
    @BindView(R.id.bottomLayout)
    RelativeLayout bottomLayout;
    @BindView(R.id.ivReplay)
    ImageView ivReplay;

    @BindView(R.id.ivback)
    ImageView back;


    private boolean gridValue = false;
    private CameraPreview mPreview;
    Camera mCamera;
    Bitmap imageBitmap;
    private CameraPreview.LayoutMode mLayoutMode;
    int width=400, height=400;
   public static float viewPortHeight, viewPortWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        ButterKnife.bind(this);

        if (Camera.getNumberOfCameras() > 0) {


            ivReplay.setVisibility(View.VISIBLE);

        } else {

            mCameraId = 0;
            ivReplay.setVisibility(View.GONE);


        }
    }

    @org.greenrobot.eventbus.Subscribe
    public void onMyEventModel(MyEventModel myEventModel) {

    }
    @OnClick({R.id.clickImage, R.id.ivGalleryImage, R.id.ivReplay, R.id.ivback})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.clickImage:

                clickImage.setEnabled(false);
                captureimage();


                break;
            case R.id.ivGalleryImage:
               /* startActivity(new Intent(VariableSizesActivtiy.this, LargeImageViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
*/


                if (!gridValue) {

                    gridValue = true;
                    cameralayout.removeAllViews();
                    cameralayout.addView(new CustomRectangle(this, width, height, true));
                    CameraPreview(VariableSizesActivtiy.this, mCameraId, CameraPreview.LayoutMode.FitToParent);


                } else {
                    gridValue = false;
                    cameralayout.removeAllViews();
                    cameralayout.addView(new CustomRectangle(this, width, height, false));
                    CameraPreview(VariableSizesActivtiy.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                }


                break;

            case R.id.ivReplay:

                if (mCameraId == 1) {
                    if (mCamera != null) {


                        mPreview.stop();
                        cameralayout.removeView(mPreview); // This is necessary.
                        mPreview = null;
                        mCamera.release();
                        mCameraId = 0;

                        CameraPreview(VariableSizesActivtiy.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {
                        mCameraId = 0;

                        CameraPreview(VariableSizesActivtiy.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    }

                } else {
                    // mCameraId = 0;
                    if (mCamera != null) {

                        mPreview.stop();
                        cameralayout.removeView(mPreview); // This is necessary.
                        mPreview = null;
                        mCamera.release();
                        mCameraId = 1;

                        CameraPreview(VariableSizesActivtiy.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {
                        mCameraId = 1;

                        CameraPreview(VariableSizesActivtiy.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    }


                }
                break;

            case R.id.ivback:

                finish();
                break;


        }


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
        clickImage.setEnabled(true);
        CameraPreview(VariableSizesActivtiy.this, mCameraId, CameraPreview.LayoutMode.FitToParent);
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


   /*     previewLayoutParams.height = Integer.parseInt(mViewHeight);
        previewLayoutParams.width = Integer.parseInt(mViewWidth);
*/
        cameralayout.setGravity(RelativeLayout.CENTER_HORIZONTAL);
        cameralayout.addView(mPreview, 0, previewLayoutParams);
        //  cameralayout.setOnTouchListener(new MoveViewTouchListener(cameralayout, getWidth(), cameralayout.getHeight()));



        cameralayout.addView(new CustomRectangle(this, width, height, gridValue));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        viewPortHeight = cameralayout.getHeight();

        //width = cameralayout.getHeight();
        // height = cameralayout.getWidth();
        Log.e("testing inside", "" + width + ">>>>" + height);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }



    public void captureimage() {

        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                Log.e("testing", "" + data.length);

                float shapeX = (CustomRectangle.x3 - CustomRectangle.width / 2) + 30, shapeY = (CustomRectangle.y3 - CustomRectangle.height / 2) + 30;
                float shapeWidth = CustomRectangle.width, shapeHeight = CustomRectangle.height;


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                Matrix matrix = new Matrix();

                if (mCameraId == 1) {
                    float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
                    Matrix matrixMirrorY = new Matrix();
                    matrixMirrorY.setValues(mirrorY);

                    matrix.postConcat(matrixMirrorY);
                }


                int angle;
                Display display = VariableSizesActivtiy.this.getWindowManager().getDefaultDisplay();
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
              /*  Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);


                ivGalleryImage.setImageBitmap(rotatedBitmap);
*/
              /*  Matrix matrix = new Matrix();
                matrix.postRotate(90);
              */
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);


                Log.e("bitmap", "" + rotatedBitmap.getHeight() + ">>" + rotatedBitmap.getWidth());

                int bitmapHeight = rotatedBitmap.getHeight();

                viewPortHeight = cameralayout.getHeight();
                viewPortWidth = cameralayout.getWidth();
                Log.e("viewport", "" + viewPortWidth + ">>>" + viewPortHeight);

                float ratioHeight = 0;
                float ratioWidth = 0;


                ratioHeight = rotatedBitmap.getHeight() / viewPortHeight;

                ratioWidth = rotatedBitmap.getWidth() / viewPortWidth;


                float mFinalY;
                float mFinalX;


                mFinalY = (ratioHeight * (shapeY));
                mFinalX = (ratioWidth * (shapeX));


                if (mFinalX < 1) {

                    mFinalX = 1;
                }

                if (mFinalY < 1) {

                    mFinalY = 1;
                }


                Log.e("finalx", "" + mFinalX + ">>>" + mFinalY);

                Bitmap finalBitmap = null;


                if ((shapeWidth * ratioWidth) + mFinalX < rotatedBitmap.getWidth() && (int) (shapeHeight * ratioHeight) + mFinalY < rotatedBitmap.getHeight()) {

                    finalBitmap = Bitmap.createBitmap(rotatedBitmap, (int) (mFinalX), (int) (mFinalY), (int) (shapeWidth * ratioWidth), (int) (shapeHeight * ratioHeight));


                   // GlobalBus.getBus().post(events());
                    imageBitmap = finalBitmap;
                    EventBus.getDefault().postSticky(new MyEventModel(imageBitmap,"FixedCameraPreview"));
                    //   ivGalleryImage.setImageBitmap(finalBitmap);
                    startActivity(new Intent(VariableSizesActivtiy.this, LargeImageViewActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);


                    //    cropBitmap(finalBitmap);

                    // CameraPreview(FixedSizeCameraActivty.this, 0, CameraPreview.LayoutMode.FitToParent);

                } else {

                    Toast.makeText(VariableSizesActivtiy.this, "UnReachable Area", Toast.LENGTH_LONG).show();
                    if (camera != null) {

                        camera.startPreview();

                    }
                    //CameraPreview(FixedSizeCameraActivty.this, 0, CameraPreview.LayoutMode.FitToParent);

                }

            }
        });


    }
}
