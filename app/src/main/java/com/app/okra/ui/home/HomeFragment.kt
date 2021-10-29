package com.app.okra.ui.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.okra.R
import com.app.okra.base.BaseFragmentWithoutNav
import com.app.okra.base.BaseViewModel
import com.app.okra.data.preference.PreferenceManager
import com.app.okra.data.repo.HomeRepoImpl
import com.app.okra.extension.navigate
import com.app.okra.extension.navigationOnly
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.MealData
import com.app.okra.ui.logbook.meal.MealLogsAdapter
import com.app.okra.ui.notification.NotificationActivity
import com.app.okra.utils.AppConstants
import com.app.okra.utils.Listeners
import com.app.okra.utils.getDateFromISOInString
import com.app.okra.utils.navigateToLogin
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.rv_meal_list
import com.github.mikephil.charting.data.LineDataSet

import com.github.mikephil.charting.data.LineData

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

import com.app.okra.models.GraphInfo
import com.github.mikephil.charting.formatter.ValueFormatter

class HomeFragment : BaseFragmentWithoutNav(), Listeners.ItemClickListener {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mealLogsAdapter: MealLogsAdapter
    private var hashMapKeyList = ArrayList<String>()

    private var hashMapMealLog = LinkedHashMap<String, ArrayList<MealData>>()
  
