package com.app.okra.ui.logbook.test_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.extension.beVisible
import kotlinx.android.synthetic.main.layout_header.*

class EditTestDetailsFragment : BaseFragment() {
    override fun getViewModel(): BaseViewModel? {
        return null
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
        setUpToolbar()
        setListener()
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.edit_test_details)
        btnSave.beVisible()
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.finish()
        }

        btnSave.setOnClickListener {
            navController.navigate(R.id.action_editTestDetails_to_successfulUpdatedFragment, null)
        }
    }
}