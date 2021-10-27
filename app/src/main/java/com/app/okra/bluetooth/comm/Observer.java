package com.app.okra.bluetooth.comm;


import com.app.okra.bluetooth.otherlib.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
