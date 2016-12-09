package com.github.zan_kusterle.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.zan_kusterle.R;
import com.github.zan_kusterle.model.Song;
import com.github.zan_kusterle.model.SongsAdapter;
import com.github.zan_kusterle.presenter.PlaylistPresenter;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaylistActivity extends MvpActivity<PlaylistView, PlaylistPresenter>
        implements PlaylistView {

    private static final int PLAYLIST_ACTIVITY_REQUEST_CODE = 1;

    @BindView(R.id.playlistListView)
    public ListView playlistListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.playlistToolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @Override
    public PlaylistPresenter createPresenter() {
        return new PlaylistPresenter();
    }

    @OnClick(R.id.addSongFab)
    public void onAddSongFabClick() {
        startActivityForResult(new Intent(this, SongPickerActivity.class), PLAYLIST_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playlist_menu, menu);

        boolean isHost = getIntent().getBooleanExtra("isHost", false);
        if (!isHost) {
            menu.findItem(R.id.loadPlaylistMenuItem)
                    .setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loadPlaylistMenuItem:
                presenter.loadPlaylist();
                return true;
            case R.id.savePlaylistMenuItem:
                presenter.savePlaylist();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPlaylist(List<Song> playlist) {
        playlistListView.setAdapter(new SongsAdapter(this, playlist));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLAYLIST_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            getPresenter().addSong(data.getStringExtra("URL"), data.getStringExtra("title"));
        }
    }
}
