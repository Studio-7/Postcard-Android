package com.studioseven.postcard.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.studioseven.postcard.R
import com.studioseven.postcard.Utils.NonSwipeableViewPager


class IntroActivity : AppCompatActivity() {

    private lateinit var viewPager: NonSwipeableViewPager
    private lateinit var pagerAdapter: com.studioseven.postcard.Adapter.PagerAdapter
    private lateinit var intro_1: ImageView
    private lateinit var intro_2: ImageView
    private lateinit var intro_3: ImageView
    private lateinit var next: TextView
    private lateinit var sharedpreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        sharedpreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)

        viewPager = findViewById(R.id.pager)
        pagerAdapter = com.studioseven.postcard.Adapter.PagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        intro_1 = findViewById(R.id.intro1)
        intro_2 = findViewById(R.id.intro2)
        intro_3 = findViewById(R.id.intro3)
        next = findViewById(R.id.intro_skip)
        val linearLayout :LinearLayout = findViewById(R.id.linearlayout)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPageSelected(p0: Int) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                val mDrawable = ContextCompat.getDrawable(this@IntroActivity, R.drawable.intro_dot_default)
                if(p0 == 0){
                    linearLayout.setBackgroundColor(ContextCompat.getColor(this@IntroActivity, R.color.colorPrimary))
                    window.statusBarColor = ContextCompat.getColor(this@IntroActivity, R.color.colorPrimary)
                    next.text = "NEXT"
                    intro_1.setImageResource(R.drawable.intro_dot_selected)
                    intro_2.setImageResource(R.drawable.intro_dot_default)
                    intro_3.setImageResource(R.drawable.intro_dot_default)
                } else if(p0 == 1){
                    linearLayout.setBackgroundColor(ContextCompat.getColor(this@IntroActivity, R.color.colorAccent))
                    window.statusBarColor = ContextCompat.getColor(this@IntroActivity, R.color.colorAccent)
                    next.text = "NEXT"
                    intro_1.setImageResource(R.drawable.intro_dot_default)
                    intro_2.setImageResource(R.drawable.intro_dot_selected)
                    intro_3.setImageResource(R.drawable.intro_dot_default)
                } else{
                    linearLayout.setBackgroundColor(ContextCompat.getColor(this@IntroActivity, R.color.material_blue_grey_800))
                    window.statusBarColor = ContextCompat.getColor(this@IntroActivity, R.color.material_blue_grey_800)
                    next.text = "FINISH"
                    intro_1.setImageResource(R.drawable.intro_dot_default)
                    intro_2.setImageResource(R.drawable.intro_dot_default)
                    intro_3.setImageResource(R.drawable.intro_dot_selected)
                }
            }

        })

        next.setOnClickListener{
            val set: HashSet<String> = sharedpreferences.getStringSet((viewPager.currentItem).toString(), null) as HashSet<String>
            if(set.isEmpty()){
                Toast.makeText(this, "Please select atleast one option", Toast.LENGTH_LONG).show()
            } else {
                if (viewPager.currentItem != 2) {
                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                } else {
                    val intent = Intent(this@IntroActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

}
