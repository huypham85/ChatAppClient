package com.vn.chat_app_client.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.vn.chat_app_client.R

object LoadingUtils {

    private var dialog: Dialog? = null
    fun showLoading(context: Context, text: String? = null, cancelable: Boolean = false): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_loading_screen)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(cancelable)
        if (text != null) {
            dialog.findViewById<TextView>(R.id.text).text = text
        }
        dialog.show()
        this.dialog = dialog
        return dialog

    }

    fun hideLoading() {
        dialog?.dismiss()
        dialog = null
    }
}