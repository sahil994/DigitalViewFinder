package com.hocrox.digitalviewfinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hocrox.digitalviewfinder.DataTransferControl.MyEventModel;
import com.hocrox.digitalviewfinder.Testing.CustomView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.hocrox.digitalviewfinder.R.id.clickImage;

public class FixedSizeActivity extends AppCompatActivity {
    private int xDelta;
    private int yDelta;
    Camera mCamera;
    private CameraPreview mPreview;
    LinearLayout linearLayout;
    RelativeLayout mLayout;
    protected Activity mActivity;
    private SurfaceHolder mHolder;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private int mCameraId;
    private Uri fileUri;

    private CameraPreview.LayoutMode mLayoutMode;
    protected List<Camera.Size> mPreviewSizeList;
    protected List<Camera.Size> mPictureSizeList;
    ImageView imageView, mHeartImage, backImage;
    RelativeLayout viewLayout;
    private int mLeft, mTop;
    boolean gridValue = false;

    Bitmap imageBitmap;

    float viewPortHeight, viewPortWidth;
    int heartWidth, heartHeight;
    ImageView ivReplay;
     FloatingActionButton button;

    // private CameraPreview mPreview;
    //   private RelativeLayout relativelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letstry);
        mLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        viewLayout = (RelativeLayout) findViewById(R.id.viewLayout);
        backImage = (ImageView) findViewById(R.id.ivBackArrow);
        ivReplay = (ImageView) findViewById(R.id.ivReplay);
       /* PixelGridView pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(3);
        pixelGrid.setNumRows(3);*/
        button = (FloatingActionButton) findViewById(clickImage);


        //  View view = findViewById(R.id.CropOverlayView);

        // view.bringToFront();
        imageView = (ImageView) findViewById(R.id.ivGalleryImage);
        // mHeartImage = (ImageView) findViewById(R.id.rlSurfaceView);
        Intent intent = getIntent();
        final int mViewWidth = Integer.parseInt(intent.getStringExtra("width"));
        final int mViewHeight = Integer.parseInt(intent.getStringExtra("height"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Camera.getNumberOfCameras() > 0) {
                ivReplay.setVisibility(View.VISIBLE);
            } else {
                ivReplay.setVisibility(View.GONE);

            }
        }





   /*     android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = 80;
        layoutParams.height = 80;
        mHeartImage.setLayoutParams(layoutParams);
*/
       /* mHeartImage.getLayoutParams().height = mViewHeight;
        mHeartImage.getLayoutParams().width = mViewWidth;


        // mHeartImage.bringToFront();
        //mHeartImage.setOnTouchListener(new MultiTouchListener(mHeartImage, getWidth(), 300));

        mHeartImage.setOnTouchListener(new MoveViewTouchListener(mHeartImage, getWidth(), getHeight()));
*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mCamera.takePicture(null, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                        Log.e("testing", "" + data.length);


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


                        Display display = FixedSizeActivity.this.getWindowManager().getDefaultDisplay();
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

                        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);


                        Log.e("bitmap", "" + rotatedBitmap.getHeight() + ">>" + rotatedBitmap.getWidth());

                        int bitmapHeight = rotatedBitmap.getHeight();

                        viewPortHeight = (int) mLayout.getHeight();
                        viewPortWidth = (int) mLayout.getWidth();
                        Log.e("viewport", "" + viewPortWidth + ">>>" + viewPortHeight);

                        float ratioHeight = 0;
                        float ratioWidth = 0;


                        ratioHeight = rotatedBitmap.getHeight() / viewPortHeight;

                        ratioWidth = rotatedBitmap.getWidth() / viewPortWidth;


                        float mFinalY;
                        float mFinalX;

                        mFinalY = (ratioHeight * (CustomView.y));

                        mFinalX = (ratioWidth * (CustomView.x));


                        if (mFinalX < 1) {

                            mFinalX = 1;
                        }

                        if (mFinalY < 1) {

                            mFinalY = 1;
                        }


                        Log.e("finalx", "" + mFinalX + ">>>" + mFinalY);

                        Bitmap finalBitmap = null;


                        if ((mViewWidth * ratioWidth) + mFinalX < rotatedBitmap.getWidth() && (int) (mViewHeight * ratioHeight) + mFinalY < rotatedBitmap.getHeight()) {

                            finalBitmap = Bitmap.createBitmap(rotatedBitmap, (int) (mFinalX), (int) (mFinalY), (int) (mViewWidth * ratioWidth), (int) (mViewHeight * ratioHeight), null, true);

                            imageBitmap = finalBitmap;

                            EventBus.getDefault().postSticky(new MyEventModel(imageBitmap, "FixedCameraPreview"));
                            //   GlobalBus.getBus().post(imageBitmap);
//           /                 imageView.setImageBitmap(finalBitmap);
                            startActivity(new Intent(FixedSizeActivity.this, LargeImageViewActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);


                        } else {
                            Toast.makeText(FixedSizeActivity.this, "UnReachable Area", Toast.LENGTH_LONG).show();
                            camera.startPreview();

                        }


                        //finalBitmap = Bitmap.createBitmap(rotatedBitmap, (int) (mFinalX + 40), (int) (mFinalY + 60), (int) (heartWidth * ratioWidth), (int) (heartHeight * ratioHeight));


                        //                      }


                        //    Bitmap rotatedBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(), matrix, true);


                        // Bitmap original = bitmap;


                     /*   //  Bitmap resizedBitmap = rotatedBitmap;
                        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas mCanvas = new Canvas(result);
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                        mCanvas.drawBitmap(rotatedBitmap, 0, 0, null);
                        mCanvas.drawBitmap(mask,0,0, paint);
                        paint.setXfermode(null);
                        imageView.setImageBitmap(result);
                        imageView.setScaleType(ImageView.ScaleType.CENTER);



                        Bitmap bmp2 = rotatedBitmap;
                        Bitmap bmOverlay = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);

                        Paint paint = new Paint();
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

                        Canvas canvas = new Canvas(bmOverlay);
                        canvas.drawBitmap(bmp2, 0, 0, null);
                        canvas.drawRect(mFinalX,mFinalY, 100, 100, paint);
                        imageView.setImageBitmap(bmOverlay);
*/


