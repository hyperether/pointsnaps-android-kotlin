package com.hyperether.pointsnaps.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*


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

        fun getFileFromUri(uri: Uri, context: Context): File {
            val file = createImageFile(context)
            val inputStream: InputStream = context.getContentResolver().openInputStream(uri)!!
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            val bos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return file
        }

    }

}