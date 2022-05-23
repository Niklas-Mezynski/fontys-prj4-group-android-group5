package org.die6sheeshs.projectx.helpers;

import android.content.Context;
import android.widget.Toast;

public abstract class Toaster {
    public static void makeToast(Context c , String text){

        int duration = Toast.LENGTH_SHORT;

        Toast t = Toast.makeText(c, text, duration);
        t.show();
    }

    public static void makeToast(Context c , String text, int duration){

        Toast t = Toast.makeText(c, text, duration);
        t.show();
    }
}
