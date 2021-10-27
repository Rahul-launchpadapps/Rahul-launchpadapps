package com.app.okra.bluetooth.otherlib.callback;


import com.app.okra.bluetooth.otherlib.exception.BleException;

public abstract class BleWriteCallback extends BleBaseCallback{

    public abstract void onWriteSuccess(int current, int total, byte[] justWrite);

    public abstract void onWriteFailure(BleException exception);

}
