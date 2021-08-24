package com.app.okra.ui.logbook.meal_detail

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.utils.Listeners
import com.app.okra.utils.showCustomAlertDialog
import kotlinx.android.synthetic.main.layout_header.*

class MealDetailsFragment : BaseFragment(), Listeners.DialogListener {

    override fun getViewModel(): BaseViewModel? {
        return null
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
            navController.navigate(R.id.action_mealDetails_to_editMealDetails, null)
        }

        ivDelete.setOnClickListener {
            showCustomAlertDialog(context,this,getString(R.string.are_you_sure_you_want_to_delete_the_added_meal),true,getString(R.string.cancel),getString(R.string.delete),getString(R.string.delete_meal))
        }
    }

    override fun onOkClick(dialog: DialogInterface?) {

    }

    override fun onCancelClick(dialog: DialogInterface?) {

    }

}