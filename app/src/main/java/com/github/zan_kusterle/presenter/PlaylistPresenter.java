package com.github.zan_kusterle.presenter;

import com.github.zan_kusterle.model.BluetoothManager;
import com.github.zan_kusterle.model.Playlist;
import com.github.zan_kusterle.model.Song;
import com.github.zan_kusterle.model.events.FileOperationErrorEvent;
import com.github.zan_kusterle.model.events.PlaylistChangedEvent;
import com.github.zan_kusterle.model.events.PlaylistSavedEvent;
import com.github.zan_kusterle.view.PlaylistView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

/**
 * Created by bencz on 2016. 12. 08..
 */

public abstract class PlaylistPresenter extends MvpBasePresenter<PlaylistView> {

    protected final BluetoothManager bluetoothManager;
    protected final Playlist playlist;

    public PlaylistPresenter(BluetoothManager bluetoothManager, Playlist playlist) {
        this.bluetoothManager = bluetoothManager;
        this.playlist = playlist;
    }

    public void onConfigurationChanged() {
        List<Song> songs = playlist.getSongs();
        if (songs.size() > 0) {
            getView().setPlaylist(playlist.getSongs());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlaylistChanged(PlaylistChangedEvent event) {
        getView().setPlaylist(event.getSongs());
    }

    public void onSavePlaylistClicked() {
        getView().showSaveDialog();
    }

    public void savePlaylist(File saveFile) {
        playlist.save(saveFile);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlaylistSaved(PlaylistSavedEvent event) {
        getView().showToast("Playlist saved to: " + event.getPlaylistFilePath());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFileOperationError(FileOperationErrorEvent event) {
        getView().showToast(event.getErrorMessage());
    }

    public abstract void addSong(String url, String title);

    public abstract void removeSong(String songId);

    public abstract void onSongLongClick(int songIndex);
}
