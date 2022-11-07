package com.vn.chat_app_client.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

object URIPathHelper {

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } catch (e: Exception) {
            Log.e(TAG, "getRealPathFromURI Exception : $e")
            ""
        } finally {
            cursor?.close()
        }
    }
}