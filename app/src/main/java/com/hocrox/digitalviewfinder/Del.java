package com.hocrox.digitalviewfinder;

import android.app.Activity;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class Del extends AppCompatActivity {
    private int mCameraId;
    private Uri fileUri;
    Camera mCamera;
    RelativeLayout mLayout;
    private CameraPreview mPreview;
    private CameraPreview.LayoutMode mLayoutMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del);
        mLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
    }
    public void CameraPreview(Activity activity, int cameraId, CameraPreview.LayoutMode mode) {

        mLayoutMode = mode;

        //  mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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


        /*   previewLayoutParams.height = 352;
        previewLayoutParams.width = 288;*/
        // Un-comment below line to specify the position.

        mLayout.addView(mPreview, 0, previewLayoutParams);


    }
    @Override
    protected void onResume() {
        super.onResume();

        CameraPreview(Del.this, 0, CameraPreview.LayoutMode.FitToParent);


    }
}
