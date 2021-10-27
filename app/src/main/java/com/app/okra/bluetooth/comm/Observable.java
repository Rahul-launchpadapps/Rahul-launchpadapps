package com.app.okra.bluetooth.comm;


import com.app.okra.bluetooth.otherlib.data.BleDevice;

public interface Observable {

    void addObserver(Observer obj);

    void deleteObserver(Observer obj);

    void notifyObserver(BleDevice bleDevice);
}
