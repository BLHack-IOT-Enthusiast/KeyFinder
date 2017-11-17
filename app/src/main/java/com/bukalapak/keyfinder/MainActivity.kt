package com.bukalapak.keyfinder

import android.content.Context
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var pagerAdapter : PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = PagerAdapter(supportFragmentManager, this)
        viewContainer.adapter = pagerAdapter

        tabView.setupWithViewPager(viewContainer)
        for (i in 0 until tabView.tabCount) {
            val tab = tabView.getTabAt(i)
            tab?.customView = pagerAdapter.getTabView(i)
        }
    }


    class PagerAdapter : FragmentPagerAdapter {
        val NUM_ITEMS = 2
        var context : Context
        var dataTabTitle = mutableListOf("KeyFinder", "Setting")
        var dataTabImage = mutableListOf(R.drawable.ic_arrow, R.drawable.ic_tools)

        constructor(fm: FragmentManager?, context : Context) : super(fm) {
            this.context = context
        }


        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> KeyFinderFragment()
                1 -> SettingFragment()
                else -> KeyFinderFragment()
            }
        }

        override fun getCount(): Int {
            return NUM_ITEMS
        }

        fun getTabView(position : Int) : View {
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null)
            var imageView = view.findViewById<ImageView>(R.id.imageIcon)
            var textView = view.findViewById<TextView>(R.id.textTitle)

            textView.text = dataTabTitle[position]
            imageView.setImageDrawable(ActivityCompat.getDrawable(context, dataTabImage[position]))

            return view
        }

    }
}
