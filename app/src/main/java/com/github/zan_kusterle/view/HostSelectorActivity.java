package com.github.zan_kusterle.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zan_kusterle.GuppyApplication;
import com.github.zan_kusterle.R;
import com.github.zan_kusterle.presenter.HostSelectorPresenter;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostSelectorActivity extends MvpActivity<HostSelectorView, HostSelectorPresenter>
        implements HostSelectorView, AdapterView.OnItemClickListener {

    @BindView(R.id.bluetoothDevicesListView)
    ListView bluetoothDevicesListView;

    @BindView(R.id.emptyBluetoothDevicesListTextView)
    TextView emptyBluetoothDevicesListTextView;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private DiscoveredBluetoothDevicesAdapter discoveredBluetoothDevicesAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_selector);

        ButterKnife.bind(this);

        if (!presenter.isBluetoothSupported()) {
            presenter.bluetoothNotSupported();
            return;
        }

        discoveredBluetoothDevicesAdapter = new DiscoveredBluetoothDevicesAdapter(this);
        bluetoothDevicesListView.setAdapter(discoveredBluetoothDevicesAdapter);
        bluetoothDevicesListView.setEmptyView(emptyBluetoothDevicesListTextView);
        bluetoothDevicesListView.setOnItemClickListener(this);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        presenter.registerBluetoothDeviceBroadcastReceiver(this, filter);

        if (presenter.isBluetoothEnabled())
            presenter.startBluetoothDeviceDiscovery();
        else
            presenter.bluetoothNotEnabled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.unregisterBluetoothDeviceBroadcastReceiver(this);
    }

    @Override
    public HostSelectorPresenter createPresenter() {
        return ((GuppyApplication) getApplication()).getApplicationComponent()
                .provideHostSelectorPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            presenter.startBluetoothDeviceDiscovery();
        } else if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_CANCELED) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.text_bluetooth_not_enabled_title)
                    .setMessage(R.string.text_bluetooth_not_enabled_message)
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.connectToHost(position);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        presenter.onConfigurationChanged();
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
    public void showEnableBluetoothDialog() {
        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void setDiscoveredBluetoothDevices(List<BluetoothDevice> discoveredBluetoothDevices) {
        discoveredBluetoothDevicesAdapter.setItems(discoveredBluetoothDevices);
    }

    @Override
    public void showConnectionErrorToast(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showProgressDialog() {
        String title = getResources().getString(R.string.text_connection_dialog_title);
        String text = getResources().getString(R.string.text_connection_dialog_text);

        progressDialog = ProgressDialog.show(this, title, text, true);
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showPlaylist() {
        startActivity(new Intent(this, ClientPlaylistActivity.class));
        finish();
    }
}
