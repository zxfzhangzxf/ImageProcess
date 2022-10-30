package com.android.imageprocess

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Build.VERSION_CODES.N
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.imageprocess.databinding.ActivityCameraAlbumBinding
import com.android.imageprocess.logic.model.Background
import com.android.imageprocess.logic.util.getCurrentTime
import com.android.imageprocess.logic.util.rotateBitmap
import com.android.imageprocess.ui.camalb.BackgroundAdapter
import com.android.imageprocess.ui.login.ImageViewModel
import java.io.File

class CameraAlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraAlbumBinding

    private lateinit var adapter: BackgroundAdapter

    val viewModel by lazy { ViewModelProvider(this).get(ImageViewModel::class.java) }

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initBackgrounds()
        val layoutManager = LinearLayoutManager(this)
        binding.rvBackground.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        adapter = BackgroundAdapter(this,viewModel.backgroundList)
        binding.rvBackground.adapter = adapter
        //获取用户名称
        val UserAccount = intent.getStringExtra("UserAccount")
        //监听背景颜色变化
        viewModel.backgroundColor.observe(this, Observer { color->
            Log.d("cococo",color.toString())
            binding.relativelayout.background = this.getDrawable(color)
        })
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
            //val fileName = (0 until 1000).random().toString() + (0 until 1000).random().toString() + "_image.jpg"
            viewModel.imageName = getCurrentTime() + "_image.jpg"
            Log.d("CameraAlbumActivity",viewModel.imageName)
            viewModel.outputImage = File(externalCacheDir, viewModel.imageName)
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
                    //将拍摄图片保存至系统图库
                    MediaStore.Images.Media.insertImage(contentResolver,bitmap,viewModel.imageName,null)
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
            R.id.settings -> {
                Toast.makeText(this, "settings btn", Toast.LENGTH_SHORT).show()
                binding.llBackground.visibility = View.VISIBLE
            }
        }
        return true
    }

    //初始化背景颜色列表
    private fun initBackgrounds() {
        viewModel.backgroundList.clear()
        val backgrounds = mutableListOf(
            Background("淡黄",R.drawable.bg_paleyellow),
            Background("象牙",R.drawable.bg_ivory),
            Background("粉红",R.drawable.bg_pink),
            Background("银灰",R.drawable.bg_silivergrey),
            Background("灰色",R.drawable.bg_grey),
            Background("淡蓝",R.drawable.bg_paleblue),
            Background("亮蓝",R.drawable.bg_lightblue),
            Background("棕色",R.drawable.bg_brown),
            Background("淡紫",R.drawable.bg_palepurple),
            Background("瑰红",R.drawable.bg_rosered),
            Background("碧绿",R.drawable.bg_verdure),
            Background("弱绿",R.drawable.bg_palegreen),)

        viewModel.backgroundList.addAll(backgrounds)
    }


}