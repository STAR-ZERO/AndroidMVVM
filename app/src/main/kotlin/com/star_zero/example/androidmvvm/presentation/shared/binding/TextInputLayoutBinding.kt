package com.star_zero.example.androidmvvm.presentation.shared.binding

import android.databinding.BindingAdapter
import android.support.design.widget.TextInputLayout

object TextInputLayoutBinding {

    @JvmStatic
    @BindingAdapter("errorMessage")
    fun setErrorMessage(view: TextInputLayout, messageId: Int) {
        if (messageId == 0) {
            view.isErrorEnabled = false
        } else {
            view.isErrorEnabled = true
            view.error = view.context.getString(messageId)
        }
    }
}
