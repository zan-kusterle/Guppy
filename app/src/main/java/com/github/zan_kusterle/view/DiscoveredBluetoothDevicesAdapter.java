package com.github.zan_kusterle.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.zan_kusterle.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bencz on 2016. 12. 13..
 */

public class DiscoveredBluetoothDevicesAdapter extends BaseAdapter {

    private final Context context;

    private List<BluetoothDevice> items;

    static class ViewHolder {
        @BindView(R.id.deviceNameTextView)
        TextView deviceNameTextView;

        @BindView(R.id.deviceAddressTextView)
        TextView deviceAddressTextView;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public DiscoveredBluetoothDevicesAdapter(Context context) {
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
    public BluetoothDevice getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_host_selector_device, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device = getItem(position);

        holder.deviceNameTextView.setText(device.getName());
        holder.deviceAddressTextView.setText(device.getAddress());

        // Return the completed view to render on screen
        return convertView;
    }

    public void setItems(List<BluetoothDevice> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
