package com.github.zan_kusterle.model;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.github.zan_kusterle.model.events.ClientConnectedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by bencz on 2016. 12. 12..
 */

public class AcceptanceThread extends Thread {

    private final EventBus eventBus;

    private final BluetoothServerSocket bluetoothServerSocket;

    public AcceptanceThread(BluetoothServerSocket bluetoothServerSocket, EventBus eventBus) {
        this.bluetoothServerSocket = bluetoothServerSocket;
        this.eventBus = eventBus;
    }

    public void run() {
        try {
            while (true) {
                BluetoothSocket clientBluetoothSocket = bluetoothServerSocket.accept();
                eventBus.post(new ClientConnectedEvent(clientBluetoothSocket));
            }
        } catch (IOException e) {

        }
    }
}
