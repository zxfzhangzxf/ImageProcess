package com.android.imageprocess

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.N
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.imageprocess.databinding.ActivityCameraAlbumBinding
import com.android.imageprocess.logic.util.rotateBitmap
import com.android.imageprocess.ui.login.ImageViewModel
import java.io.File

class CameraAlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraAlbumBinding

    val viewModel by lazy { ViewModelProvider(this).get(ImageViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val UserAccount = intent.getStringExtra("UserAccount")
        //设置导航栏
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        binding.navView.setCheckedItem(R.id.navCall)
        binding.navView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawers()
            true
        }
        //打开相机拍照按钮的监听
        binding.btnTakephoto.setOnClickListener {
            viewModel.outputImage = File(externalCacheDir, "output_image.jpg")
            if (viewModel.outputImage.exists()) {
                viewModel.outputImage.delete()
            }
            viewModel.outputImage.createNewFile()
            viewModel.imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    this, "com.android.imageprocess.fileprovider", viewModel.outputImage
                )
            } else {
                Uri.fromFile(viewModel.outputImage)
            }
            //启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.imageUri)
            startActivityForResult(intent, viewModel.TAKE_PHOTO)
        }
        //点开相册选择图片
        binding.btnOpenAlbum.setOnClickListener {
            //打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //指定只选择图片
            intent.type = "image/*"
            startActivityForResult(intent,viewModel.OPEN_ALBUM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            viewModel.TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    //将拍摄的照片显示出来
                    val bitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(viewModel.imageUri)
                    )
                    binding.ivShow.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
            viewModel.OPEN_ALBUM -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        //将选择的图片显示
                        val bitmap = getBitmapFromUri(uri)
                        binding.ivShow.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap? {
        val exif = ExifInterface(viewModel.outputImage.path)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }


    //处理导航视图的选项点击，以及导航视图的弹出点击
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.backup -> Toast.makeText(this, "backup btn", Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "delete btn", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "settings btn", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}