package com.android.imageprocess.ui.login

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.imageprocess.logic.model.Background
import java.io.File

class ImageViewModel : ViewModel() {
    val TAKE_PHOTO = 1
    val OPEN_ALBUM = 2

    val backgroundColor : LiveData<Int>
        get() = _backgroundColor

    private val _backgroundColor = MutableLiveData<Int>()

    val backgroundList = ArrayList<Background>()

    lateinit var imageUri: Uri
    lateinit var outputImage:File
    lateinit var imageName:String

    fun setBackgroundColor(color:Int) {
        _backgroundColor.value = color
    }

}