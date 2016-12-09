package com.github.zan_kusterle.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.zan_kusterle.R;

import java.util.List;

/**
 * Created by bencz on 2016. 12. 08..
 */

public class SongsAdapter extends ArrayAdapter<Song> {

    public SongsAdapter(Context context, List<Song> songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_playlist_song, parent, false);
        }

        // Lookup view for data population
        TextView songTitleTextView = (TextView) convertView.findViewById(R.id.songTitleTextView);
        TextView songURLTextView = (TextView) convertView.findViewById(R.id.songURLTextView);

        songTitleTextView.setText(song.getTitle());
        songURLTextView.setText(song.getURL());

        // Return the completed view to render on screen
        return convertView;
    }
}
