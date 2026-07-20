package com.example.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ImageView imgAlbumArt;

    TextView songTitle;
    TextView txtArtist;
    TextView txtCurrentTime;
    TextView txtTotalTime;

    Button btnPlayPause;
    Button btnPrevious;
    Button btnNext;

    SeekBar seekBar;

    Handler handler = new Handler();

    ArrayList<Song> songList;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        imgAlbumArt = findViewById(R.id.imgAlbumArt);

        songTitle = findViewById(R.id.songTitle);
        txtArtist = findViewById(R.id.txtArtist);

        txtCurrentTime = findViewById(R.id.txtCurrentTime);
        txtTotalTime = findViewById(R.id.txtTotalTime);

        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        seekBar = findViewById(R.id.seekBar);

        songList = (ArrayList<Song>) getIntent().getSerializableExtra("songList");
        position = getIntent().getIntExtra("position", 0);
        MusicManager.setSongList(songList);
        MusicManager.setCurrentPosition(position);

        playSong(position);

        btnPlayPause.setOnClickListener(v -> {

            if (MusicService.isPlaying()) {

                MusicService.pauseSong();
                btnPlayPause.setText("Play");

            } else {

                MusicService.resumeSong();
                btnPlayPause.setText("Pause");

            }

        });
        btnNext.setOnClickListener(v -> {

            Song nextSong = MusicManager.getNextSong();

            if (nextSong != null) {

                position = MusicManager.getCurrentPosition();
                playSong(position);

            }

        });
        btnPrevious.setOnClickListener(v -> {

            Song previousSong = MusicManager.getPreviousSong();

            if (previousSong != null) {

                position = MusicManager.getCurrentPosition();
                playSong(position);

            }

        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {

                    MusicService.seekTo(progress);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

    }

    private void playSong(int index) {
        MusicManager.setCurrentPosition(index);

        Song song = songList.get(index);

        songTitle.setText(song.getTitle());

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {

            retriever.setDataSource(song.getPath());

            String artist = retriever.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_ARTIST);

            if (artist == null)
                artist = "Unknown Artist";

            txtArtist.setText(artist);

        } catch (Exception e) {

            txtArtist.setText("Unknown Artist");

        }

        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();

            mmr.setDataSource(song.getPath());

            byte[] art = mmr.getEmbeddedPicture();

            if (art != null) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);

                imgAlbumArt.setImageBitmap(bitmap);

            } else {

                imgAlbumArt.setImageResource(android.R.drawable.ic_menu_gallery);

            }

        } catch (Exception e) {

            imgAlbumArt.setImageResource(android.R.drawable.ic_menu_gallery);

        }

        MusicService.playSong(song.getPath());

        seekBar.setMax(MusicService.getDuration());

        txtTotalTime.setText(formatTime(MusicService.getDuration()));

        updateSeekBar();

        btnPlayPause.setText("Pause");

    }

    private void updateSeekBar() {

        handler.post(new Runnable() {

            @Override
            public void run() {

                if (MusicService.isPlaying()) {

                    int current = MusicService.getCurrentPosition();

                    seekBar.setProgress(current);

                    txtCurrentTime.setText(formatTime(current));

                    handler.postDelayed(this, 500);

                }

            }

        });

    }

    private String formatTime(int milliseconds) {

        int minutes = milliseconds / 1000 / 60;
        int seconds = (milliseconds / 1000) % 60;

        return String.format("%02d:%02d", minutes, seconds);

    }

}