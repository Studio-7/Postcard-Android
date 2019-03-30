package com.studioseven.postcard.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.studioseven.postcard.Models.Options
import com.studioseven.postcard.Fragments.QuestionFragment
import com.studioseven.postcard.R

class PagerAdapter internal constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(p0: Int): Fragment? {
        val options = Options("here", R.drawable.place, 0)
        val option2 = Options("This one", R.drawable.place, 1)
        val list = ArrayList<Options>()
        list.add(options)
        list.add(option2)
        when(p0){
            0 -> return QuestionFragment.newInstance("This is my question", list, 0)
            1 -> return QuestionFragment.newInstance("This is my second question", list, 1)
            2 -> return QuestionFragment.newInstance("This is my third question", list, 2)
        }
        return null
    }

    override fun getCount(): Int {
        return 3
    }


}