package com.app.okra.ui.insight

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.app.okra.R
import com.app.okra.base.BaseFragmentWithoutNav
import com.app.okra.base.BaseViewModel
import com.app.okra.ui.logbook.ViewPagerBottomBar
import kotlinx.android.synthetic.main.fragment_in_sight.*

class InSightFragment : BaseFragmentWithoutNav() {

    private var mPagerAdapter: ViewPagerBottomBar? = null

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_sight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        initListeners()
    }

    private fun setupViewPager() {
        mPagerAdapter = activity?.supportFragmentManager?.let { ViewPagerBottomBar(it) }
        mPagerAdapter?.addFragment(BloodGlucoseFragment())
        mPagerAdapter?.addFragment(InsulinFragment())
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 1
        viewPager.beginFakeDrag()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                handleTabsBackground(position)
            }
        })
    }

    private fun initListeners() {
        rl_blood_glucose.setOnClickListener {
            handleTabsBackground(0)
            viewPager.currentItem = 0
        }
        rl_insulin.setOnClickListener {
            handleTabsBackground(1)
            viewPager.currentItem = 1
        }

        ivFilter.setOnClickListener {

        }
    }

    private fun handleTabsBackground(value: Int) {
        val textGreenColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.green_1) } ?: 0
        val textGreyColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.grey_3) } ?: 0
        val textWhiteColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.bg_grey) } ?: 0

        if (value == 0) {
            tv_blood_glucose.setTextColor(textGreenColor)
            iv_blood_glucose.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_insulin.setTextColor(textGreyColor)
            iv_insulin.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        } else {
            tv_insulin.setTextColor(textGreenColor)
            iv_insulin.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_blood_glucose.setTextColor(textGreyColor)
            iv_blood_glucose.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        }
    }

}