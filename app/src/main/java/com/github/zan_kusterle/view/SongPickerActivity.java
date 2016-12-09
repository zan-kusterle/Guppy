package com.github.zan_kusterle.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.zan_kusterle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongPickerActivity extends AppCompatActivity {

    @BindView(R.id.songPickerWebView)
    public WebView songPickerWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.songPickerToolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Setup WebView
        songPickerWebView.getSettings()
                .setJavaScriptEnabled(true);

        songPickerWebView.setWebChromeClient(new WebChromeClient());
        songPickerWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        songPickerWebView.loadUrl("https://www.youtube.com/");
    }

    @OnClick(R.id.pickSongFab)
    public void onPickSongFabClick() {
        String songUrl = songPickerWebView.getUrl();

        // Allow only video URLs
        if (!songUrl.contains("/watch?v=")) {
            return;
        }

        // Parse the song's title
        String songTitle = songPickerWebView.getTitle();
        songTitle = songTitle.substring(0, songTitle.lastIndexOf(" - YouTube"));

        // Destroy audio/video objects
        songPickerWebView.loadUrl("about:blank");

        // Pass the song's URL to the previous activity
        Intent data = new Intent();
        data.putExtra("title", songTitle);
        data.putExtra("URL", songUrl);
        setResult(RESULT_OK, data);

        // Finish activity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy WebView
        ViewGroup parent = (ViewGroup) songPickerWebView.getParent();
        if (parent != null) {
            parent.removeView(songPickerWebView);
        }

        songPickerWebView.removeAllViews();
        songPickerWebView.destroy();
    }
}
