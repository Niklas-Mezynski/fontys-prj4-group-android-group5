package org.die6sheeshs.projectx.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import io.reactivex.schedulers.Schedulers;

public class ImageConversion {
    public static Bitmap base64ToBitmap(String base64) {
        //Convert base64 string into a byte array and then into a bitmap in order to set it to the imageView
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    public static String fileToBase64(File file) {
        //Convert base64 string into a byte array and then into a bitmap in order to set it to the imageView
        byte[] bytes;
        //Converting it into a byte array
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            Log.e("File upload", e.getMessage());
            return null;
        }
        //Encode to base64 and send it to the restAPI
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
