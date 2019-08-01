package com.example.audiovideo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {


    private VideoView myVideoView;
    private Button btnPlayVideo;
    private Button btnPlayMusic;
    private Button btnPauseMusic;
    private Timer timer;

    private MediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private SeekBar seekBarBackAddForth;
    private AudioManager mAudioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);

        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayVideo.setOnClickListener(MainActivity.this);

        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPlayMusic.setOnClickListener(MainActivity.this);

        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        btnPauseMusic.setOnClickListener(MainActivity.this);

        mMediaController = new MediaController(MainActivity.this);
        mMediaPlayer = MediaPlayer.create(this, R.raw.cooldown);
        mSeekBar = findViewById(R.id.seekBarVolume);
        seekBarBackAddForth = findViewById(R.id.seekBarBackAndForth);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mSeekBar.setMax(maxVol);
        mSeekBar.setProgress(currVolume);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean userInput) {

                if (userInput) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarBackAddForth.setOnSeekBarChangeListener(this);
        seekBarBackAddForth.setMax(mMediaPlayer.getDuration());

        mMediaPlayer.setOnCompletionListener(this);

    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()) {
            case R.id.btnPlayVideo:

                Uri videoUri = Uri.parse("android.resource:// " + getPackageName() + "/" + R.raw.videoplay);
                myVideoView.setVideoURI(videoUri);
                myVideoView.setMediaController(mMediaController);
                mMediaController.setAnchorView(myVideoView);
                myVideoView.start();
                break;
            case R.id.btnPlayMusic:
                mMediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        seekBarBackAddForth.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                }, 0, 1000);

                break;
            case R.id.btnPauseMusic:

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    if (timer != null)
                        timer.cancel();
                }
                break;
        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (timer != null)
            timer.cancel();
        Toast.makeText(this, "Music has ended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        mMediaPlayer.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }
}
