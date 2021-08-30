package com.app.okra.ui.logbook.test.test_details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.TestLogsRepoImpl
import com.app.okra.extension.beGone
import com.app.okra.extension.beInvisible
import com.app.okra.extension.beVisible
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.Data
import com.app.okra.ui.logbook.test.TestLogsViewModel
import com.app.okra.ui.my_account.setting.measurement.CustomSpinnerAdapter
import com.app.okra.utils.*
import kotlinx.android.synthetic.main.fragment_edit_test_details.*
import kotlinx.android.synthetic.main.fragment_edit_test_details.spinner
import kotlinx.android.synthetic.main.fragment_edit_test_details.tvBloodGlucoseValue
import kotlinx.android.synthetic.main.fragment_edit_test_details.tvBloodPressureValue
import kotlinx.android.synthetic.main.fragment_edit_test_details.tvDateValue
import kotlinx.android.synthetic.main.fragment_edit_test_details.tvDeviceIdValue
import kotlinx.android.synthetic.main.fragment_edit_test_details.tvDeviceNameValue
import kotlinx.android.synthetic.main.fragment_edit_test_details.tvInsulinValue
import kotlinx.android.synthetic.main.fragment_edit_test_details.tvTestingTimeValue
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.btnSave

class EditTestDetailsFragment : BaseFragment() , View.OnClickListener{

    private lateinit var customSpinnerAdapter: CustomSpinnerAdapter
    private var data: Data? = null

    private val timingList by lazy {
        arrayListOf<String>()
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                TestLogsViewModel(TestLogsRepoImpl(apiServiceAuth))
            }
        ).get(TestLogsViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_test_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        getData()
        setView()
        setAdapter()
        setObserver()
    }

    private fun setAdapter() {
        timingList.add(AppConstants.SELECT_TESTING_TIME)
        timingList.add(AppConstants.BEFORE_MEAL_TEXT)
        timingList.add(AppConstants.AFTER_MEAL_TEXT)
        timingList.add(AppConstants.POST_MEDICINE_TEXT)
        timingList.add(AppConstants.POST_WORKOUT_TEXT)
        timingList.add(AppConstants.CONTROLE_SOLUTION_TEXT)

        customSpinnerAdapter = CustomSpinnerAdapter(requireActivity(), timingList)
        spinner.adapter = customSpinnerAdapter

        var index = 0

        data?.testingTime?.let {

            if(it.isNotEmpty()){
                for((i, data) in timingList.withIndex()){
                    if(getMealTime(it) == data){
                        index = i
                        break;
                    }
                }
            }
        }
        //spinner.setSelection(index)
        tvSetTestingTime.text = timingList[index]
    }

    private fun setView() {
        ivRight.beInvisible()
        ivDelete.beGone()
        btnSave.beVisible()
        tvTitle.text = getString(R.string.edit_test_details)
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this)
        viewModel._updateTestLiveData.observe(viewLifecycleOwner) { it ->
            navController.navigate(
                R.id.action_editTestDetails_to_successfulUpdatedFragment,
                null
            )
        }
    }

    private fun setListener() {
        ivBack.setOnClickListener(this)

        tvSetTestingTime.setOnClickListener(this)

        btnSave.setOnClickListener (this)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tvSetTestingTime.text = timingList[p2]
                if(p2 ==0 && etNotes.text.isNullOrEmpty()){
                    btnSave.beGone()
                }else{
                    btnSave.beVisible()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        etNotes.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrEmpty() && tvSetTestingTime.text.toString().trim() == AppConstants.SELECT_TESTING_TIME){
                    btnSave.beGone()
                }else{
                    btnSave.beVisible()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun getData() {
        arguments?.let {
            data = it.getParcelable("data")

            tvDateValue.text =
                data?.createdAt?.let { it1 ->
                    getDateFromISOInString(
                        it1,
                        formatYouWant = "MMM dd yyyy"
                    )
                }
            data?.testingTime?.let {
                tvTestingTimeValue.text = getMealTime(it)
            }
            tvBloodGlucoseValue.text = data?.bloodGlucose + " mg/dL"
            tvBloodPressureValue.text = data?.datbloodPressuree  + " mmHg"
            tvInsulinValue.text = data?.insulin ?: ""
            tvDeviceIdValue.text = data?.deviceId ?: ""
            tvDeviceNameValue.text = data?.deviceName ?: ""
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ivBack -> activity?.finish()
            R.id.tvSetTestingTime -> spinner.performClick()
            R.id.btnSave -> {
                var bloodGlucose = 0
                var bloodPresure = 0
                var insulin = 0
                var testId = ""
                var testingTime = ""
                data?.apply {
                    bloodGlucose = if (!this.bloodGlucose.isNullOrEmpty()) {
                        this.bloodGlucose!!.toInt()
                    } else 0
                    bloodPresure = if (!this.datbloodPressuree.isNullOrEmpty()) {
                        this.datbloodPressuree!!.toInt()
                    } else 0
                    insulin = if (!this.insulin.isNullOrEmpty()) {
                        this.insulin!!.toInt()
                    } else 0
                    testId = if (!_id.isNullOrEmpty()) {
                        _id!!
                    } else ""
                    testId = if (!_id.isNullOrEmpty()) {
                        _id!!
                    } else ""
                    val newTestTime = tvSetTestingTime.text.toString().trim()
                    testingTime =
                        if (newTestTime.isEmpty() || newTestTime == AppConstants.SELECT_TESTING_TIME) ({
                            if(!this.testingTime.isNullOrEmpty()){
                                this.testingTime
                            }else ""
                        }).toString()
                        else {
                           getMealTime(newTestTime, false)
                        }

                }

                val mealAfter = mutableListOf<String>()
                mealAfter.add("123")
                mealAfter.add("321")
                val mealBefore= mutableListOf<String>()
                mealBefore.add("AAA")
                mealBefore.add("BBB")

                viewModel.prepareUpdateRequest(
                    testId = testId,
                    bloodGlucose = bloodGlucose,
                    bloodPressure = bloodPresure,
                    insulin = insulin,
                    additionalNotes = etNotes.text.toString().trim(),
                    mealsAfter = null,
                    mealsBefore = null,
                    testingTime = testingTime
                )
                viewModel.updateTest()

            }
        }
    }


}