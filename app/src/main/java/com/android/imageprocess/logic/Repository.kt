package com.android.imageprocess.logic

import com.android.imageprocess.logic.dao.UserDao
import com.android.imageprocess.logic.model.User

object Repository {
    fun saveUser(user: User) = UserDao.saveUser(user)

    fun getSavedUser() = UserDao.getSavedUser()

    fun isUserSaved() = UserDao.isUserSaved()


}