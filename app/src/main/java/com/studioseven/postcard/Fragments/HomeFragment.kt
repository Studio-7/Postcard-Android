package com.studioseven.postcard.Fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studioseven.postcard.Adapters.ImageAdapter
import com.studioseven.postcard.Models.Image
import com.studioseven.postcard.R
import kotlinx.android.synthetic.main.item_postcard.*
import kotlinx.android.synthetic.main.item_postcard.view.*

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val images: List<Image> = listOf(
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRh8mC7z2hVvA9ljM1NtgyxfROwyTGCcFOKYIXHSGxi__1KjX5m"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrAzB3dynTfZ4CioA56_XksdHsXMZUZgv4HfSb5O9js5BBjEix"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRd1Fh2z2i8_IzYyPhXgWMjaPMPNPcgYOQIMVwGrsRqGA2M1OoTlg"),
            Image("https://www.washingtonpost.com/resizer/oLrhvDHWXoe3ycLXTe0PAcZlejk=/960x0/arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/AWN2OZ75JQYHZOBKZAQL5NROZU.jpg"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS19FFqjUfKyZnx6K7g_YmnWQqHZ86ZodzbhDgwtQFH2rohNTvE"),
            Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRd1Fh2z2i8_IzYyPhXgWMjaPMPNPcgYOQIMVwGrsRqGA2M1OoTlg")
        )

        /*imageScroll.layoutManager = GridLayoutManager(this, 2)*/
        view.imageScroll.adapter = ImageAdapter(images)
        view.imageScroll.smoothScrollToPosition(3)

        view.image_heart_white.setOnClickListener{
            image_heart_white.visibility = View.INVISIBLE
            image_heart_red.visibility = View.VISIBLE
        }
        view.image_heart_red.setOnClickListener{
            image_heart_white.visibility = View.VISIBLE
            image_heart_red.visibility = View.INVISIBLE
        }

        return view
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
