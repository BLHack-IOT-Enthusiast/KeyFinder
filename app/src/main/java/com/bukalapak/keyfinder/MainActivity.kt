package com.bukalapak.keyfinder

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var pagerAdapter : PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = PagerAdapter(supportFragmentManager)
        viewContainer.adapter = pagerAdapter
        createTabIcons()
    }

    private fun createTabIcons() {

    }

    class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        val NUM_ITEMS = 2

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

    }
}
