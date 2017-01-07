package com.github.zan_kusterle.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.zan_kusterle.R;
import com.github.zan_kusterle.model.Song;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bencz on 2016. 12. 08..
 */

public class PlaylistAdapter extends BaseAdapter {

    private final Context context;

    private List<Song> items;

    static class ViewHolder {
        @BindView(R.id.songTitleTextView)
        TextView songTitleTextView;

        @BindView(R.id.songURLTextView)
        TextView songURLTextView;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public PlaylistAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }

        return 0;
    }

    @Override
    public Song getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_playlist_song, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Song song = getItem(position);

        holder.songTitleTextView.setText(song.getTitle());
        holder.songURLTextView.setText(song.getUrl());

        // Return the completed view to render on screen
        return convertView;
    }

    public void setItems(List<Song> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
