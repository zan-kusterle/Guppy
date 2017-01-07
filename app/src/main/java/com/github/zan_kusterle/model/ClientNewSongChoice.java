package com.github.zan_kusterle.model;

import java.io.Serializable;

import lombok.Getter;

/**
 * Created by bencz on 2017. 01. 05..
 */
@Getter
public class ClientNewSongChoice implements Serializable {

    private final String url;
    private final String title;

    public ClientNewSongChoice(String url, String title) {
        this.url = url;
        this.title = title;
    }
}
