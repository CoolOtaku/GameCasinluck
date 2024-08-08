package com.denise_hill_sarah.gamecasinluck.views_controllers;

import static android.view.View.inflate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denise_hill_sarah.gamecasinluck.R;

public class CustomToastGame extends Toast {

    private Activity activity;

    private View view;
    private LinearLayout containerToast;
    private TextView txtToastTitle, txtToastMessage;

    private int widthScene;

    public CustomToastGame(Activity activity) {
        super(activity);
        this.activity = activity;
        view = inflate(activity, R.layout.custom_toast_game, null);
        containerToast = view.findViewById(R.id.containerToast);
        txtToastTitle = view.findViewById(R.id.txtToastTitle);
        txtToastMessage = view.findViewById(R.id.txtToastMessage);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"})
        int navWidth = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        widthScene = displayMetrics.widthPixels + activity.getResources().getDimensionPixelSize(navWidth);

        setView(view);
        setGravity(Gravity.FILL, 0, 0);
    }

    public void showBigWin(String text) {
        setDuration(Toast.LENGTH_SHORT);

        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = activity.getDrawable(R.drawable.s8);
        drawable.setBounds(0, 0, 100, 100);
        txtToastTitle.setCompoundDrawables(null, drawable, null, null);

        txtToastTitle.setText(R.string.big_win);
        txtToastMessage.setText(text);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        if ((containerToast.getWidth() / 2) != 0) {
            layoutParams.setMargins((widthScene / 2) - (containerToast.getWidth() / 2), 0, 0, 0);
        } else {
            layoutParams.setMargins((widthScene / 2) - 300, 0, 0, 0);
        }
        containerToast.setLayoutParams(layoutParams);

        super.show();
    }

}