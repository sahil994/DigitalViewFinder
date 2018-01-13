package com.hocrox.digitalviewfinder.DataTransferControl;

import android.graphics.Bitmap;

public class MyEventModel {
public Bitmap getBitmap() {
    return bitmap;
}

Bitmap bitmap;
String name;



public String getName() {
    return name;
}


public MyEventModel(Bitmap bitmap, String name) {

    this.bitmap = bitmap;
    this.name = name;
}

public MyEventModel(Bitmap bitmap) {

    this.bitmap = bitmap;
}

}
