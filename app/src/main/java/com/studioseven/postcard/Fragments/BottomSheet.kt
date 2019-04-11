package com.studioseven.postcard.Fragments

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import java.io.File
import java.util.jar.Manifest


class BottomSheet: BottomSheetDialogFragment() {

    private lateinit var  pFragment :Fragment

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(com.studioseven.postcard.R.layout.bottom_sheet, container, false)


        view.gallery.setOnClickListener {
            Log.d("TAG", "CLICKED")
            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/* video/*"
            pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            pickIntent.action = Intent.ACTION_GET_CONTENT
            pFragment.startActivityForResult(pickIntent, 1)
            dismiss()
        }
        view.camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(pFragment.context!!, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(pFragment.activity!!,  arrayOf(android.Manifest.permission.CAMERA), 2);
            } else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                pFragment.startActivityForResult(cameraIntent, 2)
            }
            dismiss()
        }


        return view
    }

    fun setParentFragment(fragment: Fragment){
        this.pFragment = fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}