package com.example.musicplayer;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Song> songList;
    SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        recyclerView = findViewById(R.id.recyclerSongs);

        songList = new ArrayList<>();

        loadSongs();

        adapter = new SongAdapter(this, songList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadSongs() {

        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.IS_MUSIC + "!=0",
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                String path = cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                songList.add(new Song(title, path));
            }

            cursor.close();
        }
    }
}