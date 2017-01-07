package com.github.zan_kusterle.view;

import android.bluetooth.BluetoothDevice;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by bencz on 2016. 12. 13..
 */

public interface HostSelectorView extends MvpView {
    void showBluetoothNotSupportedDialog();
    void showEnableBluetoothDialog();
    void setDiscoveredBluetoothDevices(List<BluetoothDevice> discoveredBluetoothDevices);
    void showConnectionErrorToast(String errorMessage);
    void showProgressDialog();
    void hideProgressDialog();
    void showPlaylist();
}
