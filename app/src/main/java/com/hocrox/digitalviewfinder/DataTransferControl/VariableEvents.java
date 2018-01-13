package com.hocrox.digitalviewfinder.DataTransferControl;

import android.graphics.Bitmap;

public class VariableEvents {

    public Bitmap getBitmap() {
        return bitmap;
    }

    Bitmap bitmap;
    String name;



    public String getName() {
        return name;
    }


    public VariableEvents(Bitmap bitmap, String name) {

        this.bitmap = bitmap;
        this.name = name;
    }



}