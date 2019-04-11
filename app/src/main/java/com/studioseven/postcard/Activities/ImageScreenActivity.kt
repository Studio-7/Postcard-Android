package com.studioseven.postcard.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_screen.*


class ImageScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.studioseven.postcard.R.layout.activity_image_screen)
        val intent = intent
        val value = intent.getStringExtra("imageUrl")
        Picasso.get().load(value).into(image)
    }
}
