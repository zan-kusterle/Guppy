package com.github.zan_kusterle.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.github.zan_kusterle.model.events.ClientAddedSongEvent;
import com.github.zan_kusterle.model.events.ClientConnectedEvent;
import com.github.zan_kusterle.model.events.ClientRemovedSongEvent;
import com.github.zan_kusterle.model.events.ClientRequestedPlaylistSyncEvent;
import com.github.zan_kusterle.model.events.CommunicationErrorEvent;
import com.github.zan_kusterle.model.events.DataReceivedEvent;

import org.apache.commons.lang3.SerializationUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by bencz on 2016. 12. 13..
 */

public class HostBluetoothManager extends BluetoothManager {

    private final HashMap<String, CommunicationThread> communicationThreads;

    private BluetoothServerSocket bluetoothServerSocket;

    @Inject
    public HostBluetoothManager(BluetoothAdapter adapter, EventBus eventBus) {
        super(adapter, eventBus);
        communicationThreads = new HashMap<>();

        eventBus.register(this);
    }

    public void acceptConnections() throws IOException {
        bluetoothServerSocket = adapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, SERVICE_UUID);
        new AcceptanceThread(bluetoothServerSocket, eventBus).start();
    }

    public void sendSongsToClient(String clientBluetoothDeviceAddress, List<Song> songs) throws IOException {
        if (songs.size() == 0)
            return;

        byte[] data = getSerializedSetSongsIntention(songs);

        CommunicationThread communicationThread = communicationThreads.get(clientBluetoothDeviceAddress);

        sendDataToClient(communicationThread, data);
    }

    public void sendSongsToClients(List<Song> songs) throws IOException {
        if (communicationThreads.size() == 0)
            return;

        byte[] data = getSerializedSetSongsIntention(songs);

        for (CommunicationThread communicationThread : communicationThreads.values())
            sendDataToClient(communicationThread, data);
    }

    @Subscribe
    public void onClientConnected(ClientConnectedEvent event) {
        BluetoothSocket socket = event.getClientBluetoothSocket();

        try {
            CommunicationThread thread = new CommunicationThread(socket, eventBus);
            thread.start();

            communicationThreads.put(socket.getRemoteDevice().getAddress(), thread);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onCommunicationError(CommunicationErrorEvent event) {
        communicationThreads.remove(event.getBluetoothDeviceAddress());
    }

    @Subscribe
    public void onDataReceived(DataReceivedEvent event) {
        Intention receivedIntention = SerializationUtils.deserialize(event.getData());

        switch (receivedIntention.getType()) {
            case SYNC_SONGS:
                eventBus.post(new ClientRequestedPlaylistSyncEvent(event.getSenderBluetoothDeviceAddress()));
                break;
            case ADD_SONG:
                ClientNewSongChoice clientNewSongChoice = ((AddSongIntention) receivedIntention).getClientNewSongChoice();

                String url = clientNewSongChoice.getUrl();
                String title = clientNewSongChoice.getTitle();

                eventBus.post(new ClientAddedSongEvent(event.getSenderBluetoothDeviceAddress(), url, title));
                break;
            case REMOVE_SONG:
                String removedSongId = ((RemoveSongIntention) receivedIntention).getRemovedSongId();
                eventBus.post(new ClientRemovedSongEvent(removedSongId));
                break;
        }
    }

    private byte[] getSerializedSetSongsIntention(List<Song> songs) {
        SetSongsIntention setSongsIntention = new SetSongsIntention(IntentionType.SET_SONGS, songs);
        return SerializationUtils.serialize(setSongsIntention);
    }

    private void sendDataToClient(CommunicationThread communicationThread, byte[] data) throws IOException {
        communicationThread.writeDataLength(data.length);
        communicationThread.writeData(data);
    }
}
