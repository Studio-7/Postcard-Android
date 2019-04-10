package com.studioseven.postcard.Utils

import android.support.v7.widget.RecyclerView
import com.studioseven.postcard.Adapters.PostcardAdapter

interface OnItemChangedListener<T : RecyclerView.ViewHolder> {
    /*
         * This method will be also triggered when view appears on the screen for the first time.
         * If data set is empty, viewHolder will be null and adapterPosition will be NO_POSITION
         */
    fun onCurrentItemChanged(viewHolder: T?, adapterPosition: Int, postCardHolder: PostcardAdapter.MyViewHolder)
}