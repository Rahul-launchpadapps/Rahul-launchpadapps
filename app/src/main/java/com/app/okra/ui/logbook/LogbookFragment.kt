package com.app.okra.ui.logbook

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.TestLogsRepoImpl
import com.app.okra.extension.viewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.*
import kotlinx.android.synthetic.main.fragment_logbook.*
import java.util.*

class LogbookFragment : BaseFragment() {

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                TestLogsViewModel(TestLogsRepoImpl(apiServiceAuth))
            }
        ).get(TestLogsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logbook, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        initListeners()
    }

    private fun setupViewPager() {
        val mPagerAdapter = activity?.supportFragmentManager?.let { ViewPagerBottomBar(it) }
        mPagerAdapter?.addFragment(TestLogsFragment())
        mPagerAdapter?.addFragment(MealLogsFragment())
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 1
        viewPager.beginFakeDrag()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

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
        rl_test_logs.setOnClickListener {
            handleTabsBackground(0)
            viewPager.currentItem = 0
        }
        rl_meal_logs.setOnClickListener {
            handleTabsBackground(1)
            viewPager.currentItem = 1
        }

        ivFilter.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun handleTabsBackground(value: Int) {
        val textGreenColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.green_1) } ?: 0
        val textGreyColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.grey_3) } ?: 0
        val textWhiteColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.white) } ?: 0

        if (value == 0) {
            tv_test_logs.setTextColor(textGreenColor)
            iv_test_logs.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_meal_logs.setTextColor(textGreyColor)
            iv_meal_logs.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        } else {
            tv_meal_logs.setTextColor(textGreenColor)
            iv_meal_logs.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_test_logs.setTextColor(textGreyColor)
            iv_test_logs.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottomsheet_logs_filter)
        bottomSheetDialog.show()

        bottomSheetDialog.ivDisplayAll.setOnClickListener {
            if (bottomSheetDialog.ivDisplayAll.isSelected) {
                bottomSheetDialog.ivDisplayAll.isSelected = false
            } else {
                bottomSheetDialog.ivDisplayAll.isSelected = true
            }
        }

        bottomSheetDialog.ivBeforeMeal.setOnClickListener {
            if (bottomSheetDialog.ivBeforeMeal.isSelected) {
                bottomSheetDialog.ivBeforeMeal.isSelected = false
            } else {
                bottomSheetDialog.ivBeforeMeal.isSelected = true
            }
        }

        bottomSheetDialog.ivAfterMeal.setOnClickListener {
            if (bottomSheetDialog.ivAfterMeal.isSelected) {
                bottomSheetDialog.ivAfterMeal.isSelected = false
            } else {
                bottomSheetDialog.ivAfterMeal.isSelected = true
            }
        }

        bottomSheetDialog.ivPostMedicine.setOnClickListener {
            if (bottomSheetDialog.ivPostMedicine.isSelected) {
                bottomSheetDialog.ivPostMedicine.isSelected = false
            } else {
                bottomSheetDialog.ivPostMedicine.isSelected = true
            }
        }

        bottomSheetDialog.ivPostWorkout.setOnClickListener {
            if (bottomSheetDialog.ivPostWorkout.isSelected) {
                bottomSheetDialog.ivPostWorkout.isSelected = false
            } else {
                bottomSheetDialog.ivPostWorkout.isSelected = true
            }
        }

        bottomSheetDialog.ivControlSolution.setOnClickListener {
            if (bottomSheetDialog.ivControlSolution.isSelected) {
                bottomSheetDialog.ivControlSolution.isSelected = false
            } else {
                bottomSheetDialog.ivControlSolution.isSelected = true
            }
        }

        bottomSheetDialog.tvFromDate.setOnClickListener {
            selectDate(bottomSheetDialog.tvFromDate)
        }

        bottomSheetDialog.tvToDate.setOnClickListener {
            selectDate(bottomSheetDialog.tvToDate)
        }
    }

    private fun selectDate(tvFromDate: AppCompatTextView) {
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
            val strDate: String = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
            tvFromDate.text = strDate
        }, mYear, mMonth, mDay)
        val c1 = Calendar.getInstance()
        c1.add(Calendar.MONTH, -2)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()

    }

}