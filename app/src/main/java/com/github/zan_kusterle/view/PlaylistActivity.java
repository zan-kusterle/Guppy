package com.github.zan_kusterle.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.zan_kusterle.R;
import com.github.zan_kusterle.model.Song;
import com.github.zan_kusterle.presenter.PlaylistPresenter;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.SaveFileDialog;

import java.io.File;
import java.util.List;

/**
 * Created by bencz on 2016. 12. 20..
 */

public abstract class PlaylistActivity extends MvpActivity<PlaylistView, PlaylistPresenter>
        implements PlaylistView, AdapterView.OnItemLongClickListener, FileDialog.OnFileSelectedListener {

    protected static final int REQUEST_ADD_SONG = 1;

    protected PlaylistAdapter playlistAdapter;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public abstract PlaylistPresenter createPresenter();

    public abstract boolean onCreateOptionsMenu(Menu menu);

    public abstract boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id);

    @Override
    public void setPlaylist(List<Song> songs) {
        playlistAdapter.setItems(songs);
    }

    @Override
    public void showRemovalConfirmationDialog(final String songId) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.text_song_removal_dialog_title)
                .setMessage(R.string.text_song_removal_dialog_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.removeSong(songId);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savePlaylistMenuItem:
                presenter.onSavePlaylistClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playlistAdapter = new PlaylistAdapter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_SONG && resultCode == RESULT_OK) {
            presenter.addSong(data.getStringExtra("URL"), data.getStringExtra("title"));
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showSaveDialog() {
        showFileDialog(new SaveFileDialog(), SaveFileDialog.class.getName());
    }

    public abstract void onFileSelected(FileDialog dialog, File file);

    protected void onAddSongFabClick() {
        startActivityForResult(new Intent(this, SongPickerActivity.class), REQUEST_ADD_SONG);
    }

    protected void showFileDialog(FileDialog dialog, String tag) {
        Bundle args = new Bundle();
        args.putString(FileDialog.EXTENSION, ".gpl");
        dialog.setArguments(args);
        dialog.setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        dialog.show(getSupportFragmentManager(), tag);
    }
}
