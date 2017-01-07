package com.github.zan_kusterle.model.events;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class ClientRemovedSongEvent {

    private final String removedSongId;

    public ClientRemovedSongEvent(String removedSongId) {
        this.removedSongId = removedSongId;
    }
}
