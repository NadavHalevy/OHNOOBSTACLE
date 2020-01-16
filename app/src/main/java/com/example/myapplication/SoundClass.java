/*
package com.example.myapplication;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundClass {




        private static SoundPool soundPool;
        private static int hit;

        public SoundClass(Context context){

            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            hit = soundPool.load(context, R.raw.hit, 1);
        }

        public void playHit(){
            //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
            soundPool.play(hit,1.0f/8, 1.0f/8,1,0,1.0f);
        }

}
*/


