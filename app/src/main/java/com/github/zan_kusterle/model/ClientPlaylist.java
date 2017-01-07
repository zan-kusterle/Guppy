package com.github.zan_kusterle.model;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by bencz on 2017. 01. 06..
 */

public class ClientPlaylist extends Playlist {

    @Inject
    public ClientPlaylist(EventBus eventBus) {
        super(eventBus);
    }

    public boolean canSongBeRemovedByUser(int songIndex, String bluetoothDeviceAddress) {
        if (bluetoothDeviceAddress.equals(songs.get(songIndex).getOriginBluetoothDeviceAddress())) {
            return true;
        }

        return false;
    }
}
