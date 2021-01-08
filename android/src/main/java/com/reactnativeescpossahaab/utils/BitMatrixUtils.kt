package com.reactnativeescpossahaab.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.common.BitMatrix

// @ref https://gist.github.com/adrianoluis/fa9374d7f2f8ca1115b00cc83cd7aacd
object BitMatrixUtils {
    fun convertToBitmap(data: BitMatrix): Bitmap {
        val w: Int = data.getWidth()
        val h: Int = data.getHeight()
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset: Int = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (data.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}