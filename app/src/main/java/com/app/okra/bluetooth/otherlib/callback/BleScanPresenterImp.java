package com.app.okra.bluetooth.otherlib.callback;

import com.app.okra.bluetooth.otherlib.data.BleDevice;

public interface BleScanPresenterImp {

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
