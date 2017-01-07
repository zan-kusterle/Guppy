package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class PlaylistSavedEvent {

    private final String playlistFilePath;

    public PlaylistSavedEvent(String playlistFilePath) {
        this.playlistFilePath = playlistFilePath;
    }
}
