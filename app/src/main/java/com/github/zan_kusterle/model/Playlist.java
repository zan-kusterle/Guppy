package com.github.zan_kusterle.model;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by bencz on 2016. 12. 08..
 */

public abstract class Playlist {

    protected final EventBus eventBus;

    @Getter
    @Setter
    protected List<Song> songs;

    public Playlist(EventBus eventBus) {
        this.eventBus = eventBus;

        songs = new ArrayList<>();
    }

    public String getSongId(int songIndex) {
        return songs.get(songIndex).getId();
    }

    public void save(File saveFile) {
        new SaveFileThread(saveFile, songs, eventBus).start();
    }
}
