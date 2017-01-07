package com.github.zan_kusterle.view;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zan_kusterle.GuppyApplication;
import com.github.zan_kusterle.R;
import com.github.zan_kusterle.presenter.HostPlaylistPresenter;
import com.github.zan_kusterle.presenter.PlaylistPresenter;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HostPlaylistActivity extends PlaylistActivity
        implements HostPlaylistView, AdapterView.OnItemClickListener, YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlayerStateChangeListener {

    @BindView(R.id.hostPlaylistToolbar)
    Toolbar playlistToolbar;

    @BindView(R.id.hostPlaylistListView)
    ListView playlistListView;

    @BindView(R.id.hostEmptyPlaylistTextView)
    TextView emptyPlaylistTextView;

    private static final String YOUTUBE_API_KEY = "AIzaSyAe3o3dr6GbzBWiU4zo7DGBgD1oZQW-ZDc";

    private static final int REQUEST_ENABLE_DEVICE_DISCOVERABILITY = 2;

    private static final int RQS_ERROR_DIALOG = 1;

    private YouTubePlayerFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_playlist);

        ButterKnife.bind(this);

        playlistToolbar.showOverflowMenu();
        setSupportActionBar(playlistToolbar);

        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youTubePlayerFragment);
        youTubePlayerFragment.initialize(YOUTUBE_API_KEY, this);

        playlistListView.setEmptyView(emptyPlaylistTextView);
        playlistListView.setOnItemClickListener(this);
        playlistListView.setOnItemLongClickListener(this);

        playlistListView.setAdapter(playlistAdapter);
        presenter.onConfigurationChanged();

        if (!((HostPlaylistPresenter) presenter).isBluetoothSupported()) {
            ((HostPlaylistPresenter) presenter).onBluetoothNotSupported();
            return;
        }

        ((HostPlaylistPresenter) presenter).enableBluetoothDiscoverability();
    }

    @Override
    public PlaylistPresenter createPresenter() {
        return ((GuppyApplication) getApplication()).getApplicationComponent()
                .provideHostPlaylistPresenter();
    }

    @OnClick(R.id.hostAddSongFab)
    public void onHostAddSongFabClick() {
        super.onAddSongFabClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.host_playlist_menu, menu);
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onSongLongClick(position);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loadPlaylistMenuItem:
                ((HostPlaylistPresenter ) presenter).onLoadPlaylistClicked();
                return true;
            case R.id.savePlaylistMenuItem:
                presenter.onSavePlaylistClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_DEVICE_DISCOVERABILITY && resultCode == RESULT_CANCELED) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.text_bluetooth_discoverability_not_enabled_title)
                    .setMessage(R.string.text_bluetooth_discoverability_not_enabled_message)
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();

            dialog.show();
        } else if (requestCode == REQUEST_ENABLE_DEVICE_DISCOVERABILITY && resultCode == 1) {
            ((HostPlaylistPresenter) presenter).acceptConnections();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFileSelected(FileDialog dialog, File file) {
        if (dialog instanceof OpenFileDialog) {
            ((HostPlaylistPresenter) presenter).loadPlaylist(file);
        } else {
            presenter.savePlaylist(file);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RQS_ERROR_DIALOG).show();
        } else {
            String errorMessage = String.format("YouTube Player initialization failure: %s", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((HostPlaylistPresenter ) presenter).onSongClick(position);
    }

    @Override
    public void showBluetoothNotSupportedDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.text_bluetooth_unsupported_title)
                .setMessage(R.string.text_bluetooth_unsupported_message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create();

        dialog.show();
    }

    @Override
    public void showEnableDiscoverabilityDialog() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                .putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivityForResult(discoverableIntent, REQUEST_ENABLE_DEVICE_DISCOVERABILITY);
    }

    @Override
    public void showLoadDialog() {
        showFileDialog(new OpenFileDialog(), OpenFileDialog.class.getName());
    }

    @Override
    public void loadVideo(String videoId) {
        youTubePlayer.loadVideo(videoId);
    }

    @Override
    public void onLoading() {}

    @Override
    public void onLoaded(String s) {}

    @Override
    public void onAdStarted() {}

    @Override
    public void onVideoStarted() {}

    @Override
    public void onVideoEnded() {
        ((HostPlaylistPresenter) presenter).onVideoEnded();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {}
}
