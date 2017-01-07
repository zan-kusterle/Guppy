package com.github.zan_kusterle.model;

import java.util.List;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class SetSongsIntention extends Intention {

    private final List<Song> songs;

    public SetSongsIntention(IntentionType type, List<Song> songs) {
        super(type);

        this.songs = songs;
    }
}
