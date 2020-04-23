package com.hyperether.pointsnaps.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException

class Utils() {
    companion object {

        @Throws(IOException::class)
        fun createImageFile(context: Context): File {
            // Create an image file name
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile(
                "image", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )
        }
    }

}