package com.app.okra.bluetooth.otherlib.callback;


import com.app.okra.bluetooth.otherlib.exception.BleException;

public abstract class BleRssiCallback extends BleBaseCallback{

    public abstract void onRssiFailure(BleException exception);

    public abstract void onRssiSuccess(int rssi);

}