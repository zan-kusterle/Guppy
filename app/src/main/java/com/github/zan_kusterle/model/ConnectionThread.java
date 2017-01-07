package com.github.zan_kusterle.model;

import android.bluetooth.BluetoothSocket;

import com.github.zan_kusterle.model.events.ConnectionCompleteEvent;
import com.github.zan_kusterle.model.events.ConnectionFailedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import lombok.Setter;

/**
 * Created by bencz on 2016. 12. 12..
 */

public class ConnectionThread extends Thread {

    private final EventBus eventBus;

    @Setter
    private BluetoothSocket hostBluetoothSocket;

    public ConnectionThread(BluetoothSocket hostBluetoothSocket, EventBus eventBus) {
        this.hostBluetoothSocket = hostBluetoothSocket;
        this.eventBus = eventBus;
    }

    public void run() {
        try {
            hostBluetoothSocket.connect();
            eventBus.post(new ConnectionCompleteEvent());
        } catch (IOException e1) {
            try {
                eventBus.post(new ConnectionFailedEvent(e1.getMessage()));
                hostBluetoothSocket.close();
            } catch (IOException e2) {}
        }
    }
}
