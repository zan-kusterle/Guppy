package com.github.zan_kusterle.view;

import com.github.zan_kusterle.model.Song;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by bencz on 2016. 12. 19..
 */

public interface PlaylistView extends MvpView {
    void setPlaylist(List<Song> songs);
    void showRemovalConfirmationDialog(String songId);
    void showToast(String message);
    void showSaveDialog();
}
