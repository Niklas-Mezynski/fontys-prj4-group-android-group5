package org.die6sheeshs.projectx.helpers;

import android.text.Editable;
import android.text.TextWatcher;

@FunctionalInterface
public interface CustomTextWatcher extends TextWatcher {
    default public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    default public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    public void afterTextChanged(Editable editable);
}
