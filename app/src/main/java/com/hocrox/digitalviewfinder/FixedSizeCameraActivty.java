package com.hocrox.digitalviewfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hocrox.digitalviewfinder.DataTransferControl.CircleView;
import com.hocrox.digitalviewfinder.DataTransferControl.Events;
import com.hocrox.digitalviewfinder.DataTransferControl.GlobalBus;
import com.hocrox.digitalviewfinder.DataTransferControl.HeartView;
import com.hocrox.digitalviewfinder.DataTransferControl.MyEventModel;
import com.hocrox.digitalviewfinder.DataTransferControl.OvalView;
import com.squareup.otto.Produce;
import com.svgandroid.SVG;
import com.svgandroid.SVGParser;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FixedSizeCameraActivty extends AppCompatActivity {

    @BindView(R.id.ivBackArrow)
    ImageView ivBackArrow;
    @BindView(R.id.ivShapeView)
    ImageView ivShapeView;
    @BindView(R.id.ivSmallHeart)
    ImageView ivSmallHeart;
    @BindView(R.id.ivSmallCircle)
    ImageView ivSmallCircle;
    @BindView(R.id.ivSmallOval)
    ImageView ivSmallOval;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.ivGalleryImage)
    ImageView ivGalleryImage;
    @BindView(R.id.clickImage)
    FloatingActionButton clickImage;

    @BindView(R.id.viewLayout)
    RelativeLayout viewLayout;

    @BindView(R.id.ivReplay)
    ImageView ivReplay;


    private int mCameraId = 0;
    private Uri mFileUri;
    Camera mCamera;
    private CameraPreview mPreview;
    private CameraPreview.LayoutMode mLayoutMode;

    Bitmap userimageBitmap;
    float viewPortHeight, viewPortWidth;
    String type = "heart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_size_camera_activtiy);


        ButterKnife.bind(this);

        //   ivShapeView.setImageResource(R.drawable.like);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Camera.getNumberOfCameras() > 0) {
                ivReplay.setVisibility(View.VISIBLE);
            } else {
                ivReplay.setVisibility(View.GONE);

            }
        }
        //  SVGParser svg = SVGParser.getSVGFromResource(getResources(), R.drawable.);

        ivShapeView.setOnTouchListener(new MoveViewTouchListener(ivShapeView, getWidth(), getHeight()));

    }

    @org.greenrobot.eventbus.Subscribe
    public void onMyEventModel(MyEventModel myEventModel) {

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
    protected void onResume() {
        super.onResume();
        clickImage.setEnabled(true);
        CameraPreview(FixedSizeCameraActivty.this, mCameraId, CameraPreview.LayoutMode.FitToParent);
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

    @OnClick({R.id.clickImage, R.id.ivGalleryImage, R.id.ivSmallCircle, R.id.ivSmallHeart, R.id.ivSmallOval, R.id.ivBackArrow, R.id.ivReplay})
    public void onclick(View view) {
        switch (view.getId()) {

            case R.id.clickImage:
                clickImage.setEnabled(false);
                captureimage();


                break;
            case R.id.ivGalleryImage:
                startActivity(new Intent(FixedSizeCameraActivty.this, LargeImageViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case R.id.ivSmallCircle:
                type = "circle";
                viewLayout.removeAllViews();
                View circleView = new CircleView(FixedSizeCameraActivty.this, 400, 400);
                viewLayout.addView(circleView);
                ivShapeView.setImageResource(R.drawable.strokecircle_128);

                break;

            case R.id.ivSmallHeart:

                type = "heart";

                viewLayout.removeAllViews();
                View heartView = new HeartView(FixedSizeCameraActivty.this, 200, 200);
                viewLayout.addView(heartView);

                ivShapeView.setImageResource(R.drawable.stroke_heart128);


                break;
            case R.id.ivSmallOval:

                type = "oval";


                viewLayout.removeAllViews();
                View ovalView = new OvalView(FixedSizeCameraActivty.this, 400, 400);
                viewLayout.addView(ovalView);


                ivShapeView.setImageResource(R.drawable.strokeoval_128);

                break;

            case R.id.ivBackArrow:
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

                        CameraPreview(FixedSizeCameraActivty.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {
                        mCameraId = 0;

                        CameraPreview(FixedSizeCameraActivty.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    }

                } else {
                    // mCameraId = 0;
                    if (mCamera != null) {

                        mPreview.stop();
                        relativeLayout.removeView(mPreview); // This is necessary.
                        mPreview = null;
                        mCamera.release();
                        mCameraId = 1;

                        CameraPreview(FixedSizeCameraActivty.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    } else {
                        mCameraId = 1;

                        CameraPreview(FixedSizeCameraActivty.this, mCameraId, CameraPreview.LayoutMode.FitToParent);

                    }
                }
                break;

        }
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


            mPreview = new CameraPreview(this, 0, CameraPreview.LayoutMode.FitToParent, mCamera);
            RelativeLayout.LayoutParams previewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.addView(mPreview, 0, previewLayoutParams);
            viewLayout.removeAllViews();
            type="heart";
            viewLayout.addView(new HeartView(FixedSizeCameraActivty.this, 200, 200));

        } catch (Exception e) {

        }

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

                float shapeX = 0, shapeY = 0;
                float shapeWidth = 0, shapeHeight = 0;


                if (type.equalsIgnoreCase("oval")) {

                    shapeX = OvalView.finalx;
                    shapeY = OvalView.finaly;
                    shapeWidth = OvalView.WIDTH;
                    shapeHeight = OvalView.HEIGHT;


                } else if (type.equalsIgnoreCase("circle")) {

                    shapeX = CircleView.finalx;
                    shapeY = CircleView.finaly;
                    shapeWidth = CircleView.WIDTH;
                    shapeHeight = CircleView.HEIGHT;


                } else if (type.equalsIgnoreCase("heart")) {

                    shapeX = HeartView.finalx;
                    shapeY = HeartView.finaly;
                    shapeWidth = HeartView.WIDTH;
                    shapeHeight = HeartView.HEIGHT;


                }
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

                matrix.postRotate(90);


                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);


                Log.e("bitmap", "" + rotatedBitmap.getHeight() + ">>" + rotatedBitmap.getWidth());

                int bitmapHeight = rotatedBitmap.getHeight();

                viewPortHeight = (int) relativeLayout.getHeight();
                viewPortWidth = (int) relativeLayout.getWidth();
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


                    if (type.equalsIgnoreCase("heart")) {
                        finalBitmap = Bitmap.createBitmap(rotatedBitmap, (int) (mFinalX), (int) (mFinalY), (int) (shapeWidth * ratioWidth), (int) (shapeHeight * ratioHeight));

                    } else {

                        finalBitmap = Bitmap.createBitmap(rotatedBitmap, (int) (mFinalX), (int) (mFinalY), (int) (shapeWidth * ratioWidth), (int) (shapeHeight * ratioHeight));

                    }

                    Log.e("Final bitmap",""+finalBitmap.getWidth()+""+finalBitmap.getHeight());
                    // ivGalleryImage.setImageBitmap(finalBitmap);
                    cropBitmap(finalBitmap);
                  /*  if (camera != null) {

                        camera.startPreview();


                    }*/
                    // CameraPreview(FixedSizeCameraActivty.this, 0, CameraPreview.LayoutMode.FitToParent);

                } else {

                    Toast.makeText(FixedSizeCameraActivty.this, "UnReachable Area", Toast.LENGTH_LONG).show();
                    if (camera != null) {

                        camera.startPreview();

                    }
                    //CameraPreview(FixedSizeCameraActivty.this, 0, CameraPreview.LayoutMode.FitToParent);

                }


            }
        });
    }

    public void cropBitmap(Bitmap bitmap) {

        //Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.nature);
        //   Bitmap original = bitmap;

        Bitmap mask = null;


        if (type.equalsIgnoreCase("heart")) {


            mask = BitmapFactory.decodeResource(getResources(), R.drawable.finalheart);

            /*if (bitmap.getWidth()>500) {

                mask = BitmapFactory.decodeResource(getResources(), R.drawable.heart_700);


            }*/ /*else if (bitmap.getWidth() < 500 && bitmap.getWidth() > 300) {


                mask = BitmapFactory.decodeResource(getResources(), R.drawable.heart_600);


            } else if (bitmap.getWidth() < 300) {


                mask = BitmapFactory.decodeResource(getResources(), R.drawable.heart_500);


            }*/

        } else if (type.equalsIgnoreCase("circle")) {


            if (bitmap.getWidth() > 500) {

                mask = BitmapFactory.decodeResource(getResources(), R.drawable.circle_1000);


            } else if (bitmap.getWidth() < 500 && bitmap.getWidth() > 300) {


                mask = BitmapFactory.decodeResource(getResources(), R.drawable.circle_400);


            } else if (bitmap.getWidth() < 300) {


                mask = BitmapFactory.decodeResource(getResources(), R.drawable.circle_300);


            }


        } else if (type.equalsIgnoreCase("oval")) {


/*            if (bitmap.getWidth() > 500) {

                    mask = BitmapFactory.decodeResource(getResources(), R.drawable.oval_900);


            } else if (bitmap.getWidth() < 500 && bitmap.getWidth() > 300) {


                mask = BitmapFactory.decodeResource(getResources(), R.drawable.oval_400);


            } else if (bitmap.getWidth() < 300) {


                mask = BitmapFactory.decodeResource(getResources(), R.drawable.ovals_300);

            }*/

            mask = BitmapFactory.decodeResource(getResources(), R.drawable.finaloval);


        }


        if (mask != null) {

            Log.e("bitmap hheart", "" + getBitmapFromVectorDrawable(FixedSizeCameraActivty.this, R.drawable.ic_hearts, bitmap.getWidth(), bitmap.getHeight()));

            //  mask = getMask(bitmap.getWidth(), bitmap.getHeight());
            Log.e("bitmap final", "" + mask.getWidth() + ">>>" + mask.getHeight());

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap, mask.getWidth(), mask.getHeight(), false);


            //Bitmap resizedBitmap = scaleBitmap(bsk);
            Bitmap result = Bitmap.createBitmap(mask.getWidth(),mask.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas mCanvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
             paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mCanvas.drawBitmap(resizedBitmap, 0, 0, null);
            mCanvas.drawBitmap(mask, 0, 0, paint);

            paint.setXfermode(null);
            ivGalleryImage.setScaleType(ImageView.ScaleType.CENTER);
            userimageBitmap = result;

            EventBus.getDefault().postSticky(new MyEventModel(userimageBitmap,"FixedCameraPreview"));
             GlobalBus.getBus().post(events());
           // ivGalleryImage.setImageBitmap(userimageBitmap);
            startActivity(new Intent(FixedSizeCameraActivty.this, LargeImageViewActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);

        }


    }

    public static Bitmap blur(Context context, Bitmap image) {

           float BITMAP_SCALE = 0.4f;
           float BLUR_RADIUS = 7.5f;

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, int width, int height) {

        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Log.e("fixed", "" + bitmap.getHeight() + ">>>" + bitmap.getWidth());
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        Log.e("fixed", "" + bitmap.getHeight() + ">>>" + bitmap.getWidth());

        drawable.draw(canvas);

        return bitmap;
    }


    public Bitmap scaleBitmap(Bitmap orignalbitmap, Bitmap mask) {


        Bitmap background = Bitmap.createBitmap((int) mask.getWidth(), (int) mask.getHeight(), Bitmap.Config.ARGB_8888);

        float originalWidth = orignalbitmap.getWidth();
        float originalHeight = orignalbitmap.getHeight();

        Canvas canvas = new Canvas(background);
        float scale;
        float xTranslation;
        float yTranslation = 0;

        if (orignalbitmap.getWidth() > mask.getWidth()) {

            scale = mask.getWidth() / originalWidth;
            xTranslation = 0.0f;
            yTranslation = (mask.getHeight() - originalHeight * scale) / 2.0f;


        } else {

            scale = originalWidth / mask.getWidth();
            xTranslation = 0.0f;
            if (originalHeight > mask.getHeight()) {

                yTranslation = (originalHeight - mask.getHeight() * scale) / 2.0f;

            }


        }

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(orignalbitmap, transformation, paint);

        return background;

    }

  /*  private Bitmap getMask(int width, int height) {
        SVG svgMask = null;
        svgMask = SVGParser.getSVGFromInputStream(
                this.getResources().openRawResource(R.raw.hearts));

        //  SVG i = SVGParser.getSVGFromResource(getResources(), R.drawable.ic_hearts);
//The following is needed because of image accelaration in some devices such as samsung
        //  imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Bitmap bitmap = Bitmap.createBitmap(500, 500,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

         canvas.drawPicture(svgMask.getPicture());

        return bitmap;

    }*/

    private Bitmap getMask(int width, int height) {

        SVG svgMask = null;
        svgMask = SVGParser.getSVGFromInputStream(
                this.getResources().openRawResource(R.raw.imgview_heart), width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        canvas.drawPicture(svgMask.getPicture());

        return bitmap;

    }

    @Produce
    public Events events() {

        return new Events(userimageBitmap, "FixedSize");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
/*
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId,float width, float height) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }*/
}