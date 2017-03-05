package com.star_zero.example.androidmvvm.presentation.shared.binding;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;

public class TextInputLayoutBinding {

    @BindingAdapter({"errorMessage"})
    public static void setErrorMessage(TextInputLayout view, int messageId) {
        if (messageId == 0) {
            view.setErrorEnabled(false);
        } else {
            view.setErrorEnabled(true);
            view.setError(view.getContext().getString(messageId));
        }
    }
}
