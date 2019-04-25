package com.studioseven.postcard.Utils

import android.content.Context
import com.studioseven.postcard.Constants
import com.studioseven.postcard.R

class LocalStorageHelper(var context: Context?) {

    fun saveToProfile(key: String, value: String?) {
        val sharedPref = context!!.getSharedPreferences(context!!.getString(R.string.profile_file_key), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getFromProfile(key: String, default: String): String? {
        val sharedPref = context!!.getSharedPreferences(context!!.getString(R.string.profile_file_key), Context.MODE_PRIVATE)
        val highScore = sharedPref.getString(key, default)
        return highScore
    }

    fun updateToken(token: String?) {
        Constants.token = token
        saveToProfile(context!!.getString(R.string.token_key), token)
    }

    fun getToken() : String?{
        return getFromProfile(context!!.getString(R.string.token_key), "")
    }

    fun updateFname(fname: String) {
        saveToProfile(context!!.getString(R.string.fname_key), fname)
    }

    fun getFname() {
        getFromProfile(context!!.getString(R.string.fname_key), "")
    }

    fun updateLname(fname: String) {
        saveToProfile(context!!.getString(R.string.lname_key), fname)
    }

    fun getLname() {
        getFromProfile(context!!.getString(R.string.lname_key), "")
    }

    fun updateEmail(email: String) {
        Constants.email = email
        saveToProfile(context!!.getString(R.string.email_key), email)
    }

    fun getEmail() {
        getFromProfile(context!!.getString(R.string.email_key), "")
    }

    fun updateUserId(userId: String) {
        Constants.userId = userId
        saveToProfile(context!!.getString(R.string.userid_key), userId)
    }

    fun getUserId() {
        getFromProfile(context!!.getString(R.string.userid_key), "")
    }
}