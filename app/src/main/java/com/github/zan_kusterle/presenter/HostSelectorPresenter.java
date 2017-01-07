package com.github.zan_kusterle.presenter;

import android.content.Context;
import android.content.IntentFilter;

import com.github.zan_kusterle.model.BluetoothDeviceBroadcastReceiver;
import com.github.zan_kusterle.model.ClientBluetoothManager;
import com.github.zan_kusterle.model.events.BluetoothDeviceDiscoveredEvent;
import com.github.zan_kusterle.model.events.ConnectionCompleteEvent;
import com.github.zan_kusterle.model.events.ConnectionFailedEvent;
import com.github.zan_kusterle.view.HostSelectorView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by bencz on 2016. 12. 13..
 */

public class HostSelectorPresenter extends MvpBasePresenter<HostSelectorView> {

    private final ClientBluetoothManager clientBluetoothConnectionManager;
    private final BluetoothDeviceBroadcastReceiver bluetoothDeviceBroadcastReceiver;

    private boolean receiverRegistered;

    @Inject
    public HostSelectorPresenter(ClientBluetoothManager clientBluetoothConnectionManager, EventBus eventBus) {
        this.clientBluetoothConnectionManager = clientBluetoothConnectionManager;
        this.bluetoothDeviceBroadcastReceiver = clientBluetoothConnectionManager.getBluetoothDeviceBroadcastReceiver();

        eventBus.register(this);

        receiverRegistered = false;
    }

    public boolean isBluetoothSupported() {
        return clientBluetoothConnectionManager.isBluetoothSupported();
    }

    public void bluetoothNotSupported() {
        getView().showBluetoothNotSupportedDialog();
    }

    public boolean isBluetoothEnabled() {
        return clientBluetoothConnectionManager.isBluetoothEnabled();
    }

    public void bluetoothNotEnabled() {
        getView().showEnableBluetoothDialog();
    }

    public void registerBluetoothDeviceBroadcastReceiver(Context context, IntentFilter filter) {
        context.registerReceiver(bluetoothDeviceBroadcastReceiver, filter);
        receiverRegistered = true;
    }

    public void startBluetoothDeviceDiscovery() {
        clientBluetoothConnectionManager.startDeviceDiscovery();
    }

    public void onConfigurationChanged() {
        getView().setDiscoveredBluetoothDevices(bluetoothDeviceBroadcastReceiver.getDiscoveredBluetoothDevices());
    }

    @Subscribe
    public void onBluetoothDeviceDiscovered(BluetoothDeviceDiscoveredEvent event) {
        getView().setDiscoveredBluetoothDevices(event.getDiscoveredBluetoothDevices());
    }

    public void connectToHost(int deviceIndex) {
        getView().showProgressDialog();

        try {
            clientBluetoothConnectionManager.connectToHost(deviceIndex);
        } catch (IOException e) {
            getView().showConnectionErrorToast(e.getMessage());
        }
    }

    public void unregisterBluetoothDeviceBroadcastReceiver(Context context) {
        if (receiverRegistered) {
            context.unregisterReceiver(bluetoothDeviceBroadcastReceiver);
            receiverRegistered = false;
        }
    }

    @Subscribe
    public void onConnectionComplete(ConnectionCompleteEvent event) {
        getView().hideProgressDialog();
        getView().showPlaylist();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectionFailed(ConnectionFailedEvent event) {
        getView().hideProgressDialog();
        getView().showConnectionErrorToast(event.getErrorMessage());
    }
}
