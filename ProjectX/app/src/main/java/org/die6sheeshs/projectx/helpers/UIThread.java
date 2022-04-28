package org.die6sheeshs.projectx.helpers;

import android.os.Handler;
import android.os.Looper;

public abstract class UIThread {
    public static void runOnUiThread(Runnable r){
        new Handler(Looper.getMainLooper()).post(r);
    }
}
