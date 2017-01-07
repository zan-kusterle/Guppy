package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 05..
 */

@Getter
public class DataReceivedEvent {

    private final String senderBluetoothDeviceAddress;
    private final byte[] data;

    public DataReceivedEvent(String senderBluetoothDeviceAddress, byte[] data) {
        this.senderBluetoothDeviceAddress = senderBluetoothDeviceAddress;
        this.data = data;
    }
}
