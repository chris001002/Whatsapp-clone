package com.example.whatsapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.whatsapp.R;

public class sentSoundEffect extends Service {
    MediaPlayer mp;
    public sentSoundEffect() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp = MediaPlayer.create(this, R.raw.send_sfx);
        mp.start();
        mp.setOnCompletionListener(mp -> stopSelf());
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}