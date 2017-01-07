package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class CommunicationErrorEvent {

    private final String bluetoothDeviceName;
    private final String bluetoothDeviceAddress;

    public CommunicationErrorEvent(String bluetoothDeviceName, String bluetoothDeviceAddress) {
        this.bluetoothDeviceName = bluetoothDeviceName;
        this.bluetoothDeviceAddress = bluetoothDeviceAddress;
    }
}