                        //  cropBitmap(bm,mFinalX,mFinalY);

                        //  M
                        //
                        //
                        //atrix matrix = new Matrix();
                        //matrix.postRotate(90);

                        //      imageView.setImageBitmap(bm);
                        //    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);



                 /*       int width = bm.getWidth();
                        int height = bm.getHeight();
                        int newWidth = 200;
                        int newHeight = 200;


                        float scaleWidth = ((float) newWidth) / width;
                        float scaleHeight = ((float) newHeight) / height;

                        Matrix matrix = new Matrix();

                        matrix.postScale(scaleWidth, scaleHeight);

                        // Log.e("tesitng", "" + mTop + ">>>>>" + mLeft);


                        //   cropBitmap(bm);
                        // Bitmap resizedBitmap = Bitmap.createBitmap(bm,(int) MoveViewTouchListener.x,(int) MoveViewTouchListener.y,300,300,null,true);
//

                        //    imageView.setImageBitmap(resizedBitmap);



                        Matrix matrix = new Matrix();
                        matrix.postScale(xDelta,yDelta);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bm,30,30, bm.getWidth(), bm.getHeight(), matrix, true);

                        imageView.setImageBitmap(rotatedBitmap);

                        //    Log.e("tesitnf",""+bitmap.getWidth()+">>"+bitmap.getHeight());

                        Matrix matrix = new Matrix();
                     //   matrix.postRotate(90);
                        matrix.postScale(mLeft,mTop);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        imageView.setImageBitmap(rotatedBitmap);*/
                        //     imageView.setRotation(90);
                        //   mCamera = Camera.open(mCameraId);


                    }
                });

               button.setEnabled(false);
            }



        });

        //  mCamera=get
        // setContentView(R.layout.activity_letstry);
        /*preview = new Preview(FixedSizeActivity.this);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                safeCameraOpen(234231);

            }
        });*/


        //   mHeartImage.setOnTouchListener(onTouchListener());

       /*  relativelayout.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
               Log.e("dsfsdfdsf",""+event.getRawX()+""+event.getRawY());

                 return true;
             }
         });*/

        final CustomView view = new CustomView(FixedSizeActivity.this, mViewWidth, mViewHeight, gridValue);
        viewLayout.addView(view);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  startActivity(new Intent(FixedSizeActivity.this, LargeImageViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
*/

                if (!gridValue) {
                    CustomView view = new CustomView(FixedSizeActivity.this, mViewWidth, mViewHeight, true);
                    gridValue = true;
                    viewLayout.removeAllViews();
                    viewLayout.addView(view);
                    //  CameraPreview(FixedSizeActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                } else {
                    CustomView view = new CustomView(FixedSizeActivity.this, mViewWidth, mViewHeight, false);
                    gridValue = false;
                    viewLayout.removeAllViews();
                    viewLayout.addView(view);
                    //   CameraPreview(FixedSizeActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);
                }


            }
        });


        // viewLayout.addView(pixelGrid);
        //   mLayout.addView(new Viewewenruigf(FixedSizeActivity.this,mViewWidth,mViewHeight));

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ivReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mCameraId == 1) {
                    if (mCamera != null) {


                        mPreview.stop();
                        mLayout.removeView(mPreview); // This is necessary.
                        mPreview = null;
                        mCamera.release();
                        mCameraId = 0;

                        CameraPreview(FixedSizeActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {
                        mCameraId = 0;

                        CameraPreview(FixedSizeActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    }

                } else {
                    // mCameraId = 0;
                    if (mCamera != null) {

                        mPreview.stop();
                        mLayout.removeView(mPreview); // This is necessary.
                        mPreview = null;
                        mCamera.release();
                        mCameraId = 1;

                        CameraPreview(FixedSizeActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {
                        mCameraId = 1;

                        CameraPreview(FixedSizeActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    }


                }


            }
        });


    }

    @org.greenrobot.eventbus.Subscribe
    public void onMyEventModel(MyEventModel myEventModel) {

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            /*ImageView img = (ImageView) findViewById(R.id.img);
            Log.d(TAG, "width : " + img.getWidth());
     */

         /*   heartWidth = mHeartImage.getWidth();
            heartHeight = mHeartImage.getHeight();
            Log.e("sahilllll", "" + mHeartImage.getWidth() + ">>>" + mHeartImage.getHeight());
*/

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

    public void convert() {

        SimpleDateFormat inputFormat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
        inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd yyyy ");
// Adjust locale and zone appropriately

        Date date = null;
        try {
            date = inputFormat.parse("2017-06-15T12:44:20+05:30");
            Log.e("siofdjedwk", "" + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // String outputText = outputFormat.format(date);
        // Log.e("siofdjedwk",""+outputText);
    }


    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawY();
                final int y = (int) event.getRawY();

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;

                        Log.e("tesitngssss", "" + event.getRawX() + ">>" + event.getRawY() + ">>>" + event.getX() + ">>" + event.getY());

                        break;

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(FixedSizeActivity.this,
                                "thanks for new location!", Toast.LENGTH_SHORT)
                                .show();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        //    mLeft = layoutParams.leftMargin;
                        //  mTop = layoutParams.topMargin;
                        // mLeft = layoutParams.leftMargin ;
                        //mTop = layoutParams.topMargin;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        Log.e("tesitng", "" + layoutParams.leftMargin + ">>>" + layoutParams.topMargin + ">>>" + x + ">>>" + y + ">>" + xDelta + ">>" + yDelta);
                        view.setLayoutParams(layoutParams);
                        break;
                }
                mLayout.invalidate();
                return true;
            }
        };
    }

    public void CameraPreview(Activity activity, int cameraId, CameraPreview.LayoutMode mode) {

        mLayoutMode = mode;

        //  mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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

            mPreview = new CameraPreview(this, mCameraId, CameraPreview.LayoutMode.FitToParent, mCamera);
            RelativeLayout.LayoutParams previewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mLayout.addView(mPreview, 0, previewLayoutParams);
            viewPortHeight = mLayout.getHeight();
            viewPortWidth = mLayout.getWidth();
            Log.e("bitmap", "" + mLayout.getHeight() + ">>" + mLayout.getWidth());
        }
     /*   previewLayoutParams.height = 352;
        previewLayoutParams.width = 288;*/
        // Un-comment below line to specify the position.


        //  mPreview.setBackgroundColor(getResources().getColor(R.color.black));


    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            imageView.setImageBitmap(bitmap);

            /*   File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }*/
        }
    };

    public void onSelectImageClick() {

        CropImage.startPickImageActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                Uri mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        // handle result of CropImageActivity
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    Uri selectedMedia = result.getUri();
                    Bitmap selectedBitmap = null;
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedMedia);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //       imageView.setImageBitmap(selectedBitmap);
                    //cropBitmap(selectedBitmap);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }
        }


    }

    public void cropBitmap(Bitmap bitmap, float mFinalX, float mFinalY) {

        //Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.nature);
        // Bitmap original = bitmap;


        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.heart_q80_filled);
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(resizedBitmap, mFinalX, mFinalY, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        imageView.setImageBitmap(result);
        imageView.setScaleType(ImageView.ScaleType.CENTER);


    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON, 3, 3)
                .start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        button.setEnabled(true);
        CameraPreview(FixedSizeActivity.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

        //LinearLayout.LayoutParams previewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // Un-comment below lines to specify the size.

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

  /*  @Produce
    public Events events() {

        return new Events(imageBitmap, "FixedCameraPreview");

    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // imageBitmap.recycle();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
            mLayout.removeView(mPreview); // This is necessary.

        }
        mPreview = null;

        if (mCamera != null) {

            mCamera.release();

        }

    }

    /*private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(1);
            qOpened = (mCamera != null);

            // Preview preview = new Preview(FixedSizeActivity.this);
            //  linearLayout.addView(preview);

            mPreview.setCamera(mCamera);

        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }*/
}
