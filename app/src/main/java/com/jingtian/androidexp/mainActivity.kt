package com.jingtian.androidexp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class mainActivity:FragmentActivity() {
    val frags:ArrayList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        var tbl = findViewById<TabLayout>(R.id.main_tbl)
        var vp = findViewById<ViewPager2>(R.id.main_vp)


        frags.add(exp1_fragment())
        frags.add(exp2_fragment())
        frags.add(exp3_fragment())
        frags.add(exp4_fragment())


        vp.adapter = object : FragmentStateAdapter(this@mainActivity) {
            override fun getItemCount(): Int = frags.size

            override fun createFragment(position: Int): Fragment {
                return frags[position]
            }
        }

        TabLayoutMediator(tbl, vp, true, object : TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = "实验" + (position+1)
            }

        }).attach()

        vp.offscreenPageLimit = 1
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(fragmet in frags) {
            fragmet.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}