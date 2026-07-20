package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || intent.getAction() == null)
            return;

        switch (intent.getAction()) {

            case ACTION_PLAY_PAUSE:

                if (MusicService.isPlaying()) {
                    MusicService.pauseSong();
                } else {
                    MusicService.resumeSong();
                }
                break;

            case ACTION_NEXT:
                // Will connect this with PlayerActivity later
                break;

            case ACTION_PREVIOUS:
                // Will connect this with PlayerActivity later
                break;
        }
    }
}