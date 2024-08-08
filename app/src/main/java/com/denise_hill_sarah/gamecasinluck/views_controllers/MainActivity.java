package com.denise_hill_sarah.gamecasinluck.views_controllers;

import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.SOUND_BIG_WIN;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.SOUND_LOSE;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.SOUND_WIN;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.pause;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.play;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.sound;
import static com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame.stop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.denise_hill_sarah.gamecasinluck.other_items.MusicSoundGame;
import com.denise_hill_sarah.gamecasinluck.R;
import com.denise_hill_sarah.gamecasinluck.other_items.SlotMachineItem;
import com.denise_hill_sarah.gamecasinluck.other_items.SlotMachineItemSpinEnd;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SlotMachineItemSpinEnd {

    private SlotMachineItem[][] slotMachineItems;
    private TextView txtBet, txtWin, txtBalance;
    private ImageButton btnBetMinus, btnBetPlus, btnMenu, btnSpeedFast, btnSpeedSlow, btnSpin;

    public static SharedPreferences sp;
    public static SharedPreferences.Editor spEditor;
    public static Random random = new Random();
    public static Animation onClickAnimation;

    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private CustomToastGame customToastGame;
    private MenuDialog menu;

    public static int COINS;

    private boolean isPlus, isTouch;
    private int bet = 5, win = 0, count = 0, countItems = -1, speedSpin = 15;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUi();
        setContentView(R.layout.activity_main);

        slotMachineItems = new SlotMachineItem[][]{
                {
                        findViewById(R.id.item1), findViewById(R.id.item2), findViewById(R.id.item3),
                        findViewById(R.id.item4), findViewById(R.id.item5), findViewById(R.id.item6)
                },
                {
                        findViewById(R.id.item7), findViewById(R.id.item8), findViewById(R.id.item9),
                        findViewById(R.id.item10), findViewById(R.id.item11), findViewById(R.id.item12)
                },
                {
                        findViewById(R.id.item13), findViewById(R.id.item14), findViewById(R.id.item15),
                        findViewById(R.id.item16), findViewById(R.id.item17), findViewById(R.id.item18)
                },
                {
                        findViewById(R.id.item19), findViewById(R.id.item20), findViewById(R.id.item21),
                        findViewById(R.id.item22), findViewById(R.id.item23), findViewById(R.id.item24)
                }
        };
        txtBet = findViewById(R.id.txtBet);
        txtWin = findViewById(R.id.txtWin);
        txtBalance = findViewById(R.id.txtBalance);
        btnBetMinus = findViewById(R.id.btnBetMinus);
        btnBetPlus = findViewById(R.id.btnBetPlus);
        btnMenu = findViewById(R.id.btnMenu);
        btnSpeedFast = findViewById(R.id.btnSpeedFast);
        btnSpeedSlow = findViewById(R.id.btnSpeedSlow);
        btnSpin = findViewById(R.id.btnSpin);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spEditor = sp.edit();
        MusicSoundGame.volumeMusic = sp.getFloat("VOLUME_MUSIC", 1f);
        MusicSoundGame.volumeSound = sp.getFloat("VOLUME_SOUND", 1f);
        MusicSoundGame.init(getApplicationContext());
        checkCOINS();
        onClickAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animation_click);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(checkOnTouch);
            }
        }, 0, 300);

        for (SlotMachineItem[] i : slotMachineItems) {
            for (SlotMachineItem j : i) {
                j.setSlotMachineItemSpinEnd(this);
                countItems++;
            }
        }

        btnBetMinus.setOnTouchListener(this);
        btnBetPlus.setOnTouchListener(this);

        checkBet();
        checkBalanceAndWin();
        customToastGame = new CustomToastGame(this);
        menu = new MenuDialog(this);
    }

    public void onClickMenu(View view) {
        view.startAnimation(onClickAnimation);
        menu.show();
    }

    public void onClickSpeedFast(View view) {
        view.startAnimation(onClickAnimation);
        speedSpin = 7;
    }

    public void onClickSpeedSlow(View view) {
        view.startAnimation(onClickAnimation);
        speedSpin = 15;
    }

    public void onClickSpin(View view) {
        view.startAnimation(onClickAnimation);
        if (COINS >= bet && COINS > 0) {
            actionForbid();

            for (SlotMachineItem[] i : slotMachineItems) {
                for (SlotMachineItem j : i) {
                    j.setValueRandom(random.nextInt(8) + 1, random.nextInt((speedSpin - 5) + 1) + 5);
                }
            }

            COINS -= bet;
            saveCOINS();
            checkBalanceAndWin();
        }
    }

    @Override
    public void spinEnd() {
        if (count < countItems) {
            count++;
            return;
        }

        count = 0;
        win = 0;
        actionEnable();

        double multiplier = 0;
        for (SlotMachineItem[] i : slotMachineItems) {
            Set set = new HashSet();
            int countDuplicate = 0;

            for (SlotMachineItem j : i) {
                if (!set.add(j.getValue())) {
                    countDuplicate++;
                    if (j.getValue() == 8) multiplier += 0.5;
                }
            }

            switch (countDuplicate) {
                case 2:
                    multiplier += 0.25;
                    break;
                case 3:
                    multiplier += 1.0;
                    break;
                case 4:
                    multiplier += 1.5;
                    break;
                case 5:
                    multiplier += 2.0;
                    break;
            }
        }

        if (multiplier >= 1.0) {
            win = (int) (bet * multiplier);
        } else sound(SOUND_LOSE);
        if (multiplier >= 3.0) {
            customToastGame.showBigWin(getString(R.string.big_win_coins, win));
            sound(SOUND_BIG_WIN);
        } else if (multiplier >= 1.0) {
            sound(SOUND_WIN);
        }

        COINS += win;
        saveCOINS();
        checkBalanceAndWin();
    }

    private void actionEnable() {
        btnSpin.setAlpha(1f);
        btnSpin.setEnabled(true);
        btnBetMinus.setAlpha(1f);
        btnBetMinus.setEnabled(true);
        btnBetPlus.setAlpha(1f);
        btnBetPlus.setEnabled(true);
        btnMenu.setAlpha(1f);
        btnMenu.setEnabled(true);
        btnSpeedFast.setAlpha(1f);
        btnSpeedFast.setEnabled(true);
        btnSpeedSlow.setAlpha(1f);
        btnSpeedSlow.setEnabled(true);
    }

    private void actionForbid() {
        btnSpin.setAlpha(.5f);
        btnSpin.setEnabled(false);
        btnBetMinus.setAlpha(.5f);
        btnBetMinus.setEnabled(false);
        btnBetPlus.setAlpha(.5f);
        btnBetPlus.setEnabled(false);
        btnMenu.setAlpha(.5f);
        btnMenu.setEnabled(false);
        btnSpeedFast.setAlpha(.5f);
        btnSpeedFast.setEnabled(false);
        btnSpeedSlow.setAlpha(.5f);
        btnSpeedSlow.setEnabled(false);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.startAnimation(onClickAnimation);
                if (view.getId() == R.id.btnBetMinus) {
                    isPlus = false;
                }
                if (view.getId() == R.id.btnBetPlus) {
                    isPlus = true;
                }
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;
        }
        return true;
    }

    private final Runnable checkOnTouch = new Runnable() {
        public void run() {
            if (isTouch) {
                if (isPlus) {
                    bet++;
                    if (bet > COINS) bet = COINS;
                } else {
                    bet--;
                    if (bet < 1) bet = 1;
                }
                txtBet.setText(String.valueOf(bet));
            }
        }
    };

    private void checkBet() {
        if (COINS >= 5) bet = 5;
        else bet = COINS;
        txtBet.setText(String.valueOf(bet));
    }

    private void checkBalanceAndWin() {
        txtBalance.setText(String.valueOf(COINS));
        txtWin.setText(String.valueOf(win));
    }

    private void hideSystemUi() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private void checkCOINS() {
        COINS = sp.getInt("COINS", 300);
    }

    public static void saveCOINS() {
        spEditor.putInt("COINS", COINS);
        spEditor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sp.getBoolean("MUSIC_STATUS", true)) play(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCOINS();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUi();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public void finish() {
        super.finishAndRemoveTask();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        //dialogInAppBuyMoney.getBillingClient().endConnection();
        stop();
        super.onDestroy();
    }

}