package com.denise_hill_sarah.gamecasinluck.views_controllers;

import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.onClickAnimation;

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

import androidx.recyclerview.widget.RecyclerView;

import com.denise_hill_sarah.gamecasinluck.InAppClass;
import com.denise_hill_sarah.gamecasinluck.R;

import java.util.Objects;

public class ShopDialog extends Dialog implements View.OnClickListener {

    private Activity activity;

    private FrameLayout shopContainer;
    private RecyclerView listItemShop;
    private ImageButton btnClose;

    private AdapterShop adapterShop;
    private InAppClass inAppClass;

    public ShopDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.inAppClass = new InAppClass(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.shop_dialog);

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
        shopContainer = findViewById(R.id.shopContainer);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) shopContainer.getLayoutParams();
        params.setMargins(activity.getResources().getDimensionPixelSize(navWidth), 0, 0, 0);
        shopContainer.setLayoutParams(params);

        btnClose = findViewById(R.id.btnClose);
        listItemShop = findViewById(R.id.listItemShop);

        this.adapterShop = new AdapterShop(activity, inAppClass.resList, inAppClass.getBillingClient());

        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(onClickAnimation);
        if (view.getId() == R.id.btnClose) {
            cancel();
        }
    }

}