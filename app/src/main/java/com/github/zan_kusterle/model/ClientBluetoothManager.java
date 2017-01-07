package com.github.zan_kusterle.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import com.github.zan_kusterle.model.events.ConnectionCompleteEvent;
import com.github.zan_kusterle.model.events.DataReceivedEvent;
import com.github.zan_kusterle.model.events.PlaylistChangedEvent;

import org.apache.commons.lang3.SerializationUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;

/**
 * Created by bencz on 2016. 12. 13..
 */

public class ClientBluetoothManager extends BluetoothManager {

    @Getter
    private final BluetoothDeviceBroadcastReceiver bluetoothDeviceBroadcastReceiver;

    private BluetoothSocket hostBluetoothSocket;
    private CommunicationThread communicationThread;

    @Inject
    public ClientBluetoothManager(BluetoothAdapter adapter,
                                  BluetoothDeviceBroadcastReceiver bluetoothDeviceBroadcastReceiver,
                                  EventBus eventBus) {
        super(adapter, eventBus);

        this.bluetoothDeviceBroadcastReceiver = bluetoothDeviceBroadcastReceiver;

        eventBus.register(this);
    }

    public void startDeviceDiscovery() {
        adapter.startDiscovery();
    }

    public void connectToHost(int deviceIndex) throws IOException {
        adapter.cancelDiscovery();

        hostBluetoothSocket = bluetoothDeviceBroadcastReceiver.getDiscoveredBluetoothDevice(deviceIndex)
                .createRfcommSocketToServiceRecord(SERVICE_UUID);
        new ConnectionThread(hostBluetoothSocket, eventBus).start();
    }

    public void syncSongsWithHost() throws IOException {
        Intention syncSongsIntention = new Intention(IntentionType.SYNC_SONGS);

        byte[] data = SerializationUtils.serialize(syncSongsIntention);

        sendDataToHost(data);
    }

    public void sendNewSongChoiceToHost(String url, String title) throws IOException {
        ClientNewSongChoice songChoice = new ClientNewSongChoice(url, title);
        AddSongIntention addSongIntention = new AddSongIntention(IntentionType.ADD_SONG, songChoice);

        byte[] data = SerializationUtils.serialize(addSongIntention);

        sendDataToHost(data);
    }

    public void sendRemovableSongIdToHost(String songId) throws IOException {
        RemoveSongIntention removeSongIntention = new RemoveSongIntention(IntentionType.REMOVE_SONG, songId);

        byte[] data = SerializationUtils.serialize(removeSongIntention);

        sendDataToHost(data);
    }

    @Subscribe
    public void onConnectionComplete(ConnectionCompleteEvent event) {
        try {
            communicationThread = new CommunicationThread(hostBluetoothSocket, eventBus);
            communicationThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onDataReceived(DataReceivedEvent event) {
        Intention receivedIntention = SerializationUtils.deserialize(event.getData());

        switch (receivedIntention.getType()) {
            case SET_SONGS:
                List<Song> songs = ((SetSongsIntention) receivedIntention).getSongs();
                eventBus.post(new PlaylistChangedEvent(songs));
                break;
        }
    }

    private void sendDataToHost(byte[] data) throws IOException {
        communicationThread.writeDataLength(data.length);
        communicationThread.writeData(data);
    }
}
