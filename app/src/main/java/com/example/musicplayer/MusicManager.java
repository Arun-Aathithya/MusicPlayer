package com.example.musicplayer;

import java.util.ArrayList;

public class MusicManager {

    private static ArrayList<Song> songList = new ArrayList<>();
    private static int currentPosition = 0;

    public static void setSongList(ArrayList<Song> songs) {
        songList = songs;
    }

    public static ArrayList<Song> getSongList() {
        return songList;
    }

    public static void setCurrentPosition(int position) {
        currentPosition = position;
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static Song getCurrentSong() {
        if (songList == null || songList.isEmpty()) {
            return null;
        }
        return songList.get(currentPosition);
    }

    public static Song getNextSong() {
        if (songList == null || songList.isEmpty()) {
            return null;
        }

        currentPosition++;

        if (currentPosition >= songList.size()) {
            currentPosition = 0;
        }

        return songList.get(currentPosition);
    }

    public static Song getPreviousSong() {
        if (songList == null || songList.isEmpty()) {
            return null;
        }

        currentPosition--;

        if (currentPosition < 0) {
            currentPosition = songList.size() - 1;
        }

        return songList.get(currentPosition);
    }
}