package com.github.zan_kusterle.model;

import com.github.zan_kusterle.model.events.FileOperationErrorEvent;
import com.github.zan_kusterle.model.events.PlaylistLoadedEvent;

import org.apache.commons.lang3.SerializationUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by bencz on 2017. 01. 06..
 */

public class LoadFileThread extends Thread {

    private final File loadFile;
    private final EventBus eventBus;

    public LoadFileThread(File loadFile, EventBus eventBus) {
        this.loadFile = loadFile;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try {
            FileInputStream fileInputStream = new FileInputStream(loadFile);
            List<Song> songs = SerializationUtils.deserialize(fileInputStream);
            fileInputStream.close();

            eventBus.post(new PlaylistLoadedEvent(songs));
        } catch (IOException e) {
            eventBus.post(new FileOperationErrorEvent(e.getMessage()));
        }
    }
}
