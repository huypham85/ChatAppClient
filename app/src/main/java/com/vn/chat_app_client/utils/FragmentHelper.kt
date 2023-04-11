package com.vn.chat_app_client.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

@SuppressLint("ClickableViewAccessibility")
fun Fragment.hideKeyboardOnClickOutside(view: View) {
    if (view !is EditText) {
        view.setOnTouchListener { _, _ ->
            activity?.hideKeyboard()
            false
        }
    }

    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            hideKeyboardOnClickOutside(child)
        }
    }
}