package com.app.okra.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.DashboardRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.utils.navigateToLogin
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment(),View.OnClickListener{

    private val viewModel by lazy {
        ViewModelProvider(this,
                viewModelFactory {
                    DashboardViewModel(DashboardRepoImpl(apiServiceAuth))
                }
        ).get(DashboardViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? {
        return  viewModel
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLogout.setOnClickListener(this)
        setObserverData()
    }

    private fun setObserverData() {
        viewModel._logoutLiveData.observe(viewLifecycleOwner){
            navigateToLogin(requireActivity())
            requireActivity().finish()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tvLogout -> {
                viewModel.onLogout()
            }
        }
    }
}