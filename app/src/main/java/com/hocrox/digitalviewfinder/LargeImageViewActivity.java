package com.hocrox.digitalviewfinder;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hocrox.digitalviewfinder.DataTransferControl.Events;
import com.hocrox.digitalviewfinder.DataTransferControl.HomeEvents;
import com.hocrox.digitalviewfinder.DataTransferControl.MyEventModel;
import com.hocrox.digitalviewfinder.DataTransferControl.SendEvents;
import com.hocrox.digitalviewfinder.DataTransferControl.VariableEvents;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LargeImageViewActivity extends AppCompatActivity {

    private static final String TAG = "LargeImageView";
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.ivuserImage)
    ImageView ivuserImage;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.ivSave)
    TextView ivSave;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    Bitmap finalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_view);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //GlobalBus.getBus().register(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //GlobalBus.getBus().unregister(this);
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        bitmap.recycle();

    }

    @OnClick({R.id.ivClose, R.id.ivSave})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ivClose:
                finish();
                break;

            case R.id.ivSave:

                if (ApplicationConstants.finalBitmap != null) {
              Log.e("Testimf","cliekceddddddd");
                    progressDialog = new ProgressDialog(LargeImageViewActivity.this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();
                    storeImage(bitmap);

                } else {

                    Toast.makeText(getApplicationContext(), "Unable to Save", Toast.LENGTH_LONG).show();

                }
                break;

        }


    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            Toast.makeText(getApplicationContext(), "Image Saved Successfully into DigitalViewFinder ", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        progressDialog.dismiss();

    }


    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
/*
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
*/

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/DigitalViewFinder");


        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    @Subscribe
    public void getBitmap(Events events) {

        Log.e("testigsss", "" + events.getBitmap() + ">>>" + events.getName());
        bitmap = events.getBitmap();
        //   Bitmap finalbitmap = bitmap.createScaledBitmap(bitmap, events.getBitmap().getWidth(), events.getBitmap().getHeight(), true);
        ivuserImage.setImageBitmap(bitmap);

        setBitmap(bitmap);

    }

    @Subscribe
    public void getBitmap(SendEvents events) {

        Log.e("testig", "" + events.getBitmap() + ">>>" + events.getName());

        bitmap = events.getBitmap();
        ivuserImage.setImageBitmap(events.getBitmap());

        setBitmap(bitmap);
    }

    @Subscribe
    public void getBitmap(HomeEvents events) {

        Log.e("testig", "" + events.getBitmap() + ">>>" + events.getName());

        bitmap = events.getBitmap();
        setBitmap(bitmap);
    }

    @Subscribe
    public void getBitmap(VariableEvents events) {

        Log.e("testig", "" + events.getBitmap() + ">>>" + events.getName());
        bitmap = events.getBitmap();
        setBitmap(bitmap);

    }

    public void setBitmap(Bitmap bitmap) {

        if (bitmap != null) {

            ApplicationConstants.finalBitmap = bitmap;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivuserImage.setImageBitmap(ApplicationConstants.finalBitmap);
                }
            });
        }

    }

    @org.greenrobot.eventbus.Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onMyEventModel(MyEventModel myEventModel) {
        bitmap = myEventModel.getBitmap();
        setBitmap(bitmap);
    }


}