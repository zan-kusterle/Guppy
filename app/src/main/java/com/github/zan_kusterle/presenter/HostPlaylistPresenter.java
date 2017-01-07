package com.github.zan_kusterle.presenter;

import android.bluetooth.BluetoothDevice;

import com.github.zan_kusterle.model.HostBluetoothManager;
import com.github.zan_kusterle.model.HostPlaylist;
import com.github.zan_kusterle.model.events.ClientConnectedEvent;
import com.github.zan_kusterle.model.events.ClientRequestedPlaylistSyncEvent;
import com.github.zan_kusterle.model.events.CommunicationErrorEvent;
import com.github.zan_kusterle.model.events.PlaylistChangedEvent;
import com.github.zan_kusterle.view.HostPlaylistView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by bencz on 2016. 12. 18..
 */

public class HostPlaylistPresenter extends PlaylistPresenter {

    @Inject
    public HostPlaylistPresenter(HostBluetoothManager hostBluetoothManager, HostPlaylist playlist, EventBus eventBus) {
        super(hostBluetoothManager, playlist);

        eventBus.register(this);
    }

    public boolean isBluetoothSupported() {
        return bluetoothManager.isBluetoothSupported();
    }

    public void onBluetoothNotSupported() {
        ((HostPlaylistView) getView()).showBluetoothNotSupportedDialog();
    }

    public void enableBluetoothDiscoverability() {
        ((HostPlaylistView) getView()).showEnableDiscoverabilityDialog();
    }

    public void acceptConnections() {
        try {
            ((HostBluetoothManager) bluetoothManager).acceptConnections();
        } catch (IOException e) {
            getView().showToast(e.getMessage());
        }
    }

    @Override
    public void addSong(String url, String title) {
        ((HostPlaylist) playlist).addSong(bluetoothManager.getDeviceAddress(), url, title);

        try {
            ((HostBluetoothManager) bluetoothManager).sendSongsToClients(playlist.getSongs());
        } catch (IOException e) {
            getView().showToast("Unable to send modified playlist to clients: " + e.getMessage());
        }
    }

    @Override
    public void removeSong(String songId) {
        ((HostPlaylist) playlist).removeSong(songId);

        try {
            ((HostBluetoothManager) bluetoothManager).sendSongsToClients(playlist.getSongs());
        } catch (IOException e) {
            getView().showToast("Unable to send modified playlist to clients: " + e.getMessage());
        }
    }

    public void onSongClick(int songIndex) {
        String songVideoId = ((HostPlaylist) playlist).getSongVideoId(songIndex);
        ((HostPlaylist) playlist).setNextSongIndex(songIndex + 1);
        ((HostPlaylistView) getView()).loadVideo(songVideoId);
    }

    @Override
    public void onSongLongClick(int songIndex) {
        getView().showRemovalConfirmationDialog(playlist.getSongId(songIndex));
    }

    public void onVideoEnded() {
        if (((HostPlaylist) playlist).hasNextSong()) {
            ((HostPlaylistView) getView()).loadVideo(((HostPlaylist) playlist).getNextSongVideoId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientConnected(ClientConnectedEvent event) {
        BluetoothDevice clientBluetoothDevice = event.getClientBluetoothSocket().getRemoteDevice();
        String clientBluetoothDeviceAddress = clientBluetoothDevice.getAddress();
        String message = String.format("%s (%s) has connected.", clientBluetoothDevice.getName(), clientBluetoothDeviceAddress);
        getView().showToast(message);
    }

    @Subscribe
    public void onClientRequestedPlaylistSync(ClientRequestedPlaylistSyncEvent event) {
        try {
            ((HostBluetoothManager) bluetoothManager).sendSongsToClient(event.getClientBluetoothDeviceAddress(), playlist.getSongs());
        } catch (IOException e) {
            getView().showToast(e.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommunicationError(CommunicationErrorEvent event) {
        String message = String.format("%s (%s) has disconnected.", event.getBluetoothDeviceName(), event.getBluetoothDeviceAddress());
        getView().showToast(message);
    }

    @Override
    public void onPlaylistChanged(PlaylistChangedEvent event) {
        super.onPlaylistChanged(event);

        try {
            ((HostBluetoothManager) bluetoothManager).sendSongsToClients(event.getSongs());
        } catch (IOException e) {
            getView().showToast(e.getMessage());
        }
    }

    public void onLoadPlaylistClicked() {
        ((HostPlaylistView) getView()).showLoadDialog();
    }

    public void loadPlaylist(File loadFile) {
        ((HostPlaylist) playlist).load(loadFile);
    }
}