    private var time:String = AppConstants.TODAY

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                HomeViewModel(HomeRepoImpl(apiServiceAuth))
            }
        ).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        viewModel.dashboardInfo(time)
        viewModel.stripeInfo()
        tv_name.text = PreferenceManager.getString(AppConstants.Pref_Key.NAME)
        tv_time.text = getString(R.string.time)+" (hr.)"
        setListener()
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, observeError = false)
        viewModel._stripeInfoLiveData.observe(viewLifecycleOwner) { it ->
            it.data?.let {
                tvReceivedStrips.text = it.totalStripsReceived
                tvLeftStrips.text = it.totalStripsLeft
            }
        }

        viewModel._dashboardLiveData.observe(viewLifecycleOwner) { it ->
            it.data?.let {
                tvTotalTestValue.text = it.totalTest

                if(it.avgBloodGlucose!=null) {
                    val valueToSet = String.format("%.2f", it.avgBloodGlucose!!.toBigDecimal()) + "mg/dL"
                    tvAvgBgValue.text =valueToSet
                }

                if(it.avgInsulin!=null) {
                    tvInsulinValue.text = String.format("%.2f", it.avgInsulin!!.toBigDecimal())
                }
                val hyperHypoText = "${it.hyper_hypes?.hyper} / ${it.hyper_hypes?.hypos}"
                tvHyperValue.text = hyperHypoText

                if(it.Est_HbA1c!=null) {
                    tvHbaValue.text = String.format("%.2f",it.Est_HbA1c!!.toBigDecimal())
                }

                if(it.carbsCount!=null) {
                    val valueToSet = String.format("%.2f", it.carbsCount!!.toBigDecimal())+ "mg/dL"
                    tvCarbsValue.text =  valueToSet
                }
                if (it.foodLogs?.size!! > 0) {
                    tv_food_log.visibility = View.VISIBLE
                    rv_meal_list.visibility = View.VISIBLE
                    it.foodLogs?.let { it1 -> prepareDateWiseData(it1) }
                    setAdapter()
                } else {
                    tv_food_log.visibility = View.GONE
                    rv_meal_list.visibility = View.GONE
                }
                if(it.graphInfo?.size!! > 0) {
                    chart.visibility = View.VISIBLE
                    tv_no_chart.visibility = View.GONE
                    setCharts(it.graphInfo!!)
                }else {
                    chart.visibility = View.INVISIBLE
                    tv_no_chart.visibility = View.VISIBLE
                }
            }
        }

        viewModel._errorObserver.observe(viewLifecycleOwner) {
            val data = it.getContent()
            data?.message?.let { it1 -> showToast(it1) }

            if (data?.message == getString(R.string.your_login_session_has_been_expired)) {
                navigateToLogin(requireActivity())
                requireActivity().finish()
            }
        }
    }

    private fun setAdapter() {
        mealLogsAdapter = MealLogsAdapter(this, hashMapKeyList, hashMapMealLog)
        layoutManager = LinearLayoutManager(requireContext())
        rv_meal_list.layoutManager = layoutManager
        rv_meal_list.adapter = mealLogsAdapter
    }

    private fun prepareDateWiseData(testLogData: ArrayList<MealData>) {
        hashMapMealLog.clear()
        hashMapKeyList.clear()
        val hashMap = LinkedHashMap<String, ArrayList<MealData>>()
        if (testLogData.isNotEmpty()) {
            for ((index, data) in testLogData.withIndex()) {
                val date = data.createdAt
                date?.let {
                    val dateToSet = getDateFromISOInString(it, formatYouWant = "dd/MM/yyyy")

                    val list: java.util.ArrayList<MealData> = if (hashMap.containsKey(dateToSet)) {
                        hashMap[dateToSet] as ArrayList<MealData>
                    } else {
                        ArrayList()
                    }
                    list.add(data)
                    hashMap[dateToSet] = list
                }
            }
        }
        hashMapMealLog.putAll(hashMap)
        hashMapKeyList.addAll(hashMap.keys.toList())
    }

    private fun setListener() {
        ivNotification.setOnClickListener {
            requireActivity().navigationOnly(NotificationActivity())
        }
        tvReceivedStrips.setOnClickListener {
            viewModel.stripeInfo()
        }

        tvLeftStrips.setOnClickListener {
            viewModel.stripeInfo()
        }

        rl_today.setOnClickListener {
            handleTabsBackground(0)
            time= AppConstants.TODAY
            viewModel.dashboardInfo(time)
            tv_time.text = getString(R.string.time)+" (hr.)"
        }

        rl_this_week.setOnClickListener {
            handleTabsBackground(1)
            time= AppConstants.WEEK
            viewModel.dashboardInfo(time)
            tv_time.text = getString(R.string.time)+" (Weekday)"
        }

        rl_this_month.setOnClickListener {
            handleTabsBackground(2)
            time= AppConstants.MONTH
            viewModel.dashboardInfo(time)
            tv_time.text = getString(R.string.time)+" (Day of month)"
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
            tv_today.setTextColor(textGreenColor)
            iv_today.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_this_week.setTextColor(textGreyColor)
            iv_this_week.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            tv_this_month.setTextColor(textGreyColor)
            iv_this_month.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        } else if (value == 1) {
            tv_this_week.setTextColor(textGreenColor)
            iv_this_week.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_today.setTextColor(textGreyColor)
            iv_today.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            tv_this_month.setTextColor(textGreyColor)
            iv_this_month.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        } else {
            tv_this_month.setTextColor(textGreenColor)
            iv_this_month.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_this_week.setTextColor(textGreyColor)
            iv_this_week.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            tv_today.setTextColor(textGreyColor)
            iv_today.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        }
    }

    override fun onSelect(o: Any?, o1: Any?) {

    }

    override fun onUnSelect(o: Any?, o1: Any?) {

    }

    private fun setCharts(graphInfo: ArrayList<GraphInfo>) {
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)

        val xAxis: XAxis
        xAxis = chart.getXAxis()
        val array = arrayOfNulls<String>(graphInfo.size)
        if(time == AppConstants.TODAY){
            for(i in 0 until graphInfo.size)
            array[i] = graphInfo[i].hours+"hr"
        } else if(time == AppConstants.WEEK){
            for(i in 0 until graphInfo.size)
                array[i] = graphInfo[i].day?.substring(0)
        }else if(time == AppConstants.MONTH){
            for(i in 0 until graphInfo.size)
                array[i] = "#"+graphInfo[i]._id.toString()
        }
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

        val yAxis: YAxis
        yAxis = chart.getAxisLeft()
        // disable dual axis (only use LEFT axis)
        chart.getAxisRight().setEnabled(false)
        yAxis.disableGridDashedLine()
        yAxis.setDrawAxisLine(false)

        if(graphInfo.size>0)
            setData(graphInfo.size, graphInfo)
    }

    private fun setData(count: Int, list: ArrayList<GraphInfo>) {
        val values: ArrayList<Entry> = ArrayList()
        for (i in 0 until count) {
            list[i].bloodGlucose?.toFloat()?.let { Entry(i.toFloat(), it) }?.let { values.add(it) }
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