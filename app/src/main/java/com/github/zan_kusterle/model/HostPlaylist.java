package com.github.zan_kusterle.model;

import com.github.zan_kusterle.model.events.ClientAddedSongEvent;
import com.github.zan_kusterle.model.events.ClientRemovedSongEvent;
import com.github.zan_kusterle.model.events.PlaylistChangedEvent;
import com.github.zan_kusterle.model.events.PlaylistLoadedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import lombok.Setter;

/**
 * Created by bencz on 2017. 01. 06..
 */

public class HostPlaylist extends Playlist {

    private static final Pattern VIDEO_ID_PATTERN = Pattern.compile("watch\\?v=(.{11})", Pattern.CASE_INSENSITIVE);

    private final EventBus eventBus;

    @Setter
    private int nextSongIndex;

    @Inject
    public HostPlaylist(EventBus eventBus) {
        super(eventBus);

        this.eventBus = eventBus;

        eventBus.register(this);
    }

    public void addSong(String originBluetoothDeviceAddress, String url, String title) {
        String videoId = null;

        Matcher videoIdPatternMatcher = VIDEO_ID_PATTERN.matcher(url);
        if (videoIdPatternMatcher.find()) {
            videoId = videoIdPatternMatcher.group(1);
        }

        Song songToBeAdded = Song.builder()
                .id(UUID.randomUUID().toString())
                .originBluetoothDeviceAddress(originBluetoothDeviceAddress)
                .url(url)
                .title(title)
                .videoId(videoId)
                .build();

        songs.add(songToBeAdded);
        eventBus.post(new PlaylistChangedEvent(songs));
    }

    public void removeSong(String songId) {
        int songIndex = findSongIndexById(songId);
        if (songIndex == -1)
            return;

        if (songIndex == nextSongIndex) {
            ++nextSongIndex;
        }

        songs.remove(songIndex);
        eventBus.post(new PlaylistChangedEvent(songs));
    }

    public String getSongVideoId(int songIndex) {
        return songs.get(songIndex).getVideoId();
    }

    public boolean hasNextSong() {
        return nextSongIndex < songs.size();
    }

    public String getNextSongVideoId() {
        try {
            String nextSongVideoId = songs.get(nextSongIndex).getVideoId();

            ++nextSongIndex;

            return nextSongVideoId;
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("There is no next song in the playlist.");
        }
    }

    @Subscribe
    public void onClientAddedSong(ClientAddedSongEvent event) {
        addSong(event.getOriginBluetoothDeviceAddress(), event.getUrl(), event.getTitle());
        eventBus.post(new PlaylistChangedEvent(songs));
    }

    @Subscribe
    public void onClientRemovedSong(ClientRemovedSongEvent event) {
        int removedSongIndex = findSongIndexById(event.getRemovedSongId());
        if (removedSongIndex == -1)
            return;

        songs.remove(removedSongIndex);
        eventBus.post(new PlaylistChangedEvent(songs));
    }

    @Subscribe
    public void onPlaylistLoaded(PlaylistLoadedEvent event) {
        songs = event.getSongs();

        // No next song
        nextSongIndex = songs.size();

        eventBus.post(new PlaylistChangedEvent(songs));
    }

    private int findSongIndexById(String songId) {
        for (int i = 0; i < songs.size(); ++i) {
            if (songs.get(i).getId().equals(songId))
                return i;
        }

        return -1;
    }

    public void load(File loadFile) {
        new LoadFileThread(loadFile, eventBus).start();
    }
}
