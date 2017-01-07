package com.github.zan_kusterle.module;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.Nullable;

import com.github.zan_kusterle.model.BluetoothDeviceBroadcastReceiver;
import com.github.zan_kusterle.model.ClientBluetoothManager;
import com.github.zan_kusterle.model.ClientPlaylist;
import com.github.zan_kusterle.model.HostBluetoothManager;
import com.github.zan_kusterle.model.HostPlaylist;
import com.github.zan_kusterle.presenter.ClientPlaylistPresenter;
import com.github.zan_kusterle.presenter.HostPlaylistPresenter;
import com.github.zan_kusterle.presenter.HostSelectorPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bencz on 2016. 12. 18..
 */

@Module
public class GuppyModule {

    @Singleton
    @Provides
    public ClientPlaylist provideClientPlaylist(EventBus eventBus) {
        return new ClientPlaylist(eventBus);
    }

    @Singleton
    @Provides
    public HostPlaylist provideHostPlaylist(EventBus eventBus) {
        return new HostPlaylist(eventBus);
    }

    @Singleton
    @Provides
    public ClientBluetoothManager provideClientBluetoothConnectionManager(@Nullable BluetoothAdapter adapter,
                                                                          BluetoothDeviceBroadcastReceiver bluetoothDeviceBroadcastReceiver,
                                                                          EventBus eventBus) {
        return new ClientBluetoothManager(adapter, bluetoothDeviceBroadcastReceiver, eventBus);
    }

    @Singleton
    @Provides
    public HostBluetoothManager provideHostBluetoothConnectionManager(@Nullable BluetoothAdapter adapter,
                                                                      EventBus eventBus) {
        return new HostBluetoothManager(adapter, eventBus);
    }

    @Singleton
    @Provides
    public BluetoothDeviceBroadcastReceiver provideBluetoothDeviceBroadcastReceiver(EventBus eventBus) {
        return new BluetoothDeviceBroadcastReceiver(eventBus);
    }

    @Singleton
    @Provides
    public HostPlaylistPresenter provideHostPlaylistPresenter(HostBluetoothManager hostBluetoothConnectionManager, HostPlaylist playlist, EventBus eventBus) {
        return new HostPlaylistPresenter(hostBluetoothConnectionManager, playlist, eventBus);
    }

    @Singleton
    @Provides
    public ClientPlaylistPresenter provideClientPlaylistPresenter(ClientBluetoothManager clientBluetoothConnectionManager,
                                                                  ClientPlaylist playlist,
                                                                  EventBus eventBus) {
        return new ClientPlaylistPresenter(clientBluetoothConnectionManager, playlist, eventBus);
    }

    @Singleton
    @Provides
    public HostSelectorPresenter provideHostSelectorPresenter(ClientBluetoothManager clientBluetoothConnectionManager, EventBus eventBus) {
        return new HostSelectorPresenter(clientBluetoothConnectionManager, eventBus);
    }
}
