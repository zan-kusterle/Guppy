package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 07..
 */
@Getter
public class ClientRequestedPlaylistSyncEvent {

    private final String clientBluetoothDeviceAddress;

    public ClientRequestedPlaylistSyncEvent(String clientBluetoothDeviceAddress) {
        this.clientBluetoothDeviceAddress = clientBluetoothDeviceAddress;
    }
}
