package com.example.akashbhagat.aceshubinduction;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4;
    MediaPlayer mediaplayer;
    double starttime = 0;
    double finaltime = 0;
    int forwardTime = 5000;
    int backwordTime = 5000;
    SeekBar seekbar;
    TextView txt1, txt2, txt3;

    Spinner spin;
    String songName;
    Handler myhandler;


    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.back);
        b2 = (Button) findViewById(R.id.pause);
        b3 = (Button) findViewById(R.id.play);
        b4 = (Button) findViewById(R.id.forward);

        txt1 = (TextView) findViewById(R.id.textview2);
        txt2 = (TextView) findViewById(R.id.textview3);
        txt3 = (TextView) findViewById(R.id.textview4);

        spin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.songlist_Number, android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mediaplayer != null && mediaplayer.isPlaying()) {
                    mediaplayer.pause();
                    b2.setEnabled(false);
                    b3.setEnabled(true);
                }
                switch (position) {
                    case 0:
                        mediaplayer = MediaPlayer.create(getApplicationContext(), R.raw.song1);
                        songName = "Ibiza Pill";
                        break;
                    case 1:
                        mediaplayer = MediaPlayer.create(getApplicationContext(), R.raw.song2);
                        songName = "Emperor's new clothes";
                        break;
                    case 2:
                        mediaplayer = MediaPlayer.create(getApplicationContext(), R.raw.song3);
                        songName = "Bleed it Out";
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        seekbar = (SeekBar) findViewById(R.id.seekbar);


        b2.setEnabled(false);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt2.setText(songName);

                Toast.makeText(MainActivity.this, "Playing Song!", Toast.LENGTH_SHORT).show();

                mediaplayer.start();

                finaltime = mediaplayer.getDuration();
                starttime = mediaplayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finaltime);
                    oneTimeOnly = 1;
                }

                txt3.setText(String.format("%d min %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finaltime), TimeUnit.MILLISECONDS.toSeconds((long) finaltime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finaltime))));
                txt1.setText(String.format("%d min %d sec", TimeUnit.MILLISECONDS.toMinutes((long) starttime), TimeUnit.MILLISECONDS.toSeconds((long) starttime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) starttime))));

                b2.setEnabled(true);
                b3.setEnabled(false);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Song Paused", Toast.LENGTH_SHORT).show();
                mediaplayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) starttime;

                if ((temp + forwardTime) <= finaltime) {
                    starttime = starttime + forwardTime;
                    mediaplayer.seekTo((int) starttime);
                    Toast.makeText(MainActivity.this, "Jumped 5 seconds ahead!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Cannot jump 5 seconds ahead", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) starttime;

                if ((temp - backwordTime) >= 0) {
                    starttime = starttime - backwordTime;
                    mediaplayer.seekTo((int) starttime);
                    Toast.makeText(MainActivity.this, "Jumped 5 seconds Behind!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Cannot jump 5 seconds behind", Toast.LENGTH_SHORT).show();
                }
            }
        });



        myhandler=new Handler();
        myhandler.postDelayed(UpdateSongTime, 1000);

    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            starttime = mediaplayer.getCurrentPosition();
            txt1.setText(String.format("%d min %d sec", TimeUnit.MILLISECONDS.toMinutes((long) starttime), TimeUnit.MILLISECONDS.toSeconds((long) starttime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) starttime))));

            seekbar.setProgress((int) starttime);
            myhandler.postDelayed(this, 1000);
        }
    };


}
