package com.android.imageprocess.ui.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import java.io.File

class ImageViewModel : ViewModel() {
    val TAKE_PHOTO = 1
    val OPEN_ALBUM = 2

    lateinit var imageUri: Uri
    lateinit var outputImage:File

}