package com.github.zan_kusterle.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.zan_kusterle.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoleSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selector);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.hostButton)
    public void onHostClick(View v) {
        startActivity(new Intent(this, HostPlaylistActivity.class));
        finish();
    }

    @OnClick(R.id.guestButton)
    public void onGuestClick(View v) {
        startActivity(new Intent(this, HostSelectorActivity.class));
        finish();
    }
}
