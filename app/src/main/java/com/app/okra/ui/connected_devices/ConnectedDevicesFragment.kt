package com.app.okra.ui.connected_devices

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.bluetooth.BluetoothController
import com.app.okra.bluetooth.BluetoothListener
import com.app.okra.data.repo.ConnectedDevicesRepoImpl
import com.app.okra.extension.*
import com.app.okra.models.DevicesListModel
import com.app.okra.ui.my_account.support_request.SupportRequestActivity
import com.app.okra.utils.*
import kotlinx.android.synthetic.main.fragment_connected_devices.*


class ConnectedDevicesFragment : BaseFragment(),
        View.OnClickListener,
        Listeners.ItemClickListener,
        PermissionUtils.IGetPermissionListener {

    private lateinit var devicesAdapter: ConnectedDevicesAdapter
    private val devicesList = ArrayList<DevicesListModel>()
    private val mPermissionUtils = PermissionUtils(this)

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            ConnectedDevicesViewModel(ConnectedDevicesRepoImpl(apiServiceAuth))
        }).get(ConnectedDevicesViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_connected_devices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObserver()
        setListener()
    }
    private fun setAdapter() {
        devicesAdapter = ConnectedDevicesAdapter(this, devicesList)
        rv_connected_devices.adapter = devicesAdapter
    }


    private fun setObserver() {
        setBaseObservers(viewModel, this, observeToast = false)
    }


    private fun setListener() {
        btnConnect.setOnClickListener(this)
        tvNeedHelp.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnConnect -> {
                if (mPermissionUtils.checkAndGetLocationPermissions(requireActivity())) {
                    navController.navigate(R.id.action_connectedDevicesFragment_to_discoveringFragment)
                }
            }
            R.id.tvNeedHelp -> {
                requireActivity().navigate(Intent(requireContext(), SupportRequestActivity::class.java).apply {
                    putExtra(AppConstants.SCREEN_TYPE, ConnectedDeviceActivity::class.java.simpleName)
                })
            }
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
                        dialog ?. dismiss ()
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        navController.navigate(R.id.action_connectedDevicesFragment_to_discoveringFragment)

    }
}