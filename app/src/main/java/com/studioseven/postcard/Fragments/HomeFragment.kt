package com.studioseven.postcard.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
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
import com.studioseven.postcard.Adapters.PostcardAdapter
import com.studioseven.postcard.Constants
import com.studioseven.postcard.Models.Image
import com.studioseven.postcard.Models.Postcard
import com.studioseven.postcard.Network.RestAPI
import com.studioseven.postcard.R
import com.studioseven.postcard.Utils.LocalStorageHelper
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

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
    private var capsuleMessage: String? = null
    private var capsuleId: String? = null
    private var token: String? = null
    private var errorMsg:String? = null
    private var isCompleted:Boolean = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var locationManager : LocationManager? = null

    lateinit var localStorageHelper: LocalStorageHelper

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("TAG", location.longitude.toString()  + " " + location.latitude.toString())
            Constants.location = location.latitude.toString() + "," + location.longitude.toString()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ActivityCompat.requestPermissions(context as Activity, permissions,0)
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isStoragePermissionGranted()

        locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager?

        localStorageHelper = LocalStorageHelper(context)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewManager = LinearLayoutManager(view.context)

        val images: List<Image> = listOf(
            Image("https://i.pinimg.com/originals/be/86/1b/be861bdfb1a6f38395c426123efa6ee6.jpg"),
            Image("https://images-na.ssl-images-amazon.com/images/I/71Lo6ZgNLrL._SL1200_.jpg"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrAzB3dynTfZ4CioA56_XksdHsXMZUZgv4HfSb5O9js5BBjEix"),
            Image("https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS19FFqjUfKyZnx6K7g_YmnWQqHZ86ZodzbhDgwtQFH2rohNTvE"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFaEOzIwdDMy5O5m7t5qMnQVIbU5WVpfxaD40PoI528PIPOLQQcg")
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
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }

        //fetchFeed()

        //Bottom sheet
        val fab : FloatingActionButton = view?.findViewById(R.id.floating)!!
        fab.setOnClickListener {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            showAlertDialogue()
        }

        return view
    }

    /*fun fetchFeed(){
        RestAPI.getAppService().search(Constants.userId,Constants.token, "1 2 3").
            enqueue(object: Callback<Map<String, Any>>{
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if(response.body()?.get("error") == null){
                        localStorageHelper.updateToken(response.body()?.get("token"))
                        populateUI(response.body()?.get("result"))
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

    }*/

    private fun populateUI(result: Map<String, Any>?) {
        Toast.makeText(context, "Hurray", Toast.LENGTH_SHORT).show()
        for (postcard in result?.values!!){

        }
    }

    private fun showAlertDialogue() {
        val builder = AlertDialog.Builder(context!!)
        var dialogInflater: LayoutInflater = LayoutInflater.from(context)
        var dialogView: View = dialogInflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            var titleEditText: EditText = dialogView.findViewById(R.id.et_title)
            var messageEditText: EditText = dialogView.findViewById(R.id.et_message)
            val newCategory = titleEditText.text
            var isValid = true
            if (newCategory.isBlank() || messageEditText.text.isBlank()) {
                Toast.makeText(context, "Title cannot be left blank", Toast.LENGTH_LONG).show()
                isValid = false
            }
            if (isValid) {
                capsuleTitle = newCategory.toString()
                capsuleMessage = messageEditText.text.toString()
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        (if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val selectedMediaUri: ClipData? = data!!.clipData
            if (selectedMediaUri != null) {
                createCapsule(selectedMediaUri)
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            val imageBitmap: Bitmap = data!!.extras.get("data") as Bitmap
            //makeAPICallImage(imageBitmap)
            if(isExternalStorageWritable()){
                val uri: Uri = saveImage(imageBitmap)
                Log.d("TAG", uri.toString())
                makeAPICallImage(uri)
            }

        })
    }

    fun createCapsule(selectedMediaUri :ClipData){
        Log.d("TAG", Constants.token)
        RestAPI.getAppService().createCapsule(Constants.token, Constants.userId, capsuleTitle!!)
            .enqueue(object : Callback<Map<String, String>> {
                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Log.d("TAG", "Failed")
                }

                override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                    capsuleId = response.body()!!["travelcapsule"]
                    token = response.body()!!["token"]
                    Log.d("TAG", response.body()!!["result"])
                    localStorageHelper.updateToken(token!!)
                    //Log.d("TAG", token)
                    for (i in 0..(selectedMediaUri.itemCount - 1)) {
                        Log.d("TAG", selectedMediaUri.getItemAt(i).uri.toString())
                        if (!selectedMediaUri.getItemAt(i).uri.toString().contains(".mp4")) {
                            //val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, selectedMediaUri.getItemAt(i).uri)
                            makeAPICallImage(selectedMediaUri.getItemAt(i).uri)
                        } else {
                            makeAPICallVideo(selectedMediaUri.getItemAt(i).uri)
                        }
                    }

                }
            })
    }

    // convert all images to byteArrays and send to server
    fun makeAPICallImage(imageUri :Uri){
        doAsync {
            val file = File(imageUri.path)
            Log.d("TAG", file.toString())
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("image/jpeg"),
                         file
             )
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            val tokenRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.token)
            val usernameRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.userId)
            val titleRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, capsuleTitle)
            val messageRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, capsuleMessage)
            val idRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, capsuleId)

            RestAPI.getAppService()
                .postMedia(tokenRequestBody, usernameRequestBody, titleRequestBody, messageRequestBody, body, idRequestBody)
                .enqueue(object : Callback<Map<String, String>> {
                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        Log.d("TAG", t.message)
                    }
                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        capsuleId = response.body()!!["travelcapsule"]
                        token = response.body()!!["token"]
                        Log.d("TAG", response.body().toString())
                    }

                })
            }
    }

    //convert all videos to byteArray and send to server
    fun makeAPICallVideo(uri: Uri) {
        doAsync {

            val file = File(uri.path)
            Log.d("TAG", file.toString())
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("video/*"),
                file
            )
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            val tokenRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.token)
            val usernameRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.userId)
            val titleRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, capsuleTitle)
            val messageRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, capsuleMessage)
            val idRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, capsuleId)

            RestAPI.getAppService()
                .postMedia(tokenRequestBody, usernameRequestBody, titleRequestBody, messageRequestBody, body, idRequestBody)
                .enqueue(object : Callback<Map<String, String>> {
                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        Log.d("TAG", t.message)
                    }
                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        capsuleId = response.body()!!["travelcapsule"]
                        token = response.body()!!["token"]
                        Log.d("TAG", response.body().toString())
                    }

                })
        }


    }


    fun saveImage(finalBitmap: Bitmap): Uri {

        val root: String = Environment.getExternalStorageDirectory().toString()
        val myDir: File = File(root + "/Postcard")
        myDir.mkdirs()

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname: String = "image_"+ timeStamp +".jpg"

        val file: File = File(myDir, fname)
        if (file.exists()) file.delete ()
        try {
            val out: FileOutputStream = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true
        }
        return false
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
