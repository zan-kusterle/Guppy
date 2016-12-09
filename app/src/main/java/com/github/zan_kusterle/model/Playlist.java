package com.github.zan_kusterle.model;

import com.github.zan_kusterle.model.events.PlaylistChangedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bencz on 2016. 12. 08..
 */

public class Playlist {

    private final List<Song> songs;

    public Playlist() {
        songs = new ArrayList<>();
    }

    public void addSong(String URL, String title) {
        Song songToBeAdded = new Song(URL, title);
        songs.add(songToBeAdded);
        onPlaylistChanged();
    }

    public void removeSong(String URL) {
        Song songInstance = null;

        for (Song songToBeRemoved : songs) {
            if (songToBeRemoved.getURL().equals(URL)) {
                songInstance = songToBeRemoved;
            }
        }

        if (songInstance != null) {
            songs.remove(songInstance);
            onPlaylistChanged();
        }
    }

    private void onPlaylistChanged() {
        EventBus.getDefault().post(new PlaylistChangedEvent(songs));
    }
}
