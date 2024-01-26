package com.tatanstudios.abba.activitys.videos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.network.RetrofitBuilder;

public class VideoServidorActivity extends AppCompatActivity {

    private String urlVideo = "";

    private ExoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Oculta la barra de notificaciones
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Oculta la barra de acción (ActionBar) si la actividad utiliza una
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_video_servidor);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            urlVideo = bundle.getString("URL");
        }

        StyledPlayerView playerView = findViewById(R.id.playerView);
        player = new ExoPlayer.Builder(VideoServidorActivity.this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(RetrofitBuilder.urlImagenes + urlVideo);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }
}