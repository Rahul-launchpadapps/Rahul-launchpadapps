package com.app.okra.ui.logbook.test_details

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.TestLogsRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.Data
import com.app.okra.ui.logbook.TestLogsViewModel
import com.app.okra.utils.Listeners
import com.app.okra.utils.getDateFromISOInString
import com.app.okra.utils.showCustomAlertDialog
import kotlinx.android.synthetic.main.fragment_test_details.*
import kotlinx.android.synthetic.main.layout_header.*

class TestDetailsFragment : BaseFragment(), Listeners.DialogListener {

    private var data: Data? = null

    override fun getViewModel(): BaseViewModel {
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
        return inflater.inflate(R.layout.fragment_test_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setListener()
        getData()
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.test_details)
        ivRight.visibility = View.VISIBLE
        ivDelete.visibility = View.VISIBLE
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.finish()
        }

        ivRight.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("data", arguments?.getParcelable("data"))
            navController.navigate(R.id.action_testDetails_to_editTestDetails, bundle)
        }

        ivDelete.setOnClickListener {
            showCustomAlertDialog(context,this,getString(R.string.are_you_sure_you_want_to_delete_the_added_test),true,getString(R.string.cancel),getString(R.string.delete),getString(R.string.delete_test))
        }
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
            tvTestingTimeValue.text = data?.testingTime ?: ""
            tvBloodGlucoseValue.text = data?.bloodGlucose + " mg/dL"
            tvBloodPressureValue.text = data?.datbloodPressuree  + " mmHg"
            tvInsulinValue.text = data?.insulin ?: ""
            tvDeviceIdValue.text = data?.deviceId ?: ""
            tvDeviceNameValue.text = data?.deviceName ?: ""
        }
    }

    override fun onOkClick(dialog: DialogInterface?) {

    }

    override fun onCancelClick(dialog: DialogInterface?) {

    }

}