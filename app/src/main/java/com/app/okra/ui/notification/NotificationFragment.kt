package com.app.okra.ui.notification

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.okra.OkraApplication.Companion.getApplicationContext
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.ConnectedDevicesRepoImpl
import com.app.okra.extension.*
import com.app.okra.models.DevicesListModel
import com.app.okra.ui.connected_devices.ConnectedDevicesViewModel
import com.app.okra.utils.*
import com.app.okra.utils.swipe.RecyclerTouchListener
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment : BaseFragment(),
        View.OnClickListener,
        Listeners.ItemClickListener,
        PermissionUtils.IGetPermissionListener {

    private lateinit var notificationAdapter: NotificationRecyclerAdapter
    private val devicesList = ArrayList<DevicesListModel>()
    private val mPermissionUtils = PermissionUtils(this)
    private var touchListener: RecyclerTouchListener? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            ConnectedDevicesViewModel(ConnectedDevicesRepoImpl(apiServiceAuth))
        }).get(ConnectedDevicesViewModel::class.java)
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
    }
    private fun setAdapter() {
        notificationAdapter = NotificationRecyclerAdapter(requireContext())
        rv_notification.adapter = notificationAdapter

        touchListener = RecyclerTouchListener(requireActivity(), rv_notification)
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
                        Toast.makeText(
                            requireContext(),"Item Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        rv_notification.addOnItemTouchListener(touchListener!!)

    }


    private fun setObserver() {
        setBaseObservers(viewModel, this, observeToast = false)
    }


    private fun setListener() {

    }

    override fun onClick(p0: View?) {
        when(p0?.id){

        }
    }

    override fun onSelect(o: Any?, o1: Any?) {
        val pos = o as Int
        val type = o1 as DevicesListModel

    }

    override fun onUnSelect(o: Any?, o1: Any?) {}
    override fun onPermissionsGiven(data: Int) {
        navController.navigate(R.id.action_connectedDevicesFragment_to_discoveringFragment)
    }

    override fun onPermissionsDeny(data: Int) {
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
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        navController.navigate(R.id.action_connectedDevicesFragment_to_discoveringFragment)

    }
}