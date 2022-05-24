package org.die6sheeshs.projectx.helpers;

import android.content.Context;
import android.util.TypedValue;

import org.die6sheeshs.projectx.R;

public class GeneralHelperMethods {
    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, value, true);
        return value.data;
    }
}
