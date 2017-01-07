package com.github.zan_kusterle.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.zan_kusterle.GuppyApplication;
import com.github.zan_kusterle.R;
import com.github.zan_kusterle.presenter.ClientPlaylistPresenter;
import com.github.zan_kusterle.presenter.PlaylistPresenter;
import com.rustamg.filedialogs.FileDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientPlaylistActivity extends PlaylistActivity implements ClientPlaylistView {

    @BindView(R.id.clientPlaylistToolbar)
    Toolbar playlistToolbar;

    @BindView(R.id.clientPlaylistListView)
    ListView playlistListView;

    @BindView(R.id.clientEmptyPlaylistTextView)
    TextView emptyPlaylistTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_playlist);

        ButterKnife.bind(this);

        playlistToolbar.showOverflowMenu();
        setSupportActionBar(playlistToolbar);

        playlistListView.setEmptyView(emptyPlaylistTextView);
        playlistListView.setOnItemLongClickListener(this);

        playlistListView.setAdapter(playlistAdapter);

        ((ClientPlaylistPresenter) presenter).syncSongsWithHost();
    }

    @Override
    public void onFileSelected(FileDialog dialog, File file) {
        presenter.savePlaylist(file);
    }

    @Override
    public PlaylistPresenter createPresenter() {
        return presenter = ((GuppyApplication) getApplication()).getApplicationComponent()
                .provideClientPlaylistPresenter();
    }

    @OnClick(R.id.clientAddSongFab)
    public void onClientAddSongFabClick() {
        super.onAddSongFabClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_playlist_menu, menu);
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onSongLongClick(position);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        presenter.onConfigurationChanged();
    }
}
