package com.github.zan_kusterle.model;

import com.github.zan_kusterle.model.events.FileOperationErrorEvent;
import com.github.zan_kusterle.model.events.PlaylistSavedEvent;

import org.apache.commons.lang3.SerializationUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by bencz on 2017. 01. 06..
 */

public class SaveFileThread extends Thread {

    private final File saveFile;
    private final List<Song> songs;
    private final EventBus eventBus;

    public SaveFileThread(File saveFile, List<Song> songs, EventBus eventBus) {
        this.saveFile = saveFile;
        this.songs = songs;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            SerializationUtils.serialize((Serializable) songs, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            eventBus.post(new PlaylistSavedEvent(saveFile.getPath()));
        } catch (IOException e) {
            eventBus.post(new FileOperationErrorEvent(e.getMessage()));
        }
    }
}
