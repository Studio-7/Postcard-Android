package com.studioseven.postcard.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.studioseven.postcard.Models.Options
import com.studioseven.postcard.Fragments.QuestionFragment
import com.studioseven.postcard.R

class PagerAdapter internal constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(p0: Int): Fragment? {
        val options = Options("Manasarovar", "https://d3r8gwkgo0io6y.cloudfront.net/upload/OK/Mansarovar1-900x450-815x450.jpg", 0)
        val option2 = Options("Jhumri Telaiya", "http://www.seejharkhand.com/wp-content/uploads/2018/07/CH2-High-School-Jhumri-Telaiya-Koderma-Front.jpg", 1)
        val option3 = Options("Kanchi","https://www.holidify.com/images/cmsuploads/compressed/A-beautiful-image-of-Kamakshi-Amman-Temple-pond-with-towers_20171219212102.jpg" , 2)
        val option4 = Options("Paris", "https://photos.mandarinoriental.com/is/image/MandarinOriental/paris-2017-home?wid=2880&hei=1280&fmt=jpeg&crop=9,336,2699,1200&anchor=1358,936&qlt=75,0&fit=wrap&op_sharpen=0&resMode=sharp2&op_usm=0,0,0,0&iccEmbed=0&printRes=72", 3)
        val option5 = Options("Switzerland", "https://i.ytimg.com/vi/6AvRtaToqFU/maxresdefault.jpg", 4)

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