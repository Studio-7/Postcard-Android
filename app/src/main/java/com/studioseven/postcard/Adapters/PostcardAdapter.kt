package com.studioseven.postcard.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.studioseven.postcard.Activities.ImageScreenActivity
import com.studioseven.postcard.Models.Postcard
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.item_postcard.view.*


class PostcardAdapter(private val postcardList: List<Postcard>, private val context: Context) :
    RecyclerView.Adapter<PostcardAdapter.MyViewHolder>(){

    var currentPos: Int = 0

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val userNameTv = view.usernameTv
        val locationTv = view.locationTv
        val userProfileImage = view.profile_photo
        val postImage = view.postImage
        val imageScroll = view.imageScroll
        val image_heart_white = view.image_heart_white
        val image_heart_red = view.image_heart_red
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostcardAdapter.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(com.studioseven.postcard.R.layout.item_postcard, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        currentPos = position

        holder.userNameTv.text = postcardList[position].userName
        holder.locationTv.text = postcardList[position].location

        Picasso.get().load(postcardList[position].userImageLink).into(holder.userProfileImage)
        holder.postImage.setOnClickListener{
            val myIntent = Intent(context, ImageScreenActivity::class.java)
            myIntent.putExtra("imageUrl", postcardList[position].mediaLinkList[holder.imageScroll.currentItem].url) //Optional parameters
            context.startActivity(myIntent)
        }

        holder.imageScroll.adapter = ImageAdapter(postcardList[position].mediaLinkList)
        holder.imageScroll.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.02f)
                .setMinScale(0.9f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build()
        )

        Picasso.get().load(postcardList[position].mediaLinkList[holder.imageScroll.currentItem].url)
            .into(holder.postImage)



        /*holder.imageScroll.addOnItemChangedListener{
            Picasso.get().load(postcardList[position].mediaLinkList[holder.imageScroll.currentItem].url)
                .into(holder.postImage)
        }*/

        holder.image_heart_white.setOnClickListener {
            holder.image_heart_white.visibility = View.INVISIBLE
            holder.image_heart_red.visibility = View.VISIBLE
        }
        holder.image_heart_red.setOnClickListener {
            holder.image_heart_white.visibility = View.VISIBLE
            holder.image_heart_red.visibility = View.INVISIBLE
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = postcardList.size

}
