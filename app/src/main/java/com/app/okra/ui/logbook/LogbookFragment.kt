package com.app.okra.ui.logbook

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.TestLogsRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.ui.logbook.meal.MealLogsFragment
import com.app.okra.ui.logbook.test.TestLogsFragment
import com.app.okra.ui.logbook.test.TestLogsViewModel
import com.app.okra.utils.AppConstants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.*
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.ivAfterMeal
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.ivDisplayAll
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.tvFromDate
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.tvToDate
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.btnApplyFilter
import kotlinx.android.synthetic.main.bottomsheet_logs_filter.btnReset
import kotlinx.android.synthetic.main.bottomsheet_meal_logs_filter.*
import kotlinx.android.synthetic.main.bottomsheet_meal_logs_filter.ivMealDisplayAll
import kotlinx.android.synthetic.main.fragment_logbook.*
import java.util.*

class LogbookFragment : BaseFragment() {

    private var mPagerAdapter: ViewPagerBottomBar?=null
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var displayAll =false
    private var beforeMeal =false
    private var afterMeal =false
    private var postMedicine =false
    private var postWorkout=false
    private var controlSolution=false

    private var today=false
    private var thisWeek=false
    private var thisMonth=false

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
        mPagerAdapter = activity?.supportFragmentManager?.let { ViewPagerBottomBar(it) }
        mPagerAdapter?.addFragment(TestLogsFragment())
        mPagerAdapter?.addFragment(MealLogsFragment())
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 1
        viewPager.beginFakeDrag()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

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
            if(viewPager.currentItem == 0)
                showTestBottomSheetDialog()
            else
                showMealBottomSheetDialog()
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

    private fun setupFullHeight(bottomSheet: BottomSheetDialog) {
        val parentLayout = bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        parentLayout?.let { it ->
            val behaviour = BottomSheetBehavior.from(it)
            val layoutParams = it.layoutParams
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.layoutParams = layoutParams
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    private fun showTestBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.apply {
            setContentView(R.layout.bottomsheet_logs_filter)
            setupFullHeight(bottomSheetDialog)
            show()

            resetTestLogFilter()

            ivDisplayAll.setOnClickListener {
                ivDisplayAll.isSelected = !ivDisplayAll.isSelected
                ivBeforeMeal.isSelected= false
                ivAfterMeal.isSelected= false
                ivPostMedicine.isSelected= false
                ivPostWorkout.isSelected= false
                ivControlSolution.isSelected= false

                beforeMeal = false
                afterMeal = false
                postMedicine = false
                postWorkout = false
                controlSolution = false
            }
            ivBeforeMeal.setOnClickListener {
                beforeMeal = !ivBeforeMeal.isSelected
                ivBeforeMeal.isSelected = !ivBeforeMeal.isSelected

                ivDisplayAll.isSelected = false
                displayAll = false
            }
            ivAfterMeal.setOnClickListener {
                afterMeal = !ivAfterMeal.isSelected
                ivAfterMeal.isSelected = !ivAfterMeal.isSelected

                ivDisplayAll.isSelected = false
                displayAll = false

            }
            ivPostMedicine.setOnClickListener {
                postMedicine = !ivPostMedicine.isSelected
                ivPostMedicine.isSelected = !ivPostMedicine.isSelected

                ivDisplayAll.isSelected = false
                displayAll = false

            }
            ivPostWorkout.setOnClickListener {
                postWorkout = !ivPostWorkout.isSelected
                ivPostWorkout.isSelected = !ivPostWorkout.isSelected

                ivDisplayAll.isSelected = false
                displayAll = false

            }
            ivControlSolution.setOnClickListener {
                controlSolution = !ivControlSolution.isSelected
                ivControlSolution.isSelected = !ivControlSolution.isSelected

                ivDisplayAll.isSelected = false
                displayAll = false
            }
            tvFromDate.setOnClickListener {
                selectDate(tvFromDate)
            }
            tvToDate.setOnClickListener {
                selectDate(tvToDate)
            }
            btnApplyFilter.setOnClickListener {
                if (mPagerAdapter?.position == 0){
                    val testLogFragment = mPagerAdapter?.getItem(0) as TestLogsFragment
                    val toDate = tvToDate.text.toString().trim()
                    val fromDate = tvFromDate.text.toString().trim()
                    val filterTiming = getSelectedFilterTiming()
                    testLogFragment.getData(
                        pageNo = 1,
                        testingTime = filterTiming,
                        fromDate= fromDate,
                        toDate= toDate,
                    )
                    bottomSheetDialog.dismiss()
                }
            }
            btnReset.setOnClickListener {
                tvFromDate.text = ""
                tvToDate.text = ""
                ivDisplayAll.isSelected =false
                ivBeforeMeal.isSelected= false
                ivAfterMeal.isSelected= false
                ivPostMedicine.isSelected= false
                ivPostWorkout.isSelected= false
                ivControlSolution.isSelected= false
                resetTestLogFilter()
            }
        }
    }

    private fun getSelectedFilterTiming(): String {
        val sBuilder = StringBuilder()

        if(displayAll){
            sBuilder.append(AppConstants.DISPLAY_ALL)
        }

        if(beforeMeal){
            sBuilder.append(AppConstants.BEFORE_MEAL)
            sBuilder.append(",")
        }

        if(afterMeal){
            sBuilder.append(AppConstants.AFTER_MEAL)
            sBuilder.append(",")
        }
        if(postMedicine){
            sBuilder.append(AppConstants.POST_MEDICINE)
            sBuilder.append(",")
        }
        if(postWorkout){
            sBuilder.append(AppConstants.POST_WORKOUT)
            sBuilder.append(",")
        }
        if(controlSolution){
            sBuilder.append(AppConstants.CONTROLE_SOLUTION)
        }
        return  sBuilder.toString()
    }

    private fun showMealBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.apply {
            setContentView(R.layout.bottomsheet_meal_logs_filter)
            setupFullHeight(bottomSheetDialog)
            show()

            resetMealLogFilter()

            ivMealDisplayAll.setOnClickListener {
                ivMealDisplayAll.isSelected = !ivMealDisplayAll.isSelected
                displayAll = !ivMealDisplayAll.isSelected
                ivToday.isSelected = false
                ivThisWeek.isSelected = false
                ivThisMonth.isSelected = false

                today = false
                thisWeek = false
                thisMonth = false
            }
            ivToday.setOnClickListener {
                ivToday.isSelected = !ivToday.isSelected
                today = !ivToday.isSelected
            }
            ivThisWeek.setOnClickListener {
                ivThisWeek.isSelected = !ivThisWeek.isSelected
                thisWeek = !ivThisWeek.isSelected
            }
            ivThisMonth.setOnClickListener {
                ivThisMonth.isSelected = !ivThisMonth.isSelected
                thisMonth = !ivThisMonth.isSelected
            }
            tvMealFromDate.setOnClickListener {
                selectDate(tvFromDate)
            }
            tvMealToDate.setOnClickListener {
                selectDate(tvToDate)
            }
            btnMealApplyFilter.setOnClickListener {
                // selectDate(tvToDate)
            }
            btnReset.setOnClickListener {
                tvMealFromDate.text = ""
                tvMealToDate.text = ""
                displayAll = false
                ivToday.isSelected = false
                ivThisWeek.isSelected = false
                ivThisMonth.isSelected = false
                resetMealLogFilter()
            }
        }
    }


    private fun resetTestLogFilter() {
        displayAll = false
        beforeMeal = false
        afterMeal = false
        postMedicine = false
        postWorkout = false
        controlSolution = false
    }

    private fun resetMealLogFilter() {
        displayAll = false
        today = false
        thisWeek = false
        thisMonth = false
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