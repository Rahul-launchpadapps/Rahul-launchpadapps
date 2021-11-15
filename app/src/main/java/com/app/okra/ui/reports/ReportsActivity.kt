package com.app.okra.ui.reports

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.ui.my_account.setting.measurement.CustomSpinnerAdapter
import com.app.okra.utils.DateFormatter
import com.app.okra.utils.getDatePicker
import com.app.okra.utils.getMinDateForReports
import java.util.*
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.layout_header.*


class ReportsActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private val reportsViewModel by lazy { ViewModelProvider(this).get(ReportsViewModel::class.java) }
    private lateinit var fileTypeDropDownAdapter: CustomSpinnerAdapter
    private lateinit var startDatePickerInstance: DatePickerDialog
    private lateinit var endDatePickerInstance: DatePickerDialog

    override fun getViewModel(): BaseViewModel? {
        return reportsViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        setUpUI()
        setObservers()
    }

    override fun onPostResume() {
        super.onPostResume()

        ivBack.setOnClickListener {
           finish()
        }
       tvSpinner.setOnClickListener {
           spinner.performClick()
        }
       startDateTV.setOnClickListener {

            startDatePickerInstance = if (reportsViewModel.startDateTimestamp.value!! > 0) {
                getDatePicker(this, dateSelected, reportsViewModel.startDateTimestamp.value!!, false)
            } else {
                getDatePicker(this, dateSelected, false)
            }

            startDatePickerInstance.datePicker.minDate = getMinDateForReports()

            startDatePickerInstance.datePicker.maxDate = Calendar.getInstance().timeInMillis
            startDatePickerInstance.show()
        }

       endDateTV.setOnClickListener {
            endDatePickerInstance = if (reportsViewModel.startDateTimestamp.value!! > 0) {
                getDatePicker(this, dateSelected, reportsViewModel.startDateTimestamp.value!!, true)
            } else {
                getDatePicker(this, dateSelected, true)
            }
            endDatePickerInstance.datePicker.maxDate = if (reportsViewModel.startDateTimestamp.value!! > 0) {
                reportsViewModel.startDateTimestamp.value!!
            } else {
                Calendar.getInstance().timeInMillis
            }

            endDatePickerInstance.datePicker.minDate = getMinDateForReports()
            endDatePickerInstance.show()
        }
    }

    private fun setUpUI() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        tvTitle.text = getString(R.string.title_reports)
        fileTypeDropDownAdapter = CustomSpinnerAdapter(this, reportsViewModel.fileTypeArray)
        // Specify the layout to use when the list of choices appears
        fileTypeDropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
       spinner.adapter = fileTypeDropDownAdapter
       spinner.onItemSelectedListener = this
    }

    private fun setObservers() {
        reportsViewModel.isEndDateSetMLD.observe(this, {
            reportsViewModel.areAllFieldsSet.value = reportsViewModel.isEndDateSetMLD.value!! && reportsViewModel.isStartDateSetMLD.value!! && reportsViewModel.isFileTypeSetMLD.value!!
        })

        reportsViewModel.isStartDateSetMLD.observe(this, {

            reportsViewModel.areAllFieldsSet.value = reportsViewModel.isEndDateSetMLD.value!! && reportsViewModel.isStartDateSetMLD.value!! && reportsViewModel.isFileTypeSetMLD.value!!
        })

        reportsViewModel.isFileTypeSetMLD.observe(this, {

            reportsViewModel.areAllFieldsSet.value = reportsViewModel.isEndDateSetMLD.value!! && reportsViewModel.isStartDateSetMLD.value!! && reportsViewModel.isFileTypeSetMLD.value!!
        })

        reportsViewModel.areAllFieldsSet.observe(this, { allFieldsSet ->
           downloadBTN.isEnabled = allFieldsSet
           downloadBTN.alpha = if (allFieldsSet) 1.0f
            else 0.4f
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position > 0) {
            reportsViewModel.isFileTypeSetMLD.value = true
            reportsViewModel.selectedFileType = reportsViewModel.fileTypeArray[position]
           tvSpinner.text = reportsViewModel.selectedFileType
           tvSpinner.setTextColor(Color.BLACK)
           fileTypeHintTV.visibility = View.VISIBLE
        } else {
            reportsViewModel.isFileTypeSetMLD.value = false
            reportsViewModel.selectedFileType = ""
           tvSpinner.text = reportsViewModel.selectedFileType
           fileTypeHintTV.visibility = View.GONE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private val dateSelected = { dateSelected: Long, isEndDate: Boolean ->
        if (!isEndDate) {
            reportsViewModel.startDateTimestamp.value = dateSelected
            reportsViewModel.isStartDateSetMLD.value = true
           startDateTV.text = DateFormatter.covertMiliIntoDate(dateSelected.toString(), null)
        }
        else {
            reportsViewModel.endDateTimestamp.value = dateSelected
            reportsViewModel.isEndDateSetMLD.value = true
           endDateTV.text = DateFormatter.covertMiliIntoDate(dateSelected.toString(), null)
        }
    }
}