package com.app.okra.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.okra.R
import com.app.okra.base.BaseFragmentWithoutNav
import com.app.okra.base.BaseViewModel

class HomeFragment : BaseFragmentWithoutNav() {

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}