package com.github.zan_kusterle.view;

/**
 * Created by bencz on 2016. 12. 08..
 */

public interface HostPlaylistView extends PlaylistView {
    void showBluetoothNotSupportedDialog();
    void showEnableDiscoverabilityDialog();
    void loadVideo(String videoId);
    void showLoadDialog();
}
