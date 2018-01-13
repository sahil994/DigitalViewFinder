package com.hocrox.digitalviewfinder.DataTransferControl;

import android.graphics.Bitmap;

public class SendEvents {

    public Bitmap getBitmap() {
        return bitmap;
    }

    Bitmap bitmap;
    String name;



    public String getName() {
        return name;
    }


    public SendEvents(Bitmap bitmap, String name) {

        this.bitmap = bitmap;
        this.name = name;
    }



}