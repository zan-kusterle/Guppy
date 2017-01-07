package com.github.zan_kusterle.model.events;

import android.bluetooth.BluetoothSocket;

import lombok.Getter;

/**
 * Created by bencz on 2016. 12. 28..
 */

@Getter
public class ClientConnectedEvent {

    private final BluetoothSocket clientBluetoothSocket;

    public ClientConnectedEvent(BluetoothSocket clientBluetoothSocket) {
        this.clientBluetoothSocket = clientBluetoothSocket;
    }
}
