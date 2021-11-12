package com.app.okra.ui.add_medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_search_medication.*
import kotlinx.android.synthetic.main.layout_header.*

class SearchMedicationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_medication, container, false)
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setListener()
    }

    private fun setViews() {
        tvTitle.text = getString(R.string.medication)
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.finish()
        }

        tvAddMed.setOnClickListener {
            navController.navigate(R.id.action_searchMed_to_addMed, null)
        }
    }
}