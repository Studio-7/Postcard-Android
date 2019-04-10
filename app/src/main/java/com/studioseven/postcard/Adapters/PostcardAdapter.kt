package com.studioseven.postcard.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studioseven.postcard.Models.Postcard
import com.studioseven.postcard.R
import kotlinx.android.synthetic.main.item_postcard.*
import kotlinx.android.synthetic.main.item_postcard.view.*

class PostcardAdapter(private val myDataset: List<Postcard>, private val context: Context) :
    RecyclerView.Adapter<PostcardAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val userNameTv = view.usernameTv
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PostcardAdapter.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_postcard, parent, false)
        // set the view's size, margins, paddings and layout parameters

        /*imageScroll.layoutManager = GridLayoutManager(this, 2)*/
        view.imageScroll.adapter = ImageAdapter(listOf())
        //view.imageScroll.smoothScrollToPosition(3)

        view.image_heart_white.setOnClickListener{
            view.image_heart_white.visibility = View.INVISIBLE
            view.image_heart_red.visibility = View.VISIBLE
        }
        view.image_heart_red.setOnClickListener{
            view.image_heart_white.visibility = View.VISIBLE
            view.image_heart_red.visibility = View.INVISIBLE
        }

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userNameTv.text = myDataset[position].userName
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
