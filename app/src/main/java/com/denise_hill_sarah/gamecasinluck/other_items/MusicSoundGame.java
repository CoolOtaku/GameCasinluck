package com.denise_hill_sarah.gamecasinluck.other_items;

import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.spEditor;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.denise_hill_sarah.gamecasinluck.R;

public class MusicSoundGame {

    public static MediaPlayer backgroundMusic = null;
    private static SoundPool soundsGame = null;

    public static float volumeMusic = 1f, volumeSound = 1f;
    public static int SOUND_LOSE, SOUND_WIN, SOUND_BIG_WIN;

    public static void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundsGame = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(10)
                    .build();
        } else soundsGame = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        SOUND_LOSE = soundsGame.load(context, R.raw.sound_lose, 1);
        SOUND_WIN = soundsGame.load(context, R.raw.sound_win, 1);
        SOUND_BIG_WIN = soundsGame.load(context, R.raw.sound_big_win, 1);
    }

    public static void play(Context context, int resource) {
        stop();
        backgroundMusic = MediaPlayer.create(context, resource);
        backgroundMusic.setVolume(volumeMusic, volumeMusic);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    public static void sound(int id) {
        soundsGame.play(id, volumeSound, volumeSound, 1, 0, 1);
    }

    public static void stop() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    public static void play(Context context) {
        if (backgroundMusic != null) {
            backgroundMusic.start();
        } else {
            play(context, R.raw.sound_music);
        }
    }

    public static void pause() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    public static boolean isPlaying() {
        if (backgroundMusic != null) {
            return backgroundMusic.isPlaying();
        } else {
            return false;
        }
    }

    public static void saveMusicSetting() {
        spEditor.putBoolean("MUSIC_STATUS", isPlaying());
        spEditor.apply();
    }

    public static void saveVolume(int progress1, int progress2) {
        spEditor.putFloat("VOLUME_MUSIC", MusicSoundGame.volumeMusic);
        spEditor.putFloat("VOLUME_SOUND", MusicSoundGame.volumeSound);
        spEditor.putInt("VOLUME_PROGRESS_MUSIC", progress1);
        spEditor.putInt("VOLUME_PROGRESS_SOUND", progress2);
        spEditor.apply();
    }

}