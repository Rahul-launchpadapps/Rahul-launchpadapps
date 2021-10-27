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
import kotlinx.android.synthetic.main.fragment_meal_logs.*

class HomeFragment : BaseFragmentWithoutNav(), Listeners.ItemClickListener {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mealLogsAdapter: MealLogsAdapter
    private var hashMapKeyList  = ArrayList<String>()
    private var hashMapMealLog = hashMapOf<String,  ArrayList<MealData>>()

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
        viewModel.dashboardInfo(AppConstants.TODAY)
        viewModel.stripeInfo()
        tv_name.text = PreferenceManager.getString(AppConstants.Pref_Key.NAME)
        setListener()
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, observeError = false)
        viewModel._stripeInfoLiveData.observe(viewLifecycleOwner) { it ->
            it.data?.let{
                tvReceivedStrips.text = it.totalStripsReceived
                tvLeftStrips.text = it.totalStripsLeft
            }
        }

        viewModel._dashboardLiveData.observe(viewLifecycleOwner) { it ->
            it.data?.let{
                tvTotalTestValue.text = it.totalTest
                tvAvgBgValue.text = it.avgBloodGlucose
                tvInsulinValue.text = it.avgInsulin
                tvHyperValue.text = it.hyper_hypes?.hyper
                tvHbaValue.text = it.Est_HbA1c
                tvCarbsValue.text = it.carbsCount
                if(it.foodLogs?.size!! >0){
                    tv_food_log.visibility = View.VISIBLE
                    rv_meal_list.visibility = View.VISIBLE
                        // setAdapter()
                  //  it.data?.let { it1 -> prepareDateWiseData(it1)}
                }else {
                    tv_food_log.visibility = View.GONE
                    rv_meal_list.visibility = View.GONE
                }
            }
        }

        viewModel._errorObserver.observe(viewLifecycleOwner){
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
        val hashMap = hashMapOf<String,  ArrayList<MealData>>()
        if(testLogData.isNotEmpty()) {
            for ((index, data) in testLogData.withIndex()){
                val date = data.date
                date?.let{
                    val dateToSet = getDateFromISOInString(it, formatYouWant = "dd/MM/yyyy")

                    val list: java.util.ArrayList<MealData> = if(hashMap.containsKey(dateToSet)){
                        hashMap[dateToSet] as ArrayList<MealData>
                    }else{
                        ArrayList()
                    }
                    list.add(data)
                    hashMap[dateToSet]  = list
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
            viewModel.dashboardInfo(AppConstants.TODAY)
        }

        rl_this_week.setOnClickListener {
            handleTabsBackground(1)
            viewModel.dashboardInfo(AppConstants.WEEK)
        }

        rl_this_month.setOnClickListener {
            handleTabsBackground(2)
            viewModel.dashboardInfo(AppConstants.MONTH)
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
        } else if(value == 1) {
            tv_this_week.setTextColor(textGreenColor)
            iv_this_week.backgroundTintList =
                ColorStateList.valueOf(textGreenColor)
            tv_today.setTextColor(textGreyColor)
            iv_today.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
            tv_this_month.setTextColor(textGreyColor)
            iv_this_month.backgroundTintList =
                ColorStateList.valueOf(textWhiteColor)
        }else {
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

}