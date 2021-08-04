package com.app.okra.ui.connected_devices

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.ConnectedDevicesRepoImpl
import com.app.okra.extension.viewModelFactory
import kotlinx.android.synthetic.main.activity_connected_device.*
import kotlinx.android.synthetic.main.layout_header.*


class ConnectedDeviceActivity : BaseActivity(), View.OnClickListener{

       private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            ConnectedDevicesViewModel(ConnectedDevicesRepoImpl(apiServiceAuth))
        }).get(ConnectedDevicesViewModel::class.java)
    }
    private lateinit var navHost: NavHostFragment
    private lateinit var navController: NavController

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connected_device)
        initialize()
        setViews()
        setListener()

        val adapter = BluetoothAdapter.getDefaultAdapter()


        val filter = IntentFilter()

        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        registerReceiver(mReceiver, filter)
        if (!adapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBluetoothIntent)
        }


        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )



        println("Discovery" + adapter.startDiscovery())
        val mydeviceaddress = adapter.address
        val mydevicename = adapter.name
        println("**** $mydevicename  $mydeviceaddress  ${adapter.state}")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            println("Action$action")
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                println("Started")
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                println("changed")
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                println("finished")
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                //bluetooth device found
                val device =
                    intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice?

                 System.out.println("**** Found device " + device?.name);
            }
        }
    }

    private fun initialize() {
        navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHost.navController
    }

    fun setTitle(title: String){
        tvTitle.text = title
    }

    private fun setListener() {
        ivBack.setOnClickListener(this)
    }


    private fun setViews() {
        setTitle(getString(R.string.title_connect_device))
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                if (navHost.childFragmentManager.backStackEntryCount == 0) {
                    finish()
                } else {
                    navController.popBackStack()
                }
            }
        }
    }

    override fun onBackPressed() {
        onClick(findViewById(R.id.ivBack))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragment: Fragment =  navHost?.childFragmentManager?.fragments?.get(0)!!
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}