package com.android.imageprocess.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.android.imageprocess.ImageProcessApplication
import com.android.imageprocess.logic.model.User
import com.google.gson.Gson

object UserDao {
    private fun sharedPreferences() = ImageProcessApplication.context.
            getSharedPreferences("ImageProcess",Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        sharedPreferences().edit {
            putString("User", Gson().toJson(user))
        }
    }

    fun getSavedUser() : User{
        val userJson = sharedPreferences().getString("User","")
        return Gson().fromJson(userJson,User::class.java)
    }

    fun isUserSaved() = sharedPreferences().contains("User")
}