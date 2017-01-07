package com.github.zan_kusterle.presenter;

import com.github.zan_kusterle.model.ClientBluetoothManager;
import com.github.zan_kusterle.model.ClientPlaylist;
import com.github.zan_kusterle.model.events.CommunicationErrorEvent;
import com.github.zan_kusterle.model.events.PlaylistChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by bencz on 2016. 12. 18..
 */

public class ClientPlaylistPresenter extends PlaylistPresenter {

    @Inject
    public ClientPlaylistPresenter(ClientBluetoothManager clientBluetoothManager, ClientPlaylist playlist, EventBus eventBus) {
        super(clientBluetoothManager, playlist);

        eventBus.register(this);
    }

    public void syncSongsWithHost() {
        try {
            ((ClientBluetoothManager) bluetoothManager).syncSongsWithHost();
        } catch (IOException e) {
            getView().showToast("Unable to send playlist sync request to host: " + e.getMessage());
        }
    }

    @Override
    public void addSong(String url, String title) {
        try {
            ((ClientBluetoothManager) bluetoothManager).sendNewSongChoiceToHost(url, title);
        } catch (IOException e) {
            getView().showToast("Unable to send song choice to host: " + e.getMessage());
        }
    }

    @Override
    public void removeSong(String songId) {
        try {
            ((ClientBluetoothManager) bluetoothManager).sendRemovableSongIdToHost(songId);
        } catch (IOException e) {
            getView().showToast("Unable to send remove request to host: " + e.getMessage());
        }
    }

    @Override
    public void onSongLongClick(int songIndex) {
        if (((ClientPlaylist)playlist).canSongBeRemovedByUser(songIndex, bluetoothManager.getDeviceAddress())) {
            getView().showRemovalConfirmationDialog(playlist.getSongId(songIndex));
        }
    }

    @Override
    public void onPlaylistChanged(PlaylistChangedEvent event) {
        super.onPlaylistChanged(event);

        playlist.setSongs(event.getSongs());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommunicationError(CommunicationErrorEvent event) {
        String message = String.format("Disconnected from %s (%s).", event.getBluetoothDeviceName(), event.getBluetoothDeviceAddress());
        getView().showToast(message);
    }
}
