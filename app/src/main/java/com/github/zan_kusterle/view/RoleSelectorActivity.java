package com.github.zan_kusterle.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.zan_kusterle.R;
import com.github.zan_kusterle.presenter.RoleSelectorPresenter;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoleSelectorActivity extends MvpActivity<RoleSelectorView, RoleSelectorPresenter>
        implements RoleSelectorView {

    private Intent activityChangeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selector);

        ButterKnife.bind(this);

        activityChangeIntent = new Intent(this, PlaylistActivity.class);
    }

    @Override
    public RoleSelectorPresenter createPresenter() {
        return new RoleSelectorPresenter();
    }

    @OnClick(R.id.hostButton)
    public void onHostClick(View v) {
        activityChangeIntent.putExtra("isHost", true);
        startActivity(activityChangeIntent);
        finish();
    }

    @OnClick(R.id.guestButton)
    public void onGuestClick(View v) {
        activityChangeIntent.putExtra("isHost", false);
        startActivity(activityChangeIntent);
        finish();
    }
}
