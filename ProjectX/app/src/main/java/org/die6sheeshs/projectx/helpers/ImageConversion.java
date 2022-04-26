package org.die6sheeshs.projectx.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import io.reactivex.schedulers.Schedulers;

public class ImageConversion {
    public static Bitmap base64ToBitmap(String base64) {
        //Convert base64 string into a byte array and then into a bitmap in order to set it to the imageView
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }


}
