package com.studioseven.postcard.Models

import android.content.Context
import android.widget.Toast
import java.util.*

class Post(
    var url: String,
    var hashtags: ArrayList<*>,
    var message: String) {

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
