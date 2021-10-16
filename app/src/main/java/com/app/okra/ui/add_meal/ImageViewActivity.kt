package com.app.okra.ui.add_meal

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.extension.loadUserImageFromUrl
import com.app.okra.models.FoodRecognintionResponse
import com.app.okra.models.Items
import com.app.okra.models.Results
import com.app.okra.ui.add_meal.contract.AddMealContracts
import com.app.okra.utils.Listeners
import com.app.okra.utils.showCustomAlertDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_image_view.*
import kotlinx.android.synthetic.main.bottomsheet_choose_item.*


class ImageViewActivity : BaseActivity(), Listeners.DialogListener {

    var data: FoodRecognintionResponse? = null

    private lateinit var foodNameAdapter: FoodItemNameAdapter
    private lateinit var foodTypeAdapter: FoodItemAdapter
    private val foodNameList by lazy {  ArrayList<Results>() }
    private val foodTypeList by lazy {  ArrayList<Items>() }



    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        getData()
    }

    private fun getData() {
        intent.let {
            val mealInput =  it.getParcelableExtra<MealInput>(AddMealContracts.data)
            iv_image.loadUserImageFromUrl(this, mealInput!!.image)

            if (mealInput.invalid) {
                showCustomAlertDialog(
                    this,
                    this,
                    getString(R.string.this_does_not_look_like_photo),
                    false,
                    positiveButtonText = getString(R.string.retake),
                    negativeButtonText = "",
                    title = getString(R.string.invalid_photo)
                )
            }
            else {
                println("::::: ImageView")

                data = mealInput.data
                showBottomSheetDialog()
            }
        }
    }

    override fun onOkClick(dialog: DialogInterface?) {
        dialog?.dismiss()
        setResult(100)
        finish()
    }

    override fun onCancelClick(dialog: DialogInterface?) {
        dialog?.dismiss()
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.apply {
            setContentView(R.layout.bottomsheet_choose_item)
            setupFullHeight(bottomSheetDialog)
            show()


            val layoutManager = LinearLayoutManager(
                bottomSheetDialog.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            if (data?.results != null) {
                foodNameList.clear()
                foodNameList.addAll(data?.results!!)
                foodNameList[0].isSelected=true
                foodNameAdapter = FoodItemNameAdapter(foodNameList,
                    object : Listeners.ItemClickListener {
                        override fun onSelect(o: Any?, o1: Any?) {
                            val pos = o as Int
                            updateSelectedFoodName(pos)
                            foodTypeList.clear()
                            foodTypeList.addAll(foodNameList[pos].items!!)
                            foodTypeAdapter.notifyDataSetChanged()
                        }

                        override fun onUnSelect(o: Any?, o1: Any?) {}
                    })

                rv_item_names.layoutManager = layoutManager
                rv_item_names.adapter = foodNameAdapter
                rv_item_names.scrollToPosition(0)

                foodTypeList.clear()
                foodTypeList.addAll(data?.results!![0].items!!)

                foodTypeAdapter = FoodItemAdapter(
                    foodTypeList,
                    object : Listeners.ItemClickListener {
                        override fun onSelect(o: Any?, o1: Any?) {
                            val pos = o as Int


                        }

                        override fun onUnSelect(o: Any?, o1: Any?) {}

                    })
                rv_item_types.adapter = foodTypeAdapter
                rv_item_types.scrollToPosition(0)

            }
        }
    }

    private fun updateSelectedFoodName(pos: Int) {
        if(foodNameList.size>0){
            for((i, data) in foodNameList.withIndex()){
                if(i !=pos){
                    data.isSelected = false
                }
            }
            foodNameList[pos].isSelected = true
        }
        foodNameAdapter.notifyDataSetChanged()
    }

    private fun setupFullHeight(bottomSheet: BottomSheetDialog) {

        val height = Resources.getSystem().displayMetrics.heightPixels
        val parentLayout =
            bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        parentLayout?.let { it ->
            val behaviour = BottomSheetBehavior.from(it)
            val layoutParams = it.layoutParams
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.layoutParams = layoutParams
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

}