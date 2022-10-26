package com.android.imageprocess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.android.imageprocess.databinding.ActivityMainBinding
import com.android.imageprocess.logic.model.User
import com.android.imageprocess.logic.util.showToast
import com.android.imageprocess.ui.login.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel by lazy { ViewModelProvider(this).get(UserViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.isUserSaved()) {
            val user = viewModel.getSavedUser()
            val intent = Intent(this,CameraAlbumActivity::class.java).apply {
                putExtra("UserAccount",user.account)
            }
            startActivity(intent)
            this?.finish()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etAccount.text.isEmpty()) {
                "请先输入账号".showToast()
            }
            if (binding.etAccount.text.isNotEmpty() && binding.etPassword.text.isEmpty()) {
                "请输入密码".showToast()
            }

            if (binding.etAccount.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()) {
                if (binding.cbRemember.isChecked) {
                    val user = User(binding.etAccount.text.toString(),binding.etPassword.text.toString())
                    viewModel.saveUser(user)
                }

                val intent = Intent(this,CameraAlbumActivity::class.java).apply {
                    putExtra("UserAccount",binding.etAccount.text.toString())
                }
                startActivity(intent)
                this.finish()
                return@setOnClickListener
            }
        }
    }
}