package com.app.okra.ui.my_reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.utils.showCustomAlertDialog
import kotlinx.android.synthetic.main.fragment_my_reminder.*

class MyReminderFragment : BaseFragment() {

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener() {
        clDiabetesTest.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("data", "diabetes")
            navController.navigate(R.id.action_myReminderFragment_to_setReminderFragment, bundle)
        }

        clAddFood.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("data", "food")
            navController.navigate(R.id.action_myReminderFragment_to_setReminderFragment, bundle)
        }
    }

}