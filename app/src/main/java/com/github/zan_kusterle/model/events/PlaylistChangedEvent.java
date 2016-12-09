package com.github.zan_kusterle.model.events;

import com.github.zan_kusterle.model.Song;

import java.util.List;

/**
 * Created by bencz on 2016. 12. 08..
 */

public class PlaylistChangedEvent {
    private final List<Song> songs;

    public PlaylistChangedEvent(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }
}
