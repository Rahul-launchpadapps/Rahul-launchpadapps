package com.app.okra.ui.connected_devices

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.bluetooth.BluetoothController
import com.app.okra.bluetooth.BluetoothListener
import com.app.okra.bluetooth.BroadcastReceiverDelegator
import com.app.okra.data.repo.ConnectedDevicesRepoImpl
import com.app.okra.extension.beGone
import com.app.okra.extension.beVisible
import com.app.okra.extension.navigate
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.DevicesListModel
import com.app.okra.ui.my_account.support_request.SupportRequestActivity
import com.app.okra.utils.*
import kotlinx.android.synthetic.main.fragment_bluetooth_devices_list.*


class BluetoothDevicesListFragment : BaseFragment(),
        Listeners.ItemClickListener,
        BluetoothListener,
        PermissionUtils.IGetPermissionListener
{

    private lateinit var broadCastDelegator: BroadcastReceiverDelegator
    private lateinit var devicesAdapter: ConnectedDevicesAdapter
    private val devicesList = ArrayList<DevicesListModel>()
    private val mPermissionUtils by lazy {
        PermissionUtils(this)
    }

    private val bluetooth by lazy {
        // Sets up the bluetooth controller.
        BluetoothController(requireActivity(),
             BluetoothAdapter.getDefaultAdapter(),
                this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            ConnectedDevicesViewModel(ConnectedDevicesRepoImpl(apiServiceAuth))
        }).get(ConnectedDevicesViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance() = BluetoothDevicesListFragment()
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_devices_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setViews()
        setObserver()
        setListener()
       // broadCastDelegator = BroadcastReceiverDelegator(requireActivity(), this)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 123)
            }else{
                checkForBluetooth()

            }
        }else{
            checkForBluetooth()

        }*/



    }

    private fun setAdapter() {
        devicesAdapter = ConnectedDevicesAdapter(this, devicesList)
        rv_devices.adapter = devicesAdapter
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, observeToast = false)
    }

    private fun setListener() {

    }

    private fun setViews() {
        llDiscovering.beVisible()
        llList.beGone()
    }




    private fun checkForBluetooth() {
        if (!bluetooth.isBluetoothEnabled) {
            showCustomAlertDialog(
                    requireContext(),
                    object : Listeners.DialogListener {
                        override fun onOkClick(dialog: DialogInterface?) {
                            bluetooth.turnOnBluetooth()
                            dialog ?. dismiss ()
                        }

                        override fun onCancelClick(dialog: DialogInterface?) {
                            navController.popBackStack()
                            dialog?.dismiss()
                        }
                    },
                    MessageConstants.Messages.bluetooth_turn_on_permission,
                    true,
                    positiveButtonText = getString(R.string.allow),
                    negativeButtonText = getString(R.string.cancel),
                    title = getString(R.string.bluetooth),
            )
        }
        else{
            discoverDevices()
        }
    }
    private fun discoverDevices() {
        if(!bluetooth.isDiscovering) {
            bluetooth.startDiscovery()
        } else {
            bluetooth.cancelDiscovery()
        }
    }

    override fun onSelect(o: Any?, o1: Any?) {
        val pos = o as Int
        val type = o1 as DevicesListModel

    }

    override fun onUnSelect(o: Any?, o1: Any?) {}

    override fun onDeviceDiscovered(device: BluetoothDevice?) {
        device?.let {
            var isDuplicate = false

            if (devicesList.isNotEmpty()) {
                for (data in devicesList) {
                    if(it.address == data.address){
                        isDuplicate = true
                    }
                }
            }
            if(!isDuplicate){
                addDeviceInList(it)
            }

            if (devicesList.size > 0) {
                llDiscovering.beGone()
                llList.beVisible()
                devicesAdapter.notifyDataSetChanged()
            } else {
                llDiscovering.beVisible()
                llList.beGone()
            }
        }
    }

    private fun addDeviceInList(device: BluetoothDevice) {
        if (!device.name.isNullOrEmpty() && device.name.contains("OKRA")) {
            devicesList.add(DevicesListModel(name = device.name, address = device.address))
        }

    }

    override fun onDeviceDiscoveryStarted() {
        llDiscovering.beVisible()
        llList.beGone()
    }

    override fun setBluetoothController(bluetooth: BluetoothController?) {}

    override fun onDeviceDiscoveryEnd() {
       /* if(devicesList.isEmpty()){
            showToast(MessageConstants.Messages.no_device_available)
            if(this::broadCastDelegator.isInitialized)
                broadCastDelegator.close()
        }

        navController.popBackStack()*/
        Log.d("TAG", "**** Discovery ended -Main Activity.")

    }

    override fun onBluetoothStatusChanged() {
        if(!bluetooth.isBluetoothEnabled){
            showToast(MessageConstants.Messages.please_enable_your_bluetooth, requireContext())
            broadCastDelegator.close()
        }
        navController.popBackStack()
        Log.d("TAG", "**** onBluetoothStatusChanged -Main Activity.")

    }

    override fun onBluetoothTurningOn() {
        discoverDevices()
    }

    override fun onDevicePairingEnded() {}

    override fun onPermissionsGiven(data: Int) {
        checkForBluetooth()
    }

    override fun onPermissionsDeny(data: Int) {
        showToast( MessageConstants.Messages.location_permission_deny_text)
        navController.popBackStack()
        Log.d("TAG", "**** onPermissionsDeny -Main Activity.")

    }

}