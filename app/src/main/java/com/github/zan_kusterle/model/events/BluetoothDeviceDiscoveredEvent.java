package com.github.zan_kusterle.model.events;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import lombok.Getter;

/**
 * Created by bencz on 2016. 12. 19..
 */

@Getter
public class BluetoothDeviceDiscoveredEvent {

    private final List<BluetoothDevice> discoveredBluetoothDevices;

    public BluetoothDeviceDiscoveredEvent(List<BluetoothDevice> discoveredBluetoothDevices) {
        this.discoveredBluetoothDevices = discoveredBluetoothDevices;
    }
}
