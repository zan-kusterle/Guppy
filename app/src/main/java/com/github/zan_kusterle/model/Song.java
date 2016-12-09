package com.github.zan_kusterle.model;

/**
 * Created by bencz on 2016. 12. 08..
 */

public class Song {

    private final String URL;
    private final String title;

    public Song(String URL, String title) {
        this.URL = URL;
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public String getTitle() {
        return title;
    }
}
