package com.hocrox.digitalviewfinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fujiyuu75.sequent.Sequent;
import com.hocrox.digitalviewfinder.DataTransferControl.GlobalBus;
import com.hocrox.digitalviewfinder.DataTransferControl.HomeEvents;
import com.hocrox.digitalviewfinder.DataTransferControl.VariableSizesActivtiy;
import com.squareup.otto.Produce;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.ivEnterSize)
    ImageView ivEnterSize;
    @BindView(R.id.ivFixedSize)
    ImageView ivFixedSize;
    @BindView(R.id.ivVariableSize)
    ImageView ivVariableSize;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.root_cardview)
    LinearLayout rootCardview;
    @BindView(R.id.root)
    RelativeLayout root;

    Bitmap bitmap;

    int mRow, mcolumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Sequent.origin(root).anim(HomeActivity.this, R.anim.fadeup).start();

    }

    @OnClick({R.id.ivEnterSize, R.id.ivFixedSize, R.id.ivVariableSize, R.id.ivGrid})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ivEnterSize:

                showDialog();

                break;
            case R.id.ivFixedSize:


                startActivity(new Intent(HomeActivity.this, FixedSizeCameraActivty.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
             /*   dialog.dismiss();

                startActivity(new Intent(HomeActivity.this, TestActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
*/
                break;
            case R.id.ivVariableSize:

                //   startActivity(new Intent(HomeActivity.this, VariableSizeActivity.class));
                // overridePendingTransition(R.anim.enter, R.anim.exit);


                mRow = 3;
                mcolumn = 3;

                startActivity(new Intent(HomeActivity.this, VariableSizesActivtiy.class).putExtra("row", mRow).putExtra("column", mcolumn));
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;


            case R.id.ivGrid:

                Toast.makeText(HomeActivity.this, "Coming Soon :)", Toast.LENGTH_LONG).show();

                // showRowsDialog();

                break;

        }


    }


    public void showDialog() {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.fixed_size_dialog);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = (TextView) dialog.findViewById(R.id.tvProceed);
        TextView dismiss = (TextView) dialog.findViewById(R.id.tvDismiss);
        final EditText mHeightEt = (EditText) dialog.findViewById(R.id.etHeight);
        final EditText mWidthEt = (EditText) dialog.findViewById(R.id.etWidth);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mHeight = mHeightEt.getText().toString();
                String mWidth = mWidthEt.getText().toString();


                int height = Integer.parseInt(mHeight);
                int width = Integer.parseInt(mWidth);



                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1,
                        getResources().getDisplayMetrics());


                  float x=  width*10*px;
                  float y=  height*10*px;


                Log.e("Tesing ggg",""+x+""+y);



                if (TextUtils.isEmpty(mHeight) || TextUtils.isEmpty(mWidth)) {


                    Snackbar.make(mHeightEt, "Enter Preview Size", Snackbar.LENGTH_LONG).show();

                } else {


                    if ((int)x < getWidth() && (int)y < getHeight()-100) {

                        startActivity(new Intent(HomeActivity.this, FixedSizeActivity.class).putExtra("height", ""+(int)y).putExtra("width",""+(int)x));
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        dialog.dismiss();

                    } else {

                        if((int)x>getWidth()){

                            Snackbar.make(mHeightEt, "Inaccurate Width", Snackbar.LENGTH_LONG).show();

                        }

                        else if((int)y>getHeight()){

                            Snackbar.make(mHeightEt, "Inaccurate Height", Snackbar.LENGTH_LONG).show();

                        }
                        else{

                            Snackbar.make(mHeightEt, "Inaccurate Size", Snackbar.LENGTH_LONG).show();

                        }

                    }


                }


            }
        });

        dialog.show();
    }


    public void showRowsDialog() {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_row);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = (TextView) dialog.findViewById(R.id.tvProceed);
        TextView dismiss = (TextView) dialog.findViewById(R.id.tvDismiss);
        final EditText mColumn = (EditText) dialog.findViewById(R.id.etColumn);
        final EditText mRows = (EditText) dialog.findViewById(R.id.etRows);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String column = mColumn.getText().toString();
                String row = mRows.getText().toString();


                if (TextUtils.isEmpty(column) || TextUtils.isEmpty(row)) {


                    Snackbar.make(mColumn, "Enter Rows And Columns", Snackbar.LENGTH_LONG).show();

                } else {


                    if (Integer.parseInt(row) < 10 && Integer.parseInt(column) < 10) {

                        mRow = Integer.parseInt(row);
                        mcolumn = Integer.parseInt(column);
                        dialog.dismiss();

                        startActivity(new Intent(HomeActivity.this, GridViewActivity.class).putExtra("row", mRow).putExtra("column", mcolumn));
                        overridePendingTransition(R.anim.enter, R.anim.exit);

                    } else {

                        Snackbar.make(mColumn, "Rows And Columns must be less than 10", Snackbar.LENGTH_LONG).show();

                    }


                }


            }
        });

        dialog.show();
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

                        bitmap = selectedBitmap;

                        GlobalBus.getBus().post(events());

                        startActivity(new Intent(HomeActivity.this, LargeImageViewActivity.class));


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

    public void onSelectImageClick() {

        CropImage.startPickImageActivity(this);
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON, mRow, mcolumn)
                .start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(HomeActivity.this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Produce
    public HomeEvents events() {

        return new HomeEvents(bitmap, "VariableSize");

    }
}
