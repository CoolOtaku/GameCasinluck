package com.denise_hill_sarah.gamecasinluck.views_controllers;

import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.backgroundMusic;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.isPlaying;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.play;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.saveMusicSetting;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.stop;
import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.onClickAnimation;
import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.sp;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.volumeMusic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.denise_hill_sarah.gamecasinluck.R;
import com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame;

import java.util.Objects;

public class SettingsDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Activity activity;
    private MenuDialog menu;

    private FrameLayout settingsContainer;
    private ImageButton btnClose, btnMusicOnOf;
    private SeekBar seekMusicVolume, seekSoundsVolume;

    public SettingsDialog(Activity activity, MenuDialog menu) {
        super(activity);
        this.activity = activity;
        this.menu = menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.settings_dialog);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().setAttributes(layoutParams);

        @SuppressLint({"InternalInsetResource", "DiscouragedApi"})
        int navWidth = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        settingsContainer = findViewById(R.id.settingsContainer);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) settingsContainer.getLayoutParams();
        params.setMargins(activity.getResources().getDimensionPixelSize(navWidth), 0, 0, 0);
        settingsContainer.setLayoutParams(params);

        btnClose = findViewById(R.id.btnClose);
        btnMusicOnOf = findViewById(R.id.btnMusicOnOf);
        seekMusicVolume = findViewById(R.id.seekMusicVolume);
        seekSoundsVolume = findViewById(R.id.seekSoundsVolume);

        if (sp.getBoolean("MUSIC_STATUS", true)) {
            btnMusicOnOf.setImageResource(R.drawable.round_music_note_44);
        } else {
            btnMusicOnOf.setImageResource(R.drawable.round_music_off_44);
        }
        seekMusicVolume.setMax(100);
        seekMusicVolume.setProgress(sp.getInt("VOLUME_PROGRESS_MUSIC", 100));
        seekSoundsVolume.setMax(100);
        seekSoundsVolume.setProgress(sp.getInt("VOLUME_PROGRESS_SOUND", 100));

        btnClose.setOnClickListener(this);
        btnMusicOnOf.setOnClickListener(this);
        seekMusicVolume.setOnSeekBarChangeListener(this);
        seekSoundsVolume.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(onClickAnimation);
        if (view.getId() == R.id.btnClose) {
            cancel();
            menu.show();
        } else if (view.getId() == R.id.btnMusicOnOf) {
            if (isPlaying()) {
                stop();
                btnMusicOnOf.setImageResource(R.drawable.round_music_off_44);
            } else {
                play(activity);
                btnMusicOnOf.setImageResource(R.drawable.round_music_note_44);
            }
            saveMusicSetting();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (seekBar.getId() == R.id.seekMusicVolume) {
            volumeMusic = (float) (1 - (Math.log(100 - progress) / Math.log(100)));
            if (backgroundMusic != null)
                backgroundMusic.setVolume(MusicSoundGame.volumeMusic, MusicSoundGame.volumeMusic);
        } else if (seekBar.getId() == R.id.seekSoundsVolume)
            MusicSoundGame.volumeSound = (float) (1 - (Math.log(100 - progress) / Math.log(100)));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        MusicSoundGame.saveVolume(seekMusicVolume.getProgress(), seekMusicVolume.getProgress());
    }

    @Override
    public void show() {
        super.show();
        if (sp.getBoolean("MUSIC_STATUS", true)) {
            btnMusicOnOf.setImageResource(R.drawable.round_music_note_44);
        } else btnMusicOnOf.setImageResource(R.drawable.round_music_off_44);
    }

}