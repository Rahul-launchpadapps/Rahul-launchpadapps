package com.app.okra.ui.logbook.meal.meal_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.MealLogsRepoImpl
import com.app.okra.extension.beVisible
import com.app.okra.extension.loadUserImageFromUrl
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.CommonData
import com.app.okra.models.FoodItemsRequest
import com.app.okra.models.MealData
import com.app.okra.ui.logbook.meal.MealLogsViewModel
import com.app.okra.utils.AppConstants
import com.app.okra.utils.getDateFromISOInString
import com.app.okra.utils.getMealTime
import kotlinx.android.synthetic.main.fragment_edit_meal_details.*
import kotlinx.android.synthetic.main.fragment_edit_meal_details.tvCaloriesValue
import kotlinx.android.synthetic.main.fragment_edit_meal_details.tvCarbsValue
import kotlinx.android.synthetic.main.fragment_edit_meal_details.tvDateValue
import kotlinx.android.synthetic.main.fragment_edit_meal_details.tvFatValue
import kotlinx.android.synthetic.main.fragment_edit_meal_details.tvFoodTypeValue
import kotlinx.android.synthetic.main.layout_header.*
import java.sql.Array

class EditMealDetailsFragment : BaseFragment() {

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
        return inflater.inflate(R.layout.fragment_edit_meal_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        getData()
        setListener()
        setObserver()
    }

    private fun setUpToolbar() {
        btnSave.beVisible()
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.finish()
        }

        btnSave.setOnClickListener {
            var mealId = ""
            var date = ""
            var image = ""
            var foodItemsRequest :FoodItemsRequest
            var foodList : ArrayList<FoodItemsRequest> = ArrayList()

            data?.apply {
                mealId = if (!_id.isNullOrEmpty()) {
                    _id!!
                } else ""
                date = if (!this.date.isNullOrEmpty()) {
                    this.date!!
                } else ""
                image = if (!this.image.isNullOrEmpty()) {
                    this.image!!
                } else ""

                foodItemsRequest = FoodItemsRequest(foodItems?.get(0)?.item,
                    foodItems?.get(0)?.type,
                    foodItems?.get(0)?.servingSize)
                foodList.add(foodItemsRequest)
            }

            viewModel.prepareUpdateRequest(
                mealsId  = mealId,
                date = date,
                image = image,
                foodItems = foodList,
                foodType = data?.foodType,
                calories = CommonData(tvCaloriesValue.text.toString(),data?.calories?.unit),
                carbs = CommonData(tvCarbsValue.text.toString(),data?.carbs?.unit),
                fat = CommonData(tvFatValue.text.toString(),data?.fat?.unit),
                protein = CommonData(tvProteinValue.text.toString(),data?.protien?.unit),
            )
            viewModel.updateMeal()
        }
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this)
        viewModel._updateMealLiveData.observe(viewLifecycleOwner) {
            val bundle = Bundle()
            bundle.putString(AppConstants.FROM,AppConstants.MEAL)
            navController.navigate(
                R.id.action_editMealDetails_to_successfulUpdatedFragment,
                bundle
            )
        }
    }

    private fun getData() {
        arguments?.let { it ->
            data = it.getParcelable(AppConstants.DATA)

            tvDateValue.text =
                data?.createdAt?.let { it1 ->
                    getDateFromISOInString(
                        it1,

                        formatYouWant = "MMM dd yyyy"
                    )
                }

            data?.image?.let {
                ivFood.loadUserImageFromUrl(
                    requireContext(),
                    it,
                    R.mipmap.ic_person_placeholder_bg
                )
            }

            data?.foodType?.let {
                tvFoodTypeValue.text = it.let { it }
            }

            tvCaloriesValue.setText(String.format("%.2f",data?.calories?.value!!.toBigDecimal()))
            tvCarbsValue.setText(String.format("%.2f",data?.carbs?.value!!.toBigDecimal()) )
            tvFatValue.setText(String.format("%.2f",data?.fat?.value!!.toBigDecimal()))
            tvProteinValue.setText(String.format("%.2f",data?.protien?.value!!.toBigDecimal()))
        }
    }

}