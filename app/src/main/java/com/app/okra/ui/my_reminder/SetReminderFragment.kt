package com.app.okra.ui.my_reminder

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.ReminderRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.ui.my_account.setting.measurement.CustomSpinnerAdapter
import com.app.okra.utils.AppConstants
import com.app.okra.utils.convertLocalTimeZoneToUTC
import kotlinx.android.synthetic.main.fragment_set_reminder.*
import kotlinx.android.synthetic.main.fragment_set_reminder.spinnerRepeat
import kotlinx.android.synthetic.main.fragment_set_reminder.tvDate
import kotlinx.android.synthetic.main.fragment_set_reminder.tvDateValue
import kotlinx.android.synthetic.main.fragment_set_reminder.tvSetRepeat
import kotlinx.android.synthetic.main.layout_button.*
import java.util.*

import android.content.Context

import android.content.Intent
import android.media.RingtoneManager

import android.app.*
import android.graphics.Color

import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.okra.R
import com.app.okra.utils.AlarmReceiver
import java.text.SimpleDateFormat

class SetReminderFragment : BaseFragment() {

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private lateinit var customSpinnerAdapter1: CustomSpinnerAdapter
    private lateinit var customSpinnerAdapter2: CustomSpinnerAdapter
    private var time: Int = 1
    private var strDate: String = ""
    private var endDate: String = ""
    private var timeValue: String = ""
    private var reminderType: Int = 0

    private val repeatList by lazy {
        arrayListOf<String>()
    }
    private val endRepeatList by lazy {
        arrayListOf<String>()
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                ReminderViewModel(ReminderRepoImpl(apiServiceAuth))
            }
        ).get(ReminderViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setAdapter()
        setListener()
        setObserver()
    }

    private fun getData() {
        arguments?.let { it ->
            var data: String? = it.getString("data")
            if(data.equals("diabetes")){
                reminderType = 2
            }else
                reminderType = 1
        }
    }

    private fun setListener() {
        clDate.setOnClickListener {
            selectDate()
        }

        clTime.setOnClickListener {
            time = 1
            selectTime()
        }

        ivDateSelector.setOnClickListener {
            if (ivDateSelector.isSelected) {
                ivDateSelector.isSelected = false
                tvDate.gravity = Gravity.CENTER
                tvDateValue.visibility = View.GONE
                if (!ivTimeSelector.isSelected)
                    layout_button.visibility = View.GONE
            } else {
                selectDate()
            }
        }

        ivTimeSelector.setOnClickListener {
            if (ivTimeSelector.isSelected) {
                ivTimeSelector.isSelected = false
                tvTime.gravity = Gravity.CENTER
                tvTimeValue.visibility = View.GONE
                if (!ivDateSelector.isSelected)
                    layout_button.visibility = View.GONE
            } else {
                time = 1
                selectTime()
            }
        }

        tvSetRepeat.setOnClickListener {
            spinnerRepeat.performClick()
        }

        tvSetEndRepeat.setOnClickListener {
            spinnerEndRepeat.performClick()
        }

        spinnerRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tvSetRepeat.text = repeatList[p2]
                if (p2 == 0) {
                    tvEndRepeat.visibility = View.GONE
                    clSpinnerEndRepeat.visibility = View.GONE
                } else {
                    tvEndRepeat.visibility = View.VISIBLE
                    clSpinnerEndRepeat.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spinnerEndRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tvSetEndRepeat.text = endRepeatList[p2]
                if (p2 == 1) {
                    time = 2
                    selectDate()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        btnCommon.setOnClickListener {
            setReminder(1,1)
            val startDate: String
            val timeDate: String
            val repeatType: String
            val endRepeatType: String
            val endDateValue: String
            val obj = HashMap<String, Any>()
            if (ivDateSelector.isSelected) {
                startDate = convertLocalTimeZoneToUTC("yyyy-MM-dd", strDate)
            } else {
                val c = Calendar.getInstance().time
                val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                strDate = df.format(c)
                startDate = convertLocalTimeZoneToUTC("yyyy-MM-dd", strDate)
            }
            obj.put("reminderType", reminderType)
            obj.put("startDate", startDate)

            if (ivTimeSelector.isSelected) {
                timeDate =
                    convertLocalTimeZoneToUTC("yyyy-MM-dd hh:mm a", strDate + " " + timeValue)
            } else
                timeDate =
                    convertLocalTimeZoneToUTC("yyyy-MM-dd hh:mm a", strDate + " " + "12:00 pm")
            obj.put("time", timeDate)

            if(tvSetRepeat.text.toString().equals(AppConstants.NEVER))
                repeatType = "NEVER"
            else if(tvSetRepeat.text.toString().equals(AppConstants.DAILY))
                repeatType = "EVERY_DAY"
            else if(tvSetRepeat.text.toString().equals(AppConstants.MONTHLY))
                repeatType = "EVERY_MONTH"
            else if(tvSetRepeat.text.toString().equals(AppConstants.WEEKLY))
                repeatType = "EVERY_WEEK"
            else
                repeatType = "SET_UP"

            obj.put("repeatType", repeatType)

            if(tvSetEndRepeat.text.toString().equals(AppConstants.NEVER))
                endRepeatType = "NEVER"
            else
                endRepeatType = "SET_UP"

            obj.put("endRepeatType", endRepeatType)

            if(endRepeatType.equals("SET_UP")) {
                endDateValue = convertLocalTimeZoneToUTC("yyyy-MM-dd", endDate)
                obj.put("endDate",endDateValue)
            }

            viewModel.setReminder(obj)
        }
    }

    private fun setAdapter() {
        btnCommon.text = getString(R.string.save)
        repeatList.add(AppConstants.NEVER)
        repeatList.add(AppConstants.DAILY)
        repeatList.add(AppConstants.WEEKLY)
        repeatList.add(AppConstants.MONTHLY)

        customSpinnerAdapter1 = CustomSpinnerAdapter(requireActivity(), repeatList)
        spinnerRepeat.adapter = customSpinnerAdapter1
        tvSetRepeat.text = repeatList[0]

        endRepeatList.add(AppConstants.NEVER)
        endRepeatList.add(AppConstants.END_REPEAT_DATE)

        customSpinnerAdapter2 = CustomSpinnerAdapter(requireActivity(), endRepeatList)
        spinnerEndRepeat.adapter = customSpinnerAdapter2
        tvSetEndRepeat.text = endRepeatList[0]
    }

    private fun selectDate() {
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                var date =
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                if (time == 1) {
                    layout_button.visibility = View.VISIBLE
                    ivDateSelector.isSelected = true
                    tvDate.gravity = Gravity.BOTTOM
                    tvDateValue.visibility = View.VISIBLE
                    strDate = date
                    tvDateValue.text = date
                } else if (time == 2) {
                    endDate = date
                    tvSetEndRepeat.text = date
                }
            }, mYear, mMonth, mDay)
        val c1 = Calendar.getInstance()
        c1.add(Calendar.MONTH, -2)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun selectTime() {
        val timePicker = TimePickerDialog(
            requireContext(),
            timePickerDialogListener,
            12,
            10,
            false
        )
        timePicker.show()
    }

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }
                timeValue = formattedTime
                layout_button.visibility = View.VISIBLE
                ivTimeSelector.isSelected = true
                tvTime.gravity = Gravity.BOTTOM
                tvTimeValue.visibility = View.VISIBLE
                tvTimeValue.text = formattedTime
            }
        }

    private fun setObserver() {
        setBaseObservers(viewModel, this)
        viewModel._setReminderLiveData.observe(viewLifecycleOwner) { it ->
            showToast("Saved Successfully")
            activity?.finish()
        }
    }

    fun setReminder(
        hour: Int,
        min: Int,
    ) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = min
        calendar[Calendar.SECOND] = 0
        val alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("type", "reminder")
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            100,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            alarmIntent
        )
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
}