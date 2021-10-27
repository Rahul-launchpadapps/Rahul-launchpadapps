package com.app.okra.bluetooth.otherlib.callback;


import com.app.okra.bluetooth.otherlib.exception.BleException;

public abstract class BleMtuChangedCallback extends BleBaseCallback {

    public abstract void onSetMTUFailure(BleException exception);

    public abstract void onMtuChanged(int mtu);

}
