package com.hocrox.digitalviewfinder.DataTransferControl;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hocrox.digitalviewfinder.R;

public class TryActivvtiy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new CircleView(TryActivvtiy.this, 200, 200);
        setContentView(view);

      /*  LinearLayout linearLayout= (LinearLayout) findViewById(R.id.lineaarLayout);
        linearLayout.addView();
*/
       /* Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });
*/

    }

    public void showDialog() {

        Dialog dialog = new Dialog(TryActivvtiy.this, android.R.style.TextAppearance_Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.try1);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    /*    TextView textView = (TextView) dialog.findViewById(R.id.tvProceed);
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


                if (TextUtils.isEmpty(mHeight) || TextUtils.isEmpty(mWidth)) {


                    Snackbar.make(mHeightEt, "Enter Preview Size", Snackbar.LENGTH_LONG).show();

                } else {


                    if (Integer.parseInt(mWidth) < getWidth() && Integer.parseInt(mHeight) < getHeight() / 2) {

                        startActivity(new Intent(HomeActivity.this, FixedSizeActivity.class).putExtra("height", mHeight).putExtra("width", mWidth));
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        dialog.dismiss();

                    } else {

                        Snackbar.make(mHeightEt, "Inaccurate Size", Snackbar.LENGTH_LONG).show();

                    }


                }


            }
        });*/

        dialog.show();
    }

}
