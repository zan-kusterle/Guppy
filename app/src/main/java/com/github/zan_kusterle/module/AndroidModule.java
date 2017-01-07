package com.github.zan_kusterle.module;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bencz on 2016. 12. 18..
 */

@Module
public class AndroidModule {
    @Singleton
    @Provides
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Singleton
    @Provides
    @Nullable
    public BluetoothAdapter provideBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }
}
