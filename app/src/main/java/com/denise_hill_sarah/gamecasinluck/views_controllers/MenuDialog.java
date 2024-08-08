package com.denise_hill_sarah.gamecasinluck.views_controllers;

import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.onClickAnimation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.denise_hill_sarah.gamecasinluck.R;

import java.util.Objects;

public class MenuDialog extends Dialog implements View.OnClickListener {

    private Activity activity;

    private FrameLayout menuContainer;
    private ImageButton btnClose;
    private Button btnBuyCoins, btnSettings, btnQuit;
    private Drawable drawable;

    private SettingsDialog settings;

    public MenuDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.settings = new SettingsDialog(activity, this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.menu_dialog);

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
        menuContainer = findViewById(R.id.menuContainer);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) menuContainer.getLayoutParams();
        params.setMargins(activity.getResources().getDimensionPixelSize(navWidth), 0, 0, 0);
        menuContainer.setLayoutParams(params);

        btnClose = findViewById(R.id.btnClose);
        btnBuyCoins = findViewById(R.id.btnBuyCoins);
        btnSettings = findViewById(R.id.btnSettings);
        btnQuit = findViewById(R.id.btnQuit);

        drawable = activity.getDrawable(R.drawable.s8);
        assert drawable != null;
        drawable.setBounds(0, 0, 100, 100);
        btnBuyCoins.setCompoundDrawables(drawable, null, drawable, null);

        btnClose.setOnClickListener(this);
        btnBuyCoins.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(onClickAnimation);
        if (view.getId() == R.id.btnClose) {
            cancel();
        } else if (view.getId() == R.id.btnBuyCoins) {

        } else if (view.getId() == R.id.btnSettings) {
            cancel();
            settings.show();
        } else if (view.getId() == R.id.btnQuit) {
            activity.finish();
            System.exit(0);
        }
    }

}