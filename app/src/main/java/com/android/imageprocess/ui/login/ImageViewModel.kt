package com.android.imageprocess.ui.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.android.imageprocess.logic.model.Background
import java.io.File

class ImageViewModel : ViewModel() {
    val TAKE_PHOTO = 1
    val OPEN_ALBUM = 2

    val backgroundList = ArrayList<Background>()

    lateinit var imageUri: Uri
    lateinit var outputImage:File
    lateinit var imageName:String

}