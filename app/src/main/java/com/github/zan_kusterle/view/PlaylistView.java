package com.github.zan_kusterle.view;

import com.github.zan_kusterle.model.Song;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by bencz on 2016. 12. 08..
 */

public interface PlaylistView extends MvpView {
    void setPlaylist(List<Song> playlist);
}
