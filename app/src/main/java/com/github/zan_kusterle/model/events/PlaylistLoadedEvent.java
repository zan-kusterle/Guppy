package com.github.zan_kusterle.model.events;

import com.github.zan_kusterle.model.Song;

import java.util.List;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 07..
 */
@Getter
public class PlaylistLoadedEvent {

    private final List<Song> songs;

    public PlaylistLoadedEvent(List<Song> songs) {
        this.songs = songs;
    }
}
