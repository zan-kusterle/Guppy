package com.github.zan_kusterle.model;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class RemoveSongIntention extends Intention {

    private final String removedSongId;

    public RemoveSongIntention(IntentionType type, String removedSongId) {
        super(type);

        this.removedSongId = removedSongId;
    }
}
