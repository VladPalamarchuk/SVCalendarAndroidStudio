package com.example.calendar;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

public class PlaySoundButton {
    MediaPlayer m = new MediaPlayer();
    int id = 0;

    public PlaySoundButton() {
        id = MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_SOUND_BUTTON, 0);
        playBeep();

    }

    public void playBeep() {
        if (id > 0) {
            try {
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                }

                AssetFileDescriptor descriptor = MainActivity.getInstance()
                        .getAssets().openFd(id + ".mp3");
                m.setDataSource(descriptor.getFileDescriptor(),
                        descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                m.setAudioStreamType(AudioManager.STREAM_MUSIC);
                m.prepare();
                m.setVolume(0.1f, 0.1f);
                m.setLooping(false);
                m.start();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        m.release();
                        m = null;
                    }
                }, m.getDuration() + 10);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
