package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {

    public static final String CHANNEL_ID = "MusicPlayerChannel";

    private static MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            String path = intent.getStringExtra("path");
            String title = intent.getStringExtra("title");

            if (path != null) {

                playSong(path);

                startForeground(
                        1,
                        createNotification(title)
                );
            }
        }

        return START_STICKY;
    }

    private Notification createNotification(String title) {

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText("Playing Music")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true)
                .build();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Player",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void playSong(String path) {

        try {

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pauseSong() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void resumeSong() {

        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static boolean isPlaying() {

        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public static void seekTo(int position) {

        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public static int getDuration() {

        if (mediaPlayer == null)
            return 0;

        return mediaPlayer.getDuration();
    }

    public static int getCurrentPosition() {

        if (mediaPlayer == null)
            return 0;

        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}