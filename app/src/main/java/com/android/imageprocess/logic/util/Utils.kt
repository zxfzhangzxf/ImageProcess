package com.android.imageprocess.logic.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.currentCoroutineContext
import java.sql.Date
import java.text.SimpleDateFormat

fun rotateBitmap(bitmap: Bitmap,degree : Int) : Bitmap{
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    bitmap.recycle()
    return rotatedBitmap
}


@SuppressLint("SimpleDateFormat")
fun getCurrentTime() : String{
    val format = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
    val nowTime = Date(System.currentTimeMillis())
    val time = format.format(nowTime)
    return time
}
