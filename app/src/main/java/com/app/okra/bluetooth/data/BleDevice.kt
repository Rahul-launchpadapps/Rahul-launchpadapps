package com.app.okra.bluetooth.data

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import android.os.Parcelable

class BleDevice : Parcelable {
    var device: BluetoothDevice?
    var scanRecord: ByteArray?=null
    var rssi = 0
    var timestampNanos: Long = 0
    val isPaired: Boolean = false

    constructor(device: BluetoothDevice?) {
        this.device = device
    }

    constructor(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?, timestampNanos: Long) {
        this.device = device
        this.scanRecord = scanRecord
        this.rssi = rssi
        this.timestampNanos = timestampNanos
    }

    protected constructor(`in`: Parcel) {
        device = `in`.readParcelable(BluetoothDevice::class.java.classLoader)
        scanRecord = `in`.createByteArray()
        rssi = `in`.readInt()
        timestampNanos = `in`.readLong()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(device, flags)
        dest.writeByteArray(scanRecord)
        dest.writeInt(rssi)
        dest.writeLong(timestampNanos)
    }

    override fun describeContents(): Int {
        return 0
    }

    val name: String?
        get() = if (device != null) device!!.name else null
    val mac: String?
        get() = if (device != null) device!!.address else null
    val deviceKey: String
        get() = if (device != null) device!!.name + device!!.address else ""


    companion object  CREATOR: Parcelable.Creator<BleDevice?> {
            override fun createFromParcel(`in`: Parcel): BleDevice? {
                return BleDevice(`in`)
            }

            override fun newArray(size: Int): Array<BleDevice?> {
                return arrayOfNulls(size)
            }

    }
}