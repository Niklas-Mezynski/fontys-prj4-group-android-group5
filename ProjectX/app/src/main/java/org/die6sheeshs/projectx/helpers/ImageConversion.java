package org.die6sheeshs.projectx.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import io.reactivex.schedulers.Schedulers;

public class ImageConversion {

    private static final int QUALITY = 85;

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
        return Base64.encodeToString(compressToJPEG(bytes, 1080, false), Base64.DEFAULT);
    }

    public static String fileToBase64(File file, boolean makePortraitToSquareImage) {
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
        return Base64.encodeToString(compressToJPEG(bytes, 1080, makePortraitToSquareImage), Base64.DEFAULT);
    }

    private static byte[] compressToJPEG(byte[] input, int maxLandcapeHeight, boolean portraitToSquareImg){
        Bitmap bmpLarge = BitmapFactory.decodeByteArray(input, 0, input.length);
        double factor = 1;
        if(bmpLarge.getWidth() > bmpLarge.getHeight() ){//landscape picture
            if(bmpLarge.getHeight() > maxLandcapeHeight){
                factor = maxLandcapeHeight*1.00/bmpLarge.getHeight();
            }
        }else{// portrait picture
            if(bmpLarge.getWidth() > maxLandcapeHeight){
                factor = maxLandcapeHeight*1.00/bmpLarge.getWidth();
            }
        }
        Bitmap bmp;
        if(factor < 1){//downscale img to maxLandcapeHeight
            int calcWidth = (int)(bmpLarge.getWidth()*factor);
            int calcHeight = (int) (bmpLarge.getHeight()*factor);
            bmp = Bitmap.createScaledBitmap(bmpLarge, calcWidth, calcHeight, false);
        }else{
            bmp = bmpLarge;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}
