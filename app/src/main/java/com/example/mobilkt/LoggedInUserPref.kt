package com.example.mobilkt

import android.content.Context
import com.example.mobilkt.data.GroupEnum
import com.example.mobilkt.data.model.LoggedInUser

class LoggedInUserPref(context: Context) {
    companion object{
        const val SP_NAME = "loggedinuser_pref"
        const val userId = "id"
        const val displayName = "Nama User"
        const val group = "USER"
    }

    val preference = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun setLoggedInUser(user : LoggedInUser) {
        val prefEditor = preference.edit()
        prefEditor.putString(userId, user.userId)
        prefEditor.putString(group, user.group)
        user.displayName.let { prefEditor.putString(displayName, user.displayName) }
        prefEditor.apply()
    }

    fun getLoggedInUser() : LoggedInUser {
        val user = LoggedInUser(userId, displayName, GroupEnum.USER.toString())
        user.userId = preference.getString(userId, "")
        user.displayName = preference.getString(displayName, "")
        user.group = preference.getString(group, GroupEnum.USER.toString())
        return user
    }

    fun removeLoggedInUser() {
        val prefEditor = preference.edit()
        prefEditor.remove(userId)
        prefEditor.remove(displayName)
        prefEditor.remove(group)
        prefEditor.apply()
    }
}