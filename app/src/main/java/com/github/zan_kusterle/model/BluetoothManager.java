package com.github.zan_kusterle.model;

import android.bluetooth.BluetoothAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by bencz on 2016. 12. 11..
 */

public abstract class BluetoothManager {

    public static final String SERVICE_NAME = "Guppy";
    public static final UUID SERVICE_UUID = UUID.fromString("4a4b8b00-2946-49dd-8290-77ab893362ef");

    protected BluetoothAdapter adapter;
    protected EventBus eventBus;

    protected BluetoothManager(BluetoothAdapter adapter, EventBus eventBus) {
        this.adapter = adapter;
        this.eventBus = eventBus;
    }

    public boolean isBluetoothSupported() {
        return adapter != null;
    }

    public boolean isBluetoothEnabled() {
        return adapter.isEnabled();
    }

    public String getDeviceAddress() {
        return adapter.getAddress();
    }
}
