package com.app.okra.ui.my_account.setting.measurement

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.preference.PreferenceManager
import com.app.okra.data.repo.SettingRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.ui.my_account.setting.SettingsViewModel
import com.app.okra.utils.AppConstants
import kotlinx.android.synthetic.main.activity_measurements_setting.*
import kotlinx.android.synthetic.main.layout_button.*
import kotlinx.android.synthetic.main.layout_header.*


class MeasurementSettingActivity : BaseActivity(), View.OnClickListener{


    private lateinit var customSpinner: CustomSpinnerAdapter
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            SettingsViewModel(SettingRepoImpl(apiServiceAuth))
        }).get(SettingsViewModel::class.java)
    }
    private var isHypoEdited = false
    private var isHyperEdited = false
    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val bloodGlucoseList by lazy {
        arrayListOf<String>()
    }

    private var bloodGlucoseUnit = PreferenceManager.getString(AppConstants.Pref_Key.BLOOD_GLUCOSE_UNIT)
    private var hyperBloodGlucoseUnit = PreferenceManager.getInt(AppConstants.Pref_Key.HYPER_BLOOD_GLUCOSE_UNIT)
    private var hypoBloodGlucoseUnit = PreferenceManager.getInt(AppConstants.Pref_Key.HYPO_BLOOD_GLUCOSE_UNIT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurements_setting)
        setViews()
        setAdapter()
        setListener()
        setObserver()
    }

    private fun setAdapter() {

        customSpinner = CustomSpinnerAdapter(this, bloodGlucoseList)
        spinner.adapter = customSpinner

        var index = 0
        if(!bloodGlucoseUnit.isNullOrEmpty()){
            for((i, data) in bloodGlucoseList.withIndex()){
                if(bloodGlucoseUnit == data){
                    index = i
                    break;
                }
            }
        }
        spinner.setSelection(index)
        tvSpinner.text = bloodGlucoseList[index]
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, this)
        viewModel._settingLiveData.observe(this){
            PreferenceManager.putString(AppConstants.Pref_Key.BLOOD_GLUCOSE_UNIT, tvSpinner.text.toString())

            if(!etHyperBlood.text.isNullOrEmpty())
                PreferenceManager.putInt(AppConstants.Pref_Key.HYPER_BLOOD_GLUCOSE_UNIT,
                        etHyperBlood.text.toString().toInt())
            if(!etHypoBlood.text.isNullOrEmpty())
                PreferenceManager.putInt(AppConstants.Pref_Key.HYPO_BLOOD_GLUCOSE_UNIT,
                        etHypoBlood.text.toString().toInt())
            finish()
        }
    }

    private fun setListener() {
        ivBack.setOnClickListener(this)
        btnCommon.setOnClickListener(this)
        tvSpinner.setOnClickListener(this)
        setTextChangeListener()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tvSpinner.text = bloodGlucoseList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

    }

    private fun setTextChangeListener() {
     /*   etHyperBlood.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    var valueToModify = p0.toString()
                    val unit = tvSpinner.text.toString()
                    if (!isHyperEdited) {
                        isHyperEdited = true

                        if (valueToModify.contains(unit)) {
                            valueToModify = valueToModify.replace(unit, "")
                        }
                        val textToSet = "$valueToModify ${tvSpinner.text}"
                        etHyperBlood.setText(textToSet)
                 //       etHyperBlood.setText(etHyperBlood.text!!.length)
                    }else{
                        isHyperEdited = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        etHypoBlood.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!p0.isNullOrEmpty()) {
                    var valueToModify = p0.toString()
                    val unit = tvSpinner.text.toString()
                    if (!isHypoEdited) {
                        isHypoEdited = true

                        if (valueToModify.contains(unit)) {
                            valueToModify = valueToModify.replace(unit, "")
                        }
                        val textToSet = "$valueToModify ${tvSpinner.text}"
                        etHypoBlood.setText(textToSet)
                     //   etHypoBlood.setText(etHypoBlood.text!!.length)
                    }else{
                        isHypoEdited = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
*/

    }

    private fun setViews() {
        bloodGlucoseList.add("mg/dL")
        bloodGlucoseList.add("mmol/dL")
        tvTitle.text = getString(R.string.title_measurement_setting)
        btnCommon.text = getString(R.string.save)

        if(hyperBloodGlucoseUnit!=0) {
            etHyperBlood.setText(hyperBloodGlucoseUnit.toString())
        }

        if(hypoBloodGlucoseUnit!=0) {
            etHypoBlood.setText(hypoBloodGlucoseUnit.toString())
        }

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                finish()
            }
            R.id.tvSpinner -> {
                spinner.performClick()
            }
            R.id.btnCommon -> {
                val hyper = if (!etHyperBlood.text.isNullOrEmpty()) {
                    etHyperBlood.text.toString().trim().toInt()
                } else {
                    0
                }
                val hypo = if (!etHypoBlood.text.isNullOrEmpty()) {
                    etHypoBlood.text.toString().trim().toInt()
                } else {
                    0
                }
                viewModel.setSettingRequest(
                        bloodGlucoseUnit = tvSpinner.text.toString(),
                        hyperBloodGlucoseValue = hyper,
                        hypoBloodGlucoseValue = hypo
                )
                viewModel.updateSettings()
            }
        }
    }

}