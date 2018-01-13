package com.hocrox.digitalviewfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hocrox.digitalviewfinder.datacontrol.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class GetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

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


    /*public MessageEvent onEvent() {

        return new MessageEvent("Hello everyone!");
    }
*/

    @Subscribe
    public void onEvent(final MessageEvent event) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(GetActivity.this, event.message, Toast.LENGTH_SHORT).show();

            }

        });


    }


}
