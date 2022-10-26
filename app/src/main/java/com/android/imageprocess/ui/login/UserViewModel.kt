package com.android.imageprocess.ui.login

import androidx.lifecycle.ViewModel
import com.android.imageprocess.logic.Repository
import com.android.imageprocess.logic.model.User

class UserViewModel : ViewModel() {
    fun saveUser(user: User) = Repository.saveUser(user)

    fun getSavedUser() = Repository.getSavedUser()

    fun isUserSaved() = Repository.isUserSaved()
}