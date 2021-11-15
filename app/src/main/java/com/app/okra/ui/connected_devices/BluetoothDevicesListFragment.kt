package com.app.okra.ui.connected_devices

import android.app.Application
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.okra.OkraApplication
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.bluetooth.otherlib.BleManager
import com.app.okra.bluetooth.otherlib.callback.BleScanCallback
import com.app.okra.bluetooth.otherlib.data.BleDevice
import com.app.okra.data.repo.ConnectedDevicesRepoImpl
import com.app.okra.extension.beGone
import com.app.okra.extension.beVisible
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.DevicesListModel
import com.app.okra.utils.Listeners
import com.app.okra.utils.MessageConstants
import com.app.okra.utils.PermissionUtils
import com.app.okra.utils.bleValidater.BLEValidaterListener
import com.app.okra.utils.bleValidater.BleValidate
import com.app.okra.utils.showCustomAlertDialog
import kotlinx.android.synthetic.main.fragment_bluetooth_devices_list.*


class BluetoothDevicesListFragment : BaseFragment(),
        Listeners.ItemClickListener,
    PermissionUtils.IGetPermissionListener, BLEValidaterListener {

    private lateinit var devicesAdapter: ConnectedDevicesAdapter
    private val devicesList = ArrayList<DevicesListModel>()
    private val mPermissionUtils by lazy {
        PermissionUtils(this)
    }

    private val bleManager by lazy {
        // Sets up the bluetooth controller.
       BleManager.getInstance()
    }

    private val bleValidate by lazy {
        BleValidate(requireActivity(), this)
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_devices_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setViews()
        setObserver()
        setListener()
        BleManager.getInstance().init(OkraApplication.getApplicationInstance())
        BleManager.getInstance()
            .enableLog(true)
            .setReConnectCount(1, 5000)
            .setConnectOverTime(20000).operateTimeout = 5000

        bleValidate.checkPermissions()
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


    private fun addDeviceInList(device: BleDevice) {
        if (!device.name.isNullOrEmpty()) {
            devicesList.add(DevicesListModel(name = device.name, address = device.mac))
        }

    }



    override fun onBluetoothDisable(msg: String) {
        showCustomAlertDialog(
            requireContext(),
            object : Listeners.DialogListener {
                override fun onOkClick(dialog: DialogInterface?) {
                    navController.popBackStack()
                    dialog?.dismiss()
                }

                override fun onCancelClick(dialog: DialogInterface?) {
                    dialog?.dismiss()
                }
            },
            MessageConstants.Messages.bluetooth_turn_on_permission,
            false,
            positiveButtonText = getString(R.string.ok),
            negativeButtonText = getString(R.string.cancel),
            title = getString(R.string.bluetooth),
        )
    }

    override fun onLocationDisable(msg: String) {
        showCustomAlertDialog(
            requireContext(),
            object : Listeners.DialogListener {
                override fun onOkClick(dialog: DialogInterface?) {
                    navController.popBackStack()
                    dialog?.dismiss()
                }

                override fun onCancelClick(dialog: DialogInterface?) {
                    dialog?.dismiss()
                }
            },
            MessageConstants.Messages.please_turn_on_your_location,
            false,
            positiveButtonText = getString(R.string.ok),
            negativeButtonText = getString(R.string.cancel),
            title = getString(R.string.location),
        )
    }

    override fun onPermissionsGiven(data: Int) {
        startScan()
    }

    override fun onPermissionsDeny(data: Int) {
        showCustomAlertDialog(
            requireContext(),
            object : Listeners.DialogListener {
                override fun onOkClick(dialog: DialogInterface?) {
                    navController.popBackStack()
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
        Log.d("TAG", "**** onPermissionsDeny -Main Activity.")

    }

    private fun startScan() {
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                devicesList.clear()
                devicesAdapter.notifyDataSetChanged()
                manageViewVisibility(true)
            }

            override fun onLeScan(bleDevice: BleDevice?) {
                super.onLeScan(bleDevice)
            }

            override fun onScanning(bleDevice: BleDevice?) {
                println(":::: Device Found")
                addDeviceInList(bleDevice!!)
                devicesAdapter.notifyDataSetChanged()
                manageViewVisibility(false)
            }

            override fun onScanFinished(scanResultList: List<BleDevice?>?) {
                showToast("Scan finished")
                /* img_loading.clearAnimation()
                img_loading.setVisibility(View.INVISIBLE)
                btn_scan.setText(getString(R.string.start_scan))*/
            }
        })
    }

    private fun manageViewVisibility(isDiscovering: Boolean) {
        if(isDiscovering){
            llDiscovering.beVisible()
            llList.beGone()
        }else{
            llDiscovering.beGone()
            llList.beVisible()

            if(devicesList.isNotEmpty()){
                rv_devices.beVisible()
                tvNoDevice.beGone()
            }else{
                rv_devices.beGone()
                tvNoDevice.beVisible()
            }
        }
    }

    override fun onSelect(o: Any?, o1: Any?) {

    }

    override fun onUnSelect(o: Any?, o1: Any?) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startScan()
    }


}