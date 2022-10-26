package com.android.imageprocess.logic.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri

fun rotateBitmap(bitmap: Bitmap,degree : Int) : Bitmap{
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    bitmap.recycle()
    return rotatedBitmap
}

