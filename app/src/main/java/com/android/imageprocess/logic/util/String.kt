package com.android.imageprocess.logic.util

import android.widget.Toast
import com.android.imageprocess.ImageProcessApplication
import java.time.Duration

fun String.showToast (duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(ImageProcessApplication.context,this,duration).show()
}