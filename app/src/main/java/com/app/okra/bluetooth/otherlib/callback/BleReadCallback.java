package com.app.okra.bluetooth.otherlib.callback;


import com.app.okra.bluetooth.otherlib.exception.BleException;

public abstract class BleReadCallback extends BleBaseCallback {

    public abstract void onReadSuccess(byte[] data);

    public abstract void onReadFailure(BleException exception);

}
