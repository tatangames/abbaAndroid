package com.tatanstudios.abba.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;


import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.tatanstudios.abba.R;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class PruebaActivity extends AppCompatActivity {

    ExoPlayer player;
    String videourl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

    private WebView webView;


    private YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Oculta la barra de notificaciones
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Oculta la barra de acción (ActionBar) si la actividad utiliza una
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_prueba);
        /*String facebookVideoUrl = "https://www.facebook.com/reel/pNvtHJsCPO";




        // Crea un Intent con la acción ACTION_VIEW y la URI del video de Facebook
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookVideoUrl));

        // Verifica si hay aplicaciones que pueden manejar la acción VIEW (navegador o aplicación de Facebook)
        boolean facebookAppFound = false;
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + facebookVideoUrl));
        if (facebookIntent.resolveActivity(getPackageManager()) != null) {
            facebookAppFound = true;
        }

        // Crea un Intent Chooser personalizado
        Intent chooser;
        if (facebookAppFound) {
            chooser = Intent.createChooser(intent, "Abrir con...");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { facebookIntent });
        } else {
            chooser = Intent.createChooser(intent, "Abrir con navegador");
        }

        // Muestra el diálogo de elección
        startActivity(chooser);

        // Finaliza la actividad actual (opcional)
        finish();
*/



        String instagramProfileUrl = "https://www.instagram.com/reel/C2S6Pi0voaS";

        // Crea un Intent con la acción ACTION_VIEW y la URI del perfil de Instagram
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramProfileUrl));

        // Muestra el diálogo de elección para que el usuario elija un navegador o la aplicación de Instagram
        startActivity(Intent.createChooser(intent, "Abrir con..."));

        finish();




        /*webView = findViewById(R.id.webView);

        // Habilita la ejecución de JavaScript en el WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Configura el cliente Chrome para reproducir videos
        webView.setWebChromeClient(new WebChromeClient());

        // Carga la URL del video de Facebook
        String facebookVideoUrl = "https://www.facebook.com/reel/1681591828991728";
        webView.loadUrl(facebookVideoUrl);*/



       /* youTubePlayerView = findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // Reemplaza "VIDEO_ID" con el ID del video de YouTube que deseas reproducir
                youTubePlayer.loadVideo("Lb-GF26UHpI", 0);
            }
        });*/

        /*StyledPlayerView playerView = findViewById(R.id.playerview);
        player = new ExoPlayer.Builder(PruebaActivity.this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(videourl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);




            <com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
        android:id="@+id/youtube_player_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

        */
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }

}