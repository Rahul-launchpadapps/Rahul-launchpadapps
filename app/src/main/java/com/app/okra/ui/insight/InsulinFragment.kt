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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_blood_glucose.*
import kotlinx.android.synthetic.main.fragment_insulin.*
import kotlinx.android.synthetic.main.fragment_insulin.chart
import kotlinx.android.synthetic.main.fragment_insulin.iv_this_month
import kotlinx.android.synthetic.main.fragment_insulin.iv_this_week
import kotlinx.android.synthetic.main.fragment_insulin.iv_today
import kotlinx.android.synthetic.main.fragment_insulin.rl_this_month
import kotlinx.android.synthetic.main.fragment_insulin.rl_this_week
import kotlinx.android.synthetic.main.fragment_insulin.rl_today
import kotlinx.android.synthetic.main.fragment_insulin.spinner
import kotlinx.android.synthetic.main.fragment_insulin.tvSet
import kotlinx.android.synthetic.main.fragment_insulin.tv_no_chart
import kotlinx.android.synthetic.main.fragment_insulin.tv_this_month
import kotlinx.android.synthetic.main.fragment_insulin.tv_this_week
import kotlinx.android.synthetic.main.fragment_insulin.tv_today

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
            it.data?.let {
                if (it.insightData?.size!! > 0) {
                    chart.visibility = View.VISIBLE
                    tv_no_chart.visibility = View.GONE
                    setCharts(it.insightData!!)
                } else {
                    chart.visibility = View.INVISIBLE
                    tv_no_chart.visibility = View.VISIBLE
                }
            }
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

    private fun setCharts(graphInfo: ArrayList<Float>) {
        chart.getDescription().setEnabled(false)
        chart.setTouchEnabled(true)
        chart.setDrawGridBackground(false)
        chart.setDragEnabled(false)
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)

        var xAxis: XAxis
        xAxis = chart.getXAxis()
        val array = arrayOfNulls<Int>(graphInfo.size)
        for (i in 0 until graphInfo.size)
            array[i] = i
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return array[value.toInt()].toString()
            }
        }
        xAxis.disableGridDashedLine()
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)

        var yAxis: YAxis
        yAxis = chart.getAxisLeft()
        // disable dual axis (only use LEFT axis)
        chart.getAxisRight().setEnabled(false)
        yAxis.disableGridDashedLine()
        yAxis.setDrawAxisLine(false)

        if(graphInfo.size>0)
            setData(graphInfo.size, graphInfo)
    }

    private fun setData(count: Int, list: ArrayList<Float>) {
        val values: ArrayList<Entry> = ArrayList()
        for (i in 0 until count) {
            list[i].toFloat().let { Entry(i.toFloat(), it) }.let { values.add(it) }
        }
        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "")
            set1.setDrawIcons(false)

            // draw dashed line
            set1.disableDashedLine()

            // black lines and points
            set1.color = ContextCompat.getColor(requireContext(),R.color.green_1)
            set1.setCircleColor(ContextCompat.getColor(requireContext(),R.color.green_1))

            // line thickness and point size
            set1.lineWidth = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)
            set1.setDrawCircles(false)

            // customize legend entry
            set1.formLineWidth = 0f
            set1.formSize = 0f

            // text size of values
            set1.valueTextSize = 0f

            // set the filled area
            set1.setDrawFilled(false)

            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1) // add the data sets

            val data = LineData(dataSets)

            chart.data = data
        }
    }

}