package com.estebanzapata.getschwifty;

import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    static ImageView cromulon;
    static ImageButton play;
    static ImageButton reset;
    static ImageButton loop;
    boolean loopBoolean;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.getschwifty);

        cromulon = (ImageView) findViewById(R.id.cromulon);
        play = (ImageButton) findViewById(R.id.play);
        reset = (ImageButton) findViewById(R.id.reset);
        loop = (ImageButton) findViewById(R.id.loop);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.seekTo(0);
                            mediaPlayer.pause();

                            if (loopBoolean) {
                                mediaPlayer.start();
                            }

                            syncImages();
                        }
                    });
                }
                syncImages();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(0);
            }
        });

        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loopBoolean) {
                    loop.setBackgroundResource(R.color.cromulonSpace);
                    loopBoolean = false;
                } else {
                    loop.setBackgroundResource(R.color.cromulonSpaceActive);
                    loopBoolean = true;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        currentPos = mediaPlayer.getCurrentPosition();
        System.out.println(currentPos);
        mediaPlayer.release();
        mediaPlayer = null;

        syncImages();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.getschwifty);
        mediaPlayer.seekTo(currentPos);

        syncImages();
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("Stopped");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about: {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder.setTitle("About");
                alertDialogBuilder.setMessage("This app was built for educational purposes only to learn Android application creation.\n" +
                        "Source code is available at \n" +
                        "https://github.com/EstebanZapata/GetSchwifty\n" +
                        "\"Get Schwifty\" is owned by Williams Street Records, and is popularly known from Rick and Morty, owned by Adult Swim.\n" +
                        "It can be found at \n" +
                        "https://soundcloud.com/wmstrecs/get-schwifty-full-track-1\n" +
                        "The cromulon images are owned by thebuggalo and can be found at \n" +
                        "https://www.teepublic.com/user/thebuggalo");

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;
            }
        }

        return true;
    }

    protected void syncImages() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                play.setImageResource(R.drawable.ic_media_pause);
                cromulon.setImageResource(R.drawable.likeit);
            } else {
                play.setImageResource(R.drawable.ic_media_play);
                cromulon.setImageResource(R.drawable.showme);
            }

    }
}
