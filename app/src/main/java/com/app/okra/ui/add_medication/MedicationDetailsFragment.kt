package com.app.okra.ui.add_medication

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.MedicationRepoImpl
import com.app.okra.extension.beGone
import com.app.okra.extension.beVisible
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.MedicationData
import com.app.okra.ui.logbook.medication.MedicationLogsFragment
import com.app.okra.ui.logbook.medication.MedicationViewModel
import com.app.okra.utils.*
import kotlinx.android.synthetic.main.fragment_save_medication.*
import kotlinx.android.synthetic.main.layout_button.view.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.row_test_or_meal_logs.view.*
import java.util.*

class MedicationDetailsFragment : BaseFragment() {

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

    private var screenFrom: String?=null
    private var medicationData: MedicationData?=null
    private var medicationType: String?=null
    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                MedicationViewModel(MedicationRepoImpl(apiServiceAuth))
            }
        ).get(MedicationViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()

        setViews()
        setListener()
        setObserver()
    }

    private fun getData() {

        arguments?.let { it ->

            if(it.containsKey(AppConstants.DATA)){
                medicationData = it.getParcelable(AppConstants.DATA)
            }

            if(it.containsKey(AppConstants.Intent_Constant.FROM_SCREEN)){
                screenFrom = it.getString(AppConstants.Intent_Constant.FROM_SCREEN)
            }

            if(it.containsKey(AppConstants.MEDICATION_TYPE)) {
                medicationType = it.getString(AppConstants.MEDICATION_TYPE)
            }
        }
    }

    private fun setViews() {
        tvTitle.text = getString(R.string.medication)
        ivRight.beVisible()


        if(screenFrom.equals(MedicationLogsFragment::class.java.simpleName)){
            // Coming to update or delete medication
            ivRight.beVisible()
            ivDelete.beVisible()
            layout_button.btnCommon.text = getString(R.string.share)
            clEditional.beVisible()

        }else{
            // Coming to add medication
            ivRight.beGone()
            ivDelete.beGone()
            layout_button.btnCommon.text = getString(R.string.save)
            clEditional.beGone()
        }

        medicationData?.let {
            if(!it.medicineName.isNullOrEmpty()) {
                tvTitle.text = it.medicineName
            }

            if(!it.unit.isNullOrEmpty()) {
                val unit: String = if (it.unit.equals(AppConstants.MG))
                    getString(R.string.mg)
                else
                    getString(R.string.pills)
                tvUnitValue.text = unit
            }

            if(it.quantity!=null) {
                tvQuantityValue.text = it.quantity.toString()
            }

            if(!it.createdAt.isNullOrEmpty()) {
                tvDateValue.text = getDateTimeFromISO(
                    it.createdAt!!,
                    formatYouWant = "MMM dd yyyy")
            }else{
                val cal = Calendar.getInstance()
                tvDateValue.text =
                    getDateFromTimeStamp(
                        cal.timeInMillis,
                        formatYouWant = AppConstants.DateFormat.DATE_FORMAT_4
                    )
            }

            if(screenFrom.equals(MedicationLogsFragment::class.java.simpleName)){

                if(it.tags!=null) {
                    tvTagsValue.text = it.tags.toString()
                }
                if(it.feelings!=null) {
                    tvFeelingValues.text = it.feelings.toString()
                }

                if(!it.image.isNullOrEmpty()){
                    val adapter = ImageAdapter(requireContext(), it.image!!, screenType= MedicationDetailsFragment::class.java.simpleName)

                    val layoutManager = GridLayoutManager(requireContext(), 3)
                   // val layoutManager = LinearLayoutManager(requireContext())
                    rvImages.layoutManager = layoutManager
                    rvImages.adapter = adapter
                }
            }
        }
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.setResult(RESULT_CANCELED)
            activity?.finish()
        }

        ivRight.setOnClickListener {
            val bundle = Bundle()
            medicationData?.let {
                bundle.putParcelable(AppConstants.DATA, it)
            }
            navController.navigate(R.id.action_saveMedication_to_editMedicationFragment,bundle)
        }
        ivDelete.setOnClickListener {
            showAlertDialog(requireContext(), object : Listeners.DialogListener {
                override fun onOkClick(dialog: DialogInterface?) {
                    medicationData?.let {
                        viewModel.deleteMedication(it._id!!)
                    }
                   dialog?.dismiss()
                }

                override fun onCancelClick(dialog: DialogInterface?) {
                    dialog?.dismiss()
                }
            }, MessageConstants.Messages.msg_are_you_sure,true)

        }

        layout_button.btnCommon.setOnClickListener {

            if(screenFrom.equals(MedicationLogsFragment::class.java.simpleName)){
                showToast(MessageConstants.Messages.work_in_progress)
            }else {
                val unitToSend = if (tvUnitValue.text.toString() == getString(R.string.mg))
                    AppConstants.MG
                else
                    AppConstants.PILLES
                viewModel.addMedication(
                    tvTitle.text.toString(),
                    unitToSend,
                    tvQuantityValue.text.toString().toInt(),
                    medicationType!!
                )
            }
        }
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, observeError = false)
        viewModel._addMedicationLiveData.observe(viewLifecycleOwner) { it ->
            showToast(getString(R.string.saved_successfully))
            activity?.finish()
        }
        viewModel._deleteMedicationLiveData.observe(viewLifecycleOwner) { it ->
            val intent = Intent()
            intent.putExtra(AppConstants.Intent_Constant.RELOAD_SCREEN, "true")
            activity?.setResult(RESULT_OK,intent)
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