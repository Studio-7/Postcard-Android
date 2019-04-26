package com.studioseven.postcard.Adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.studioseven.postcard.Activities.ImageScreenActivity
import com.studioseven.postcard.Models.Postcard
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.item_postcard.view.*


class PostcardAdapter(private val postcardList: List<Postcard>, private val context: Context?) :
    RecyclerView.Adapter<PostcardAdapter.MyViewHolder>(),
    DiscreteScrollView.OnItemChangedListener<ImageAdapter.ViewHolder> {

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

    lateinit var myViewHolder: MyViewHolder

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(com.studioseven.postcard.R.layout.item_postcard, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        currentPos = position

        //myViewHolder = holder

        holder.userNameTv.text = postcardList[position].userName
        holder.locationTv.text = postcardList[position].location

        Picasso.get().load(postcardList[position].userImageLink).into(holder.userProfileImage)
        holder.postImage.setOnClickListener{
            val myIntent = Intent(context, ImageScreenActivity::class.java)
            myIntent.putExtra("imageUrl", postcardList[position].mediaLinkList[holder.imageScroll.currentItem].url) //Optional parameters
            context!!.startActivity(myIntent)
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
        holder.imageScroll.addOnItemChangedListener(this)
        holder.imageScroll.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            myViewHolder = holder
        }

        var url = postcardList[position].mediaLinkList[holder.imageScroll.currentItem].url
        if(url == "") url ="https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"

        Picasso.get().load(url).into(holder.postImage)


        holder.image_heart_white.setOnClickListener {
            holder.image_heart_white.visibility = View.INVISIBLE
            holder.image_heart_red.visibility = View.VISIBLE
        }
        holder.image_heart_red.setOnClickListener {
            holder.image_heart_white.visibility = View.VISIBLE
            holder.image_heart_red.visibility = View.INVISIBLE
        }
    }

    override fun onCurrentItemChanged(holder: ImageAdapter.ViewHolder?, position: Int) {
        //viewHolder will never be null, because we never remove items from adapter's list
        if (holder != null) {
            var url = postcardList[myViewHolder.adapterPosition].mediaLinkList[myViewHolder.imageScroll.currentItem].url
            if(url == "") url ="https://media.licdn.com/dms/image/C5103AQFZ1Xq-UNwjpw/profile-displayphoto-shrink_800_800/0?e=1560384000&v=beta&t=INl5kK-hwQRyIvNZeo-703mYOjn8RIXUgoenZVEVczM"

            Picasso.get().load(url)
                .into(myViewHolder.postImage)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = postcardList.size

}
