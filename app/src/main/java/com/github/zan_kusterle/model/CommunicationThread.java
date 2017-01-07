package com.github.zan_kusterle.model;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.github.zan_kusterle.model.events.CommunicationErrorEvent;
import com.github.zan_kusterle.model.events.DataReceivedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by bencz on 2016. 12. 29..
 */

public class CommunicationThread extends Thread {

    private final String bluetoothDeviceName;
    private final String bluetoothDeviceAddress;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final EventBus eventBus;

    public CommunicationThread(BluetoothSocket socket, EventBus eventBus) throws IOException {
        BluetoothDevice remoteDevice = socket.getRemoteDevice();

        this.bluetoothDeviceName = remoteDevice.getName();
        this.bluetoothDeviceAddress = remoteDevice.getAddress();
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        int bytesToRead = -1;

        while (true) {
            try {
                if (bytesToRead == -1) {
                    bytesToRead = inputStream.readInt();
                } else {
                    byte[] buffer = new byte[bytesToRead];
                    inputStream.readFully(buffer);

                    eventBus.post(new DataReceivedEvent(bluetoothDeviceAddress, buffer));

                    bytesToRead = -1;
                }
            } catch (IOException e) {
                eventBus.post(new CommunicationErrorEvent(bluetoothDeviceName, bluetoothDeviceAddress));
                break;
            }
        }
    }

    public void writeDataLength(int dataLength) throws IOException {
        outputStream.writeInt(dataLength);
    }

    public void writeData(byte[] data) throws IOException {
        outputStream.write(data);
    }
}
