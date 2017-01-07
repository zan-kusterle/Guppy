package com.github.zan_kusterle.model;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 06..
 */
@Getter
public class AddSongIntention extends Intention {

    private final ClientNewSongChoice clientNewSongChoice;

    public AddSongIntention(IntentionType type, ClientNewSongChoice clientNewSongChoice) {
        super(type);

        this.clientNewSongChoice = clientNewSongChoice;
    }
}
