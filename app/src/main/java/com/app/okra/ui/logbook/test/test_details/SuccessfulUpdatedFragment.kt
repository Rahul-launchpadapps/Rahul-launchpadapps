package com.app.okra.ui.logbook.test.test_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.utils.AppConstants.RequestOrResultCodes.RESULT_CODE_TEST_LOG_UPDATED
import kotlinx.android.synthetic.main.layout_button.*

class SuccessfulUpdatedFragment : BaseFragment() {
    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_successful_updated, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCommon.text = getString(R.string.ok)
        setListener()
    }

    private fun setListener() {
        btnCommon.setOnClickListener{
            requireActivity().apply{
                setResult(RESULT_CODE_TEST_LOG_UPDATED)
                finish()
            }
        }
    }
}