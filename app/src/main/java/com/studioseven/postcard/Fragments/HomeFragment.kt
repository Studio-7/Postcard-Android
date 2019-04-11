package com.studioseven.postcard.Fragments

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.studioseven.postcard.Adapters.ImageAdapter
import com.studioseven.postcard.Adapters.PostcardAdapter
import com.studioseven.postcard.Models.Image
import com.studioseven.postcard.Models.Postcard
import com.studioseven.postcard.Network.RestAPI
import com.studioseven.postcard.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_postcard.*
import kotlinx.android.synthetic.main.item_postcard.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var capsuleTitle: String? = null
    private var capsuleId: String? = null
    private var token: String? = null
    private var errorMsg:String? = null
    private var isCompleted:Boolean = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        /*Picasso.get().load("https://ichef.bbci.co.uk/news/660/cpsprodpb/E9DF/production/_96317895_gettyimages-164067218.jpg")
            .into(postImage)*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isStoragePermissionGranted()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewManager = LinearLayoutManager(view.context)

        val images: List<Image> = listOf(
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrAzB3dynTfZ4CioA56_XksdHsXMZUZgv4HfSb5O9js5BBjEix"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRh8mC7z2hVvA9ljM1NtgyxfROwyTGCcFOKYIXHSGxi__1KjX5m"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRd1Fh2z2i8_IzYyPhXgWMjaPMPNPcgYOQIMVwGrsRqGA2M1OoTlg"),
            Image("https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS19FFqjUfKyZnx6K7g_YmnWQqHZ86ZodzbhDgwtQFH2rohNTvE"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRd1Fh2z2i8_IzYyPhXgWMjaPMPNPcgYOQIMVwGrsRqGA2M1OoTlg")
        )

        viewAdapter = PostcardAdapter(
            listOf(Postcard("abhishek", "Kanchi", 123, 30,
                    listOf("Hariharan", "Arko"),"2 days ago",
                false, images, "https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"),
                    Postcard("arko", "Kanchi", 123, 30,
                    listOf("Hariharan", "Arko"),"2 days ago",
                false, images, "https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"))
            , view.context)

        recyclerView = view.postcardRv.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
        //Bottom sheet
        var fab : FloatingActionButton = view?.findViewById(R.id.floating)!!
        fab.setOnClickListener {
            var builder = AlertDialog.Builder(context!!)
            builder.setTitle("Capsule Title: " )
            builder.setMessage("Enter the title for your travel capsule")
            var input: EditText = EditText(context)
            var lp: LinearLayout.LayoutParams  = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
            input.setLayoutParams(lp)
            builder.setView(input)
            builder.setCancelable(false)
            builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
                val newCategory = input.text
                var isValid = true
                if (newCategory.isBlank()) {
                    Toast.makeText(context, "Title cannot be left blank", Toast.LENGTH_LONG).show()
                    isValid = false
                }
                if (isValid) {
                    capsuleTitle = newCategory.toString()
                    val bottomSheet = BottomSheet()
                    bottomSheet.setParentFragment(this)
                    bottomSheet.show(fragmentManager, "bottomsheet")
                }
                if (isValid) {
                    dialog.dismiss()
                }
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
                dialog.cancel()
            }

            builder.show()
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        (if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val selectedMediaUri: ClipData? = data!!.clipData
            if (selectedMediaUri != null) {
                createCapsule()
                for (i in 0..(selectedMediaUri.itemCount - 1)) {
                    Log.d("TAG", selectedMediaUri.getItemAt(i).uri.toString())
                    if (!selectedMediaUri.getItemAt(i).uri.toString().contains(".mp4")) {
                        val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, selectedMediaUri.getItemAt(i).uri)
                        makeAPICallImage(bitmap)
                    } else {
                        makeAPICallVideo(selectedMediaUri.getItemAt(i).uri)
                    }
                }

            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            var imageBitmap: Bitmap = data!!.extras.get("data") as Bitmap
            makeAPICallImage(imageBitmap)

        })
    }

    fun createCapsule(){
        RestAPI.getAppService().createCapsule("am9obndpY2s=.SXhDWlhnTXhteEx5QVRIT21JTXhCUGZsem5Genp6bm4=.OEQvMTlUY0QyMmkvaXo4NWZIbjdsYnFqaTVTV0RZOFY2b0VrWUFhd0RkYz0=", "johnwick", capsuleTitle!!)
            .enqueue(object : Callback<Map<String, String>> {
                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Log.d("TAG", "Failed")
                }

                override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                    capsuleId = response.body()!!["travelcapsule"]
                    token = response.body()!!["token"]
                    Log.d("TAG", response.body().toString())
                }
            })
    }

    // convert all images to byteArrays and send to server
    fun makeAPICallImage(image: Bitmap){
        doAsync {
            var stream: ByteArrayOutputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var byteArray: ByteArray = stream.toByteArray()
            image.recycle()
            onComplete {
                Log.d("TAG", byteArray.toString())
                //make API call
            }

        }
    }

    //convert all videos to byteArray and send to server
    fun makeAPICallVideo(uri: Uri) {
        doAsync {
            var videoBytes: ByteArray? = null
            var outputStream = ByteArrayOutputStream()
            var file: File = File(uri.path)
            var fileStream = FileInputStream(file)
            var n: Int
            var buffer: ByteArray = ByteArray(1024)
            n = fileStream.read(buffer)
            while(n != -1){
                outputStream.write(buffer, 0, n)
                n = fileStream.read(buffer)
            }
            videoBytes = outputStream.toByteArray()

            onComplete {
                Log.d("TAG", videoBytes.toString())
                //make API call video
            }
        }


    }


    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted")
                return true
            } else {

                Log.v("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 3)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted")
            return true
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }*/

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
