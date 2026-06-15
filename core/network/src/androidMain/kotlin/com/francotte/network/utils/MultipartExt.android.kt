package com.francotte.network.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.graphics.scale
import com.francotte.network.model.ImageUpload
import java.io.ByteArrayOutputStream

/**
 * Reads the image pointed to by [this] Uri, resizes it (max 800px on the largest side)
 * and returns a platform-agnostic [ImageUpload] usable by the Ktor APIs in commonMain.
 */
fun Uri?.toImageUpload(context: Context): ImageUpload? =
    this?.let { uri ->
        val resolver = context.contentResolver
        val originalBitmap = resolver.openInputStream(uri).use { BitmapFactory.decodeStream(it) } ?: return null
        val maxSize = 800
        val scale = minOf(maxSize / originalBitmap.width.toFloat(), maxSize / originalBitmap.height.toFloat(), 1f)
        val resizedBitmap = originalBitmap.scale(
            (originalBitmap.width * scale).toInt(),
            (originalBitmap.height * scale).toInt(),
        )
        val bytes = ByteArrayOutputStream().use { out ->
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.toByteArray()
        }
        ImageUpload(bytes = bytes, fileName = "upload.jpg", contentType = "image/jpeg")
    }
