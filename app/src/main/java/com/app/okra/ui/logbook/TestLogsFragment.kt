package com.app.okra.ui.logbook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.TestLogsRepoImpl
import com.app.okra.extension.beGone
import com.app.okra.extension.beVisible
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.SupportResponse
import com.app.okra.utils.Listeners
import kotlinx.android.synthetic.main.fragment_test_logs.*
import kotlinx.android.synthetic.main.fragment_test_logs.progressBar_loadMore

class TestLogsFragment : BaseFragment(),  Listeners.ItemClickListener {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestAdapter: TestLogsAdapter
    private var pageNo :Int = 1
    private var totalPage: Int = 0
    private var nextHit: Int = 0
    private val requestList  = ArrayList<SupportResponse>()

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                TestLogsViewModel(TestLogsRepoImpl(apiServiceAuth))
            }
        ).get(TestLogsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_logs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        getData()
        setObserver()
        setListener()
    }

    private fun getData() {
        viewModel.getTestLogs()
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, observeError = false)
        viewModel._profileInfoLiveData.observe(viewLifecycleOwner) { it ->
          //  swipe_request.isRefreshing = false

            if (it.totalPage != null) {
                totalPage = it.totalPage
            }
            if (it.nextHit != null) {
                nextHit = it.nextHit
            }
            it.data?.let{
                if (pageNo == 1 && requestList.size > 0)
                    requestList.clear()

               // requestList.addAll(it)
                requestAdapter.notifyDataSetChanged()
            }
            manageViewVisibility()
        }

        viewModel._errorObserver.observe(viewLifecycleOwner){
           // swipe_request.isRefreshing = false
        }
    }

    private fun manageViewVisibility() {
        if(requestList.isNullOrEmpty()){
            tvNoTestLogged.beVisible()
            rv_test_list.beGone()
        }else{
            tvNoTestLogged.beGone()
            rv_test_list.beVisible()
        }
    }

    private fun setAdapter() {
        requestAdapter = TestLogsAdapter(
            this,
            requestList
        )
        layoutManager =LinearLayoutManager(requireContext())
        rv_test_list.layoutManager = layoutManager
        rv_test_list.adapter = requestAdapter
    }

    private fun setListener() {
        rv_test_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = rv_test_list.childCount
                val totalItemCount: Int = rv_test_list.layoutManager!!.itemCount
                val firstVisibleItem: Int =layoutManager.findFirstVisibleItemPosition()

                if(nextHit>0) {
                    if (visibleItemCount + firstVisibleItem >= totalItemCount) {
                        pageNo += 1
                        progressBar_loadMore.visibility = View.VISIBLE
                        getData()
                    }
                }
            }
        })
    }

    override fun onSelect(o: Any?, o1: Any?) {
        startActivity(Intent(activity,TestDetailsActivity::class.java))
    }

    override fun onUnSelect(o: Any?, o1: Any?) {
    }

}