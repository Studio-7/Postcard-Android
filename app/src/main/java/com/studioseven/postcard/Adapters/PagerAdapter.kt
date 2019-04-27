package com.studioseven.postcard.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.studioseven.postcard.Fragments.QuestionFragment
import com.studioseven.postcard.Models.Options

class PagerAdapter internal constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(p0: Int): Fragment? {
        val options1a = Options("New to this", "http://static.wanderoam.com/wp-content/uploads/2019/02/16143702/Osprey-Backpack-20190216143702-20190216143702.jpg", 0)
        val option2a = Options("I can work with a map.", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ61ZdMMytZRQkUKkENLQXFS_RaFbjjRaDEPC9HE8_Vd5FTczhpcw", 1)
        val option3a = Options("Hitchhiker","https://cdn.totalfratmove.com/wp-content/uploads/2018/10/ddc37ea7fa5b38466202abe51afc83eb.jpg" , 2)
        val option4a = Options("On the fly", "https://ak0.picdn.net/shutterstock/videos/21642310/thumb/9.jpg?i10c=img.resize(height:160)", 3)

        val list1 = ArrayList<Options>()
        list1.add(options1a)
        list1.add(option2a)
        list1.add(option3a)
        list1.add(option4a)

        val options1b = Options("The Explorer", "https://lh3.googleusercontent.com/KAVlusWNMUGFE3hELyJ1GWp-rgoTTJ0MP9oXQFMUUEL3-9ZCt3sf-knblZ4a9ndOEm66UqxbQ6QDJZm5ZRs=rw", 0)
        val option2b = Options("The Camera Guy", "https://img.freepik.com/free-photo/female-tourist-with-camera-balcony_23-2147981890.jpg?size=626&ext=jpg", 1)
        val option3b = Options("The Historian","http://www.orleanscountyny.gov/portals/0/Departments/Historian/Historian2_web1.jpg" , 2)
        val option4b = Options("The AirBnB Guy", "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/BBVwsz9.img?h=390&w=624&m=6&q=60&o=f&l=f&x=789&y=317", 3)
        val option5b = Options("The Foodie", "https://i.ytimg.com/vi/FlGiwUwMdEo/maxresdefault.jpg", 4)

        val list2 = ArrayList<Options>()
        list1.add(options1b)
        list1.add(option2b)
        list1.add(option3b)
        list1.add(option4b)
        list1.add(option5b)

        val options1c = Options("Midnight in Paris", "https://i.pinimg.com/originals/f8/4a/7d/f84a7d3a07468dc7caabdfc9b2b2bf45.jpg", 0)
        val option2c = Options("The Darjeeling Limited", "https://www.holidify.com/images/bgImages/DARJEELING.jpg", 1)
        val option3c = Options("Destination Tokyo","https://tokyotreat.cdn.prismic.io/tokyotreat/4b82c625a0daeb4e482de11f5c1448485f480a82_tokyo-main.jpg" , 2)
        val option4c = Options("The Grand Budapest Hotel", "http://europe-re.com/uploads/europe/post_cover_images/63706/cover-63706.jpg", 3)

        val list3 = ArrayList<Options>()
        list1.add(options1c)
        list1.add(option2c)
        list1.add(option3c)
        list1.add(option4c)

        when(p0){
            0 -> return QuestionFragment.newInstance("Your travel experience?", list1, 0)
            1 -> return QuestionFragment.newInstance("What describes you the best?", list2, 1)
            2 -> return QuestionFragment.newInstance("The next chapter of your story?", list3, 2)
        }
        return null
    }

    override fun getCount(): Int {
        return 3
    }


}