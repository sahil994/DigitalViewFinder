package com.hocrox.digitalviewfinder.datacontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.hocrox.digitalviewfinder.GetActivity;
import com.hocrox.digitalviewfinder.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.fabric.sdk.android.Fabric;


public class PostingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_posting);
        Button button = (Button) findViewById(R.id.btnClick);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().postSticky(new MessageEvent("sasasass"));

                startActivity(new Intent(PostingActivity.this, GetActivity.class));

            }
        });





    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
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




    @Subscribe
    public void onEvent(final MessageEvent event) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

         //         Toast.makeText(PostingActivity.this, event.message, Toast.LENGTH_SHORT).show();

            }

        });


    }


}
