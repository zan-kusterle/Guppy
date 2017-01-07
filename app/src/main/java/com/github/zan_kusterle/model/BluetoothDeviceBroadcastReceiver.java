package com.github.zan_kusterle.model;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.zan_kusterle.model.events.BluetoothDeviceDiscoveredEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;

/**
 * Created by bencz on 2016. 12. 19..
 */

public class BluetoothDeviceBroadcastReceiver extends BroadcastReceiver {

    private final EventBus eventBus;

    @Getter
    private final List<BluetoothDevice> discoveredBluetoothDevices;

    @Inject
    public BluetoothDeviceBroadcastReceiver(EventBus eventBus) {
        this.eventBus = eventBus;
        discoveredBluetoothDevices = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            discoveredBluetoothDevices.add(device);
            eventBus.post(new BluetoothDeviceDiscoveredEvent(discoveredBluetoothDevices));
        }
    }

    public BluetoothDevice getDiscoveredBluetoothDevice(int deviceIndex) {
        return discoveredBluetoothDevices.get(deviceIndex);
    }
}
