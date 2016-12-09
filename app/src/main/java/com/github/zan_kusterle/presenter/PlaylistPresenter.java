package com.github.zan_kusterle.presenter;

import com.github.zan_kusterle.model.Playlist;
import com.github.zan_kusterle.model.events.PlaylistChangedEvent;
import com.github.zan_kusterle.view.PlaylistView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by bencz on 2016. 12. 08..
 */

public class PlaylistPresenter extends MvpBasePresenter<PlaylistView> {

    private final Playlist playlist;

    public PlaylistPresenter() {
        playlist = new Playlist();
        EventBus.getDefault().register(this);
    }

    public void addSong(String URL, String title) {
        playlist.addSong(URL, title);
    }

    public void removeSong(String URL) {
        playlist.removeSong(URL);
    }

    public void loadPlaylist() {

    }

    public void savePlaylist() {

    }

    @Subscribe
    public void onPlaylistChanged(PlaylistChangedEvent event) {
        getView().setPlaylist(event.getSongs());
    }
}
