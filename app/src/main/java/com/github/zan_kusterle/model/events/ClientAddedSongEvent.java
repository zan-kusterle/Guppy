package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 05..
 */
@Getter
public class ClientAddedSongEvent {

    private final String originBluetoothDeviceAddress;
    private final String url;
    private final String title;

    public ClientAddedSongEvent(String originBluetoothDeviceAddress, String url, String title) {
        this.originBluetoothDeviceAddress = originBluetoothDeviceAddress;
        this.url = url;
        this.title = title;
    }
}
