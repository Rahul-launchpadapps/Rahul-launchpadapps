package com.app.okra.ui.add_medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.MedicationRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.ui.logbook.medication.MedicationViewModel
import com.app.okra.utils.*
import kotlinx.android.synthetic.main.fragment_save_medication.*
import kotlinx.android.synthetic.main.layout_button.view.*
import kotlinx.android.synthetic.main.layout_header.*
import java.util.*

class SaveMedicationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_medication, container, false)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                MedicationViewModel(MedicationRepoImpl(apiServiceAuth))
            }
        ).get(MedicationViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setListener()
        getData()
        setObserver()
    }

    private fun getData() {
        arguments?.let { it ->
            tvTitle.text = it.getString(AppConstants.NAME)
            val unit: String
            if(it.getString(AppConstants.UNIT).equals(AppConstants.MG))
                unit = getString(R.string.mg)
            else
                unit = getString(R.string.pills)
            tvUnitValue.text = unit
            tvQuantityValue.text = it.getInt(AppConstants.QUANTITY).toString()
            val cal = Calendar.getInstance()
            tvDateValue.text =
                getDateFromTimeStamp(
                    cal.timeInMillis,
                    formatYouWant = AppConstants.DateFormat.DATE_FORMAT_4
                )
        }
    }

    private fun setViews() {
        tvTitle.text = getString(R.string.medication)
        layout_button.btnCommon.text = getString(R.string.save)
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.finish()
        }

        layout_button.btnCommon.setOnClickListener {
            viewModel.addMedication(tvTitle.text.toString(),tvUnitValue.text.toString(),tvQuantityValue.text.toString().toInt())
        }
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, observeError = false)
        viewModel._addMedicationLiveData.observe(viewLifecycleOwner) { it ->
            showToast(getString(R.string.saved_successfully))
            activity?.finish()
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

}