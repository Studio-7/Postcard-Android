package com.studioseven.postcard.Fragments

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
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.studioseven.postcard.Adapters.PostcardAdapter
import com.studioseven.postcard.Constants
import com.studioseven.postcard.Models.Image
import com.studioseven.postcard.Models.Postcard
import com.studioseven.postcard.Network.RestAPI
import com.studioseven.postcard.R
import com.studioseven.postcard.Utils.LocalStorageHelper
import kotlinx.android.synthetic.main.fragment_home.*
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


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var postCardList: List<Postcard> = listOf()

    private var locationManager : LocationManager? = null

    lateinit var localStorageHelper: LocalStorageHelper

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("TAG", location.longitude.toString()  + " " + location.latitude.toString())
            Constants.location = location.longitude.toString() + "," + location.latitude.toString()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

        view.postcardShimmer.visibility = View.VISIBLE

        /*val images: List<Image> = listOf(
            Image("https://i.pinimg.com/originals/be/86/1b/be861bdfb1a6f38395c426123efa6ee6.jpg"),
            Image("https://images-na.ssl-images-amazon.com/images/I/71Lo6ZgNLrL._SL1200_.jpg"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrAzB3dynTfZ4CioA56_XksdHsXMZUZgv4HfSb5O9js5BBjEix"),
            Image("https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS19FFqjUfKyZnx6K7g_YmnWQqHZ86ZodzbhDgwtQFH2rohNTvE"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFaEOzIwdDMy5O5m7t5qMnQVIbU5WVpfxaD40PoI528PIPOLQQcg")
        )

        postCardList = listOf(Postcard("abhishek", "Kanchi", "123",
            listOf("Hariharan", "Arko"),"2 days ago",
            false, images, "https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"),
            Postcard("abhishek", "Kanchi", "123",
                listOf("Hariharan", "Arko"),"2 days ago",
                false, images, "https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"))*/

        fetchFeed(view)

        //fetch details of previous capsule
        capsuleId = localStorageHelper.getFromProfile(context!!.getString(R.string.prevCapsuleId), null)
        capsuleTitle = localStorageHelper.getFromProfile(context!!.getString(R.string.prevCapsuleTitle), null)
        capsuleMessage = localStorageHelper.getFromProfile(context!!.getString(R.string.prevCapsuleMessage), null)
        Log.d("TAG", "$capsuleId $capsuleTitle $capsuleMessage")

        //Bottom sheet
        val fab : FloatingActionButton = view?.findViewById(R.id.floating)!!
        fab.setOnClickListener {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            //showAlertDialogue()
            capsuleId = localStorageHelper.getFromProfile(context!!.getString(R.string.prevCapsuleId), null)
            if(capsuleId == null){
                //if no previous capsule is created then always create a new capsule
                showAlertDialogue()
            } else {
                //if previous capsule exists then give option to user wheather to upload to new or existing capsule
                showDialog()
            }
        }

        return view
    }

    private fun fetchFeed(view: View) {
        RestAPI.getAppService().getFeed(Constants.userId,Constants.token).
            enqueue(object: Callback<Map<String, Any>>{
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if(response.body()?.get("error") == null){
                        localStorageHelper.updateToken(response.body()?.get("token"))
                        populateUI(response.body()?.get("result"),0, view)
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(context, "Feed fetch failed", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun populateUI(result: Collection<Map<String, Any>>?, i: Int, view: View) {

        if(result == null){
            postcardShimmer.visibility = View.GONE
            Toast.makeText(context, "No posts to show :(", Toast.LENGTH_SHORT).show()
            return
        }
        if(i == result.size){
            setUpRecycler(view)
            return
        }

        val postcard = (result as List<Map<String, Any>>)[i]

        var postList = "" //comma separated list of postIds
        for(post in postcard["Posts"] as Collection<*>){
            postList += (post as String + ",")
        }
        postList.dropLast(1)

        RestAPI.getAppService().getPosts(Constants.userId, Constants.token, postList).enqueue(object: Callback<Map<String, Any>>{
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                localStorageHelper.updateToken(response.body()?.get("token") as String)
                if(response.body()?.get("error") == null){
                    fillPostcardList(postcard, response.body()?.get("result") as Collection<Map<*, *>>)
                    populateUI(result, i+1, view)
                }else Toast.makeText(context, "Post fetch failed", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(context, "Post fetch failed", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun fillPostcardList(postcard: Map<String, Any>, postList: Collection<Map<*, *>>) {
        var images: List<Image> = listOf()

        for(postMap in postList){
            images = images + Image((((postMap["post"] as Map<*, *>)["PostBody"] as Map<*, *>)["Img"] as Map<*, *>)["Link"] as String)
        }

        val pc = Postcard(postcard["CreatedBy"] as String, postcard["Title"] as String, (postcard["Likes"] as Double).toString(),
            listOf("Hariharan", "Arko"),"2 days ago",
            false, images, "https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM")

        postCardList = postCardList + pc
    }

    private fun setUpRecycler(view: View) {
        postcardShimmer.visibility = View.GONE

        viewManager = LinearLayoutManager(view.context)

        viewAdapter = PostcardAdapter(postCardList, view.context)

        recyclerView = view.postcardRv.apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }
    }

    private fun showDialog(){
        val array = arrayOf("Add to existing capsule", "Create new capsule")
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Upload media")
        builder.setItems(array) { _, which ->
            if (which == 0){
                val bottomSheet = BottomSheet()
                bottomSheet.setParentFragment(this)
                bottomSheet.show(fragmentManager, "bottomsheet")
            } else{
                showAlertDialogue()
            }

        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun showAlertDialogue() {
        val builder = AlertDialog.Builder(context!!)
        val dialogInflater: LayoutInflater = LayoutInflater.from(context)
        val dialogView: View = dialogInflater.inflate(R.layout.upload_dialog, null);
        builder.setView(dialogView)
        builder.setCancelable(true)
        val new_capsule: Button=dialogView.findViewById(R.id.new_capsule)
        val existing_capsule: Button=dialogView.findViewById(R.id.existing_capsule)
        val dialog1=builder.show()


            new_capsule.setOnClickListener{
                val builder1 = AlertDialog.Builder(context!!)
                val dialogInflater1: LayoutInflater = LayoutInflater.from(context)
                val dialogView1: View = dialogInflater1.inflate(R.layout.custom_dialog, null);
                builder1.setView(dialogView1)
                builder1.setCancelable(true)

                val ok_button: Button=dialogView1.findViewById(R.id.Ok_button)
                val cancel_button: Button=dialogView1.findViewById(R.id.cancel_button)

                val dialog2=builder1.show()

                ok_button.setOnClickListener {
                    val titleEditText: EditText = dialogView1.findViewById(R.id.et_title)
                    val messageEditText: EditText = dialogView1.findViewById(R.id.et_message)

                    val newCategory = titleEditText.text
                    var isValid = true
                    if (newCategory.isBlank() || messageEditText.text.isBlank()) {
                        Toast.makeText(context, "Title cannot be left blank", Toast.LENGTH_LONG).show()
                        isValid = false
                    }
                    if (isValid) {
                        capsuleTitle = newCategory.toString()
                        capsuleMessage = messageEditText.text.toString()
                        //save capsule title and message to shared preference
                        localStorageHelper.saveToProfile(context!!.getString(R.string.prevCapsuleTitle), capsuleTitle)
                        localStorageHelper.saveToProfile(context!!.getString(R.string.prevCapsuleMessage), capsuleMessage)
                        val bottomSheet = BottomSheet()
                        bottomSheet.setParentFragment(this)
                        bottomSheet.show(fragmentManager, "bottomsheet")
                        dialog1.dismiss()

                    }
                    if (isValid) {
                        dialog2.dismiss()
                    }
                }

               cancel_button.setOnClickListener {
                    dialog2.cancel()
                }
                //builder1.show()

            }

        existing_capsule.setOnClickListener {
            val bottomSheet = BottomSheet()
            bottomSheet.setParentFragment(this)
            bottomSheet.show(fragmentManager, "bottomsheet")
            dialog1.dismiss()
        }

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

    private fun createCapsule(selectedMediaUri :ClipData){
        Log.d("TAG", Constants.token)
        homeProgressBar.visibility = View.VISIBLE
        RestAPI.getAppService().createCapsule(Constants.token, Constants.userId, capsuleTitle!!)
            .enqueue(object : Callback<Map<String, String>> {
                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Log.d("TAG", "Failed")
                }

                override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                    capsuleId = response.body()!!["travelcapsule"]
                    localStorageHelper.saveToProfile(context!!.getString(R.string.prevCapsuleId), capsuleId)
                    token = response.body()!!["token"]
                    Log.d("TAG", capsuleId)
                    localStorageHelper.updateToken(token!!)
                    //Log.d("TAG", token)
                    for (i in 0 until selectedMediaUri.itemCount) {
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
        homeProgressBar.visibility = View.VISIBLE
        doAsync {
            val file = File(imageUri.path)
            Log.d("TAG", file.toString())
            val requestFile: RequestBody = RequestBody.create(
                MediaType.parse("image/jpeg"),
                         file
             )
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.getName(), requestFile)
            val tokenRequestBody = RequestBody.create(MultipartBody.FORM, Constants.token)
            val usernameRequestBody = RequestBody.create(MultipartBody.FORM, Constants.userId)
            val titleRequestBody = RequestBody.create(MultipartBody.FORM, capsuleTitle)
            val messageRequestBody = RequestBody.create(MultipartBody.FORM, capsuleMessage)
            val idRequestBody = RequestBody.create(MultipartBody.FORM, capsuleId)
            val locationRequestBody = RequestBody.create(MultipartBody.FORM, Constants.location)

            RestAPI.getAppService()
                .postMedia(tokenRequestBody, usernameRequestBody, titleRequestBody, messageRequestBody, body, idRequestBody, locationRequestBody)
                .enqueue(object : Callback<Map<String, String>> {
                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        Log.d("TAG", t.message)
                        homeProgressBar.visibility = View.GONE
                        Toast.makeText(context, "Failed Image Upload ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        homeProgressBar.visibility = View.GONE
                        capsuleId = response.body()!!["travelcapsule"]
                        localStorageHelper.saveToProfile(context!!.getString(R.string.prevCapsuleId), capsuleId)
                        token = response.body()!!["token"]
                        localStorageHelper.updateToken(token)

                        Toast.makeText(context, response.body()!!.getValue("result"), Toast.LENGTH_SHORT).show()
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
            val tokenRequestBody = RequestBody.create(MultipartBody.FORM, Constants.token)
            val usernameRequestBody = RequestBody.create(MultipartBody.FORM, Constants.userId)
            val titleRequestBody = RequestBody.create(MultipartBody.FORM, capsuleTitle)
            val messageRequestBody = RequestBody.create(MultipartBody.FORM, capsuleMessage)
            val idRequestBody = RequestBody.create(MultipartBody.FORM, capsuleId)
            val locationRequestBody = RequestBody.create(MultipartBody.FORM, Constants.location)

            RestAPI.getAppService()
                .postMedia(tokenRequestBody, usernameRequestBody, titleRequestBody, messageRequestBody, body, idRequestBody, locationRequestBody)
                .enqueue(object : Callback<Map<String, String>> {
                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        Log.d("TAG", t.message)
                        homeProgressBar.visibility = View.GONE
                        Toast.makeText(context, "Failed Video Upload ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        homeProgressBar.visibility = View.GONE
                        capsuleId = response.body()!!["travelcapsule"]
                        token = response.body()!!["token"]
                        localStorageHelper.updateToken(token)

                        Toast.makeText(context, response.body()!!.getValue("result"), Toast.LENGTH_SHORT).show()
                    }

                })
        }


    }


    @SuppressLint("SimpleDateFormat")
    private fun saveImage(finalBitmap: Bitmap): Uri {

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
    private fun isExternalStorageWritable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true
        }
        return false
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted")
                true
            } else {
                Log.v("TAG", "Permission is revoked")
                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 3)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted")
            true
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


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
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
