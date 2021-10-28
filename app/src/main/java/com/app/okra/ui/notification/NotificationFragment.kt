package com.app.okra.ui.notification

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.NotificationRepoImpl
import com.app.okra.extension.*
import com.app.okra.models.Notification
import com.app.okra.utils.*
import com.app.okra.utils.swipe.RecyclerTouchListener
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_notification.progressBar_loadMore
import kotlinx.android.synthetic.main.fragment_notification.swipe_request

class NotificationFragment : BaseFragment(),
        Listeners.ItemClickListener{

   // private lateinit var layoutManager: LinearLayoutManager
    private lateinit var notificationAdapterToday: NotificationRecyclerAdapter
    private lateinit var notificationAdapterEarlier: NotificationRecyclerAdapter
    private var touchListener: RecyclerTouchListener? = null
    private val notificationToday by lazy {  ArrayList<Notification>() }
    private val notificationEarlier by lazy {  ArrayList<Notification>() }

    private var pageNo :Int = 1
    private var totalPage: Int = 0
    private var nextHit: Int = 0

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            NotificationViewModel(NotificationRepoImpl(apiServiceAuth))
        }).get(NotificationViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObserver()
        setListener()
        viewModel.getNotification(pageNo)
    }

    private fun setAdapter() {
        notificationAdapterToday = NotificationRecyclerAdapter(requireContext(),notificationToday)
        notificationAdapterEarlier = NotificationRecyclerAdapter(requireContext(),notificationEarlier)

        rv_today.adapter = notificationAdapterToday
        rv_earlier.adapter = notificationAdapterEarlier

        touchListener = RecyclerTouchListener(requireActivity(), rv_today)
        touchListener!!.setClickable(object : RecyclerTouchListener.OnRowClickListener {
                override fun onRowClicked(position: Int) {
                    Toast.makeText(
                        requireContext(),"Item Clicked: $position",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
            })
            .setSwipeOptionViews(R.id.clRowBG)
            .setSwipeable(R.id.clRowFG, R.id.clRowBG
            ) { viewID, position ->
                when (viewID) {
                    R.id.clRowBG -> {
                        showCustomAlertDialog(
                            requireContext(),
                            object : Listeners.DialogListener {
                                override fun onOkClick(dialog: DialogInterface?) {
                                    dialog?.dismiss()
                                }

                                override fun onCancelClick(dialog: DialogInterface?) {
                                    dialog?.dismiss()
                                }
                            },
                            MessageConstants.Messages.location_permission_deny_text,
                            false,
                            positiveButtonText = getString(R.string.ok),
                            title = getString(R.string.alert),
                        )

                        Toast.makeText(
                            requireContext(),"Item Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        rv_today.addOnItemTouchListener(touchListener!!)
    }


    private fun setObserver() {
        setBaseObservers(viewModel, this, observeToast = false)

        viewModel._notificationLiveData.observe(viewLifecycleOwner) { it ->
            swipe_request.isRefreshing = false
            if (it.totalPage != null) {
                totalPage = it.totalPage
            }
            if (it.nextHit != null) {
                nextHit = it.nextHit
            }
            it.data?.let{
                if (pageNo == 1) {
                    notificationToday.clear()
                    notificationEarlier.clear()
                }

                if(it.today?.size!! > 0){
                    notificationToday.addAll(it.today!!)
                }else{
                    tvToday.visibility = View.GONE
                    rv_today.visibility = View.GONE
                }

                if(it.earlier?.size!! > 0){
                    notificationEarlier.addAll(it.earlier!!)
                }else{
                    tvEarlier.visibility = View.GONE
                    rv_earlier.visibility = View.GONE
                }
                notificationAdapterToday.notifyDataSetChanged()
                notificationAdapterEarlier.notifyDataSetChanged()
            }
        }
    }


    private fun setListener() {
        /*rv_today.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = rv_today.childCount
                val totalItemCount: Int = rv_today.layoutManager!!.itemCount
                val firstVisibleItem: Int =layoutManager.findFirstVisibleItemPosition()

                if(nextHit>0) {
                    if (visibleItemCount + firstVisibleItem >= totalItemCount) {
                        pageNo += 1
                        progressBar_loadMore.visibility = View.VISIBLE
                        viewModel.getNotification(pageNo)
                    }
                }
            }
        })*/

        swipe_request.setOnRefreshListener {
            pageNo=1
            viewModel.getNotification(1)
        }
    }

    override fun onSelect(o: Any?, o1: Any?) {
    }

    override fun onUnSelect(o: Any?, o1: Any?) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        navController.navigate(R.id.action_connectedDevicesFragment_to_discoveringFragment)

    }
}