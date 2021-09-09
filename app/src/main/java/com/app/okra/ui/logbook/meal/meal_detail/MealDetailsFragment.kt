package com.app.okra.ui.logbook.meal.meal_detail

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.MealLogsRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.MealData
import com.app.okra.ui.logbook.meal.MealLogsViewModel
import com.app.okra.utils.*
import kotlinx.android.synthetic.main.fragment_meal_details.*
import kotlinx.android.synthetic.main.layout_header.*

class MealDetailsFragment : BaseFragment(), Listeners.DialogListener {

    private var data: MealData? = null

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                MealLogsViewModel(MealLogsRepoImpl(apiServiceAuth))
            }
        ).get(MealLogsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setListener()
        getData()
        setObserver()
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.meal_details)
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
            navController.navigate(R.id.action_mealDetails_to_editMealDetails, bundle)
        }

        ivDelete.setOnClickListener {
            showCustomAlertDialog(
                context,
                this,
                getString(R.string.are_you_sure_you_want_to_delete_the_added_meal),
                true,
                positiveButtonText=   getString(R.string.delete),
                negativeButtonText = getString(R.string.cancel),
                title = getString(R.string.delete_meal)
            )

        }
    }

    private fun getData() {
        arguments?.let { it ->
            data = it.getParcelable("data")

            tvDateValue.text =
                data?.createdAt?.let { it1 ->
                    getDateFromISOInString(
                        it1,
                        formatYouWant = "MMM dd yyyy"
                    )
                }

            data?.foodType?.let {
                tvFoodTypeValue.text = it.value?.let { it1 -> getMealTime(it1) }
            }

            tvCaloriesValue.text = data?.calories?.value + " Cal"
            tvCarbsValue.text = data?.carbs?.value  + " gm"
            tvFatValue.text = data?.fat?.value + " gm"
            tvProteinvalue.text = data?.protien?.value + " gm"
        }
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this)
        viewModel._deleteMealLiveData.observe(viewLifecycleOwner) { it ->
            requireActivity().apply{
                setResult(AppConstants.RequestOrResultCodes.RESULT_CODE_MEAL_LOG_DELETED)
                finish()
            }
        }

    }

    override fun onOkClick(dialog: DialogInterface?) {
        viewModel.deleteMeal(data?._id!!)
        dialog?.dismiss()
    }

    override fun onCancelClick(dialog: DialogInterface?) {
        dialog?.dismiss()
    }
}