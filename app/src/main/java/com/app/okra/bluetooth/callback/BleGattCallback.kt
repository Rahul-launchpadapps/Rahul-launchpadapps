package com.app.okra.bluetooth.callback

import android.annotation.TargetApi
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.os.Build
import com.app.okra.bluetooth.data.BleDevice
import com.app.okra.bluetooth.exception.BleException

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
abstract class BleGattCallback : BluetoothGattCallback() {
    abstract fun onStartConnect()
    abstract fun onConnectFail(bleDevice: BleDevice?, exception: BleException?)
    abstract fun onConnectSuccess(bleDevice: BleDevice?, gatt: BluetoothGatt?, status: Int)
    abstract fun onDisConnected(
        isActiveDisConnected: Boolean,
        device: BleDevice?,
        gatt: BluetoothGatt?,
        status: Int
    )
}