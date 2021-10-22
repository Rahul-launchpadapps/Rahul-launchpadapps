package com.app.okra.ui.insight

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragmentWithoutNav
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.BloodGlucoseRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.ui.my_account.setting.measurement.CustomSpinnerAdapter
import com.app.okra.utils.AppConstants
import com.app.okra.utils.getMealTime
import com.app.okra.utils.navigateToLogin
import kotlinx.android.synthetic.main.fragment_insulin.*

class InsulinFragment : BaseFragmentWithoutNav() {

    private val type : String = AppConstants.INSULIN
    private var time : String = AppConstants.TODAY
    private var testingTime : String = AppConstants.BEFORE_MEAL
    private lateinit var customSpinnerAdapter: CustomSpinnerAdapter

    private val timingList by lazy {
        arrayListOf<String>()
    }
    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                BloodGlucoseViewModel(BloodGlucoseRepoImpl(apiServiceAuth))
            }
        ).get(BloodGlucoseViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insulin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setObserver()
        setListener()
        setAdapter()
    }

    private fun getData() {
        viewModel.prepareRequest(type,testingTime,time)
        viewModel.getInsight()
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, observeError = false)
        viewModel._insightLiveData.observe(viewLifecycleOwner) { it ->
        }

        viewModel._errorObserver.observe(viewLifecycleOwner){
            val data = it.getContent()
            data?.message?.let { it1 -> showToast(it1) }

            if (data?.message == "Your login session has been expired.") {
                navigateToLogin(requireActivity())
                requireActivity().finish()
            }

        }
    }

    private fun setListener() {
        val textGreenColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.green_1) } ?: 0
        val textGreyColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.grey_3) } ?: 0
        val textWhiteColor =
            activity?.let { it1 -> ContextCompat.getColor(it1, R.color.bg_grey) } ?: 0

        rl_today.setOnClickListener {
            tv_today.setTextColor(textGreenColor)
            iv_today.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_this_week.setTextColor(textGreyColor)
            iv_this_week.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            tv_this_month.setTextColor(textGreyColor)
            iv_this_month.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            time = AppConstants.TODAY
            getData()
        }
        rl_this_week.setOnClickListener {
            tv_this_week.setTextColor(textGreenColor)
            iv_this_week.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_today.setTextColor(textGreyColor)
            iv_today.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            tv_this_month.setTextColor(textGreyColor)
            iv_this_month.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            time = AppConstants.WEEK
            getData()
        }
        rl_this_month.setOnClickListener {
            tv_this_month.setTextColor(textGreenColor)
            iv_this_month.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_this_week.setTextColor(textGreyColor)
            iv_this_week.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            tv_today.setTextColor(textGreyColor)
            iv_today.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            time = AppConstants.MONTH
            getData()
        }

        tvSet.setOnClickListener {
            spinner.performClick()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tvSet.text = timingList[p2]
                testingTime = getMealTime(timingList[p2],false)
                getData()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

    }

    private fun setAdapter() {
        timingList.add(AppConstants.BEFORE_MEAL_TEXT)
        timingList.add(AppConstants.AFTER_MEAL_TEXT)
        timingList.add(AppConstants.POST_MEDICINE_TEXT)
        timingList.add(AppConstants.POST_WORKOUT_TEXT)
        timingList.add(AppConstants.CONTROLE_SOLUTION_TEXT)

        customSpinnerAdapter = CustomSpinnerAdapter(requireActivity(), timingList)
        spinner.adapter = customSpinnerAdapter

        var index = 0
        tvSet.text = timingList[index]
    }
}