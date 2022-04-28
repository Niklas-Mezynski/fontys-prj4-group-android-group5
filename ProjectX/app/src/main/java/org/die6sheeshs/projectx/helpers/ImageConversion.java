package org.die6sheeshs.projectx.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
//import android.media.ExifInterface;
import androidx.exifinterface.media.ExifInterface;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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
        //Encode to base64 and send it to the restAPI
        return Base64.encodeToString(compressToJPEG(rotateImageToExifData(file), 1080, false), Base64.DEFAULT);
    }



    private static byte[] compressToJPEG(Bitmap bmpLarge, int maxLandcapeHeight, boolean portraitToSquareImg){
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

    private static Bitmap fileToBitmap(File file){
        byte[] bytes;
        //Converting it into a byte array
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            Log.e("File upload", e.getMessage());
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap rotateImageToExifData(File image){
        Bitmap bmp = fileToBitmap(image);
        Bitmap rotatedBitmap = null;
            try {
                ExifInterface imageData = new ExifInterface(image.getPath());
                int orientation = imageData.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bmp, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bmp, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bmp, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bmp;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return rotatedBitmap;


    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
