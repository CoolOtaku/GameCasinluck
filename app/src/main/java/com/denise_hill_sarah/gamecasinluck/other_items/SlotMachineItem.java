package com.denise_hill_sarah.gamecasinluck.other_items;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.denise_hill_sarah.gamecasinluck.R;

public class SlotMachineItem extends FrameLayout {

    private Context context;

    private ImageView currentSlotItem, nextSlotItem;

    private int animationDuration = 170, oldValue = 0;

    private SlotMachineItemSpinEnd slotMachineItemSpinEnd;

    public SlotMachineItem(@NonNull Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public SlotMachineItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_slot, this);
        currentSlotItem = (ImageView) getRootView().findViewById(R.id.currentSlotItem);
        nextSlotItem = (ImageView) getRootView().findViewById(R.id.nextSlotItem);
        nextSlotItem.setTranslationY(getHeight());
    }

    public void setValueRandom(int itemSlotIndex, int rotateCount) {
        currentSlotItem.animate().translationY(-getHeight()).setDuration(animationDuration).start();
        nextSlotItem.setTranslationY(nextSlotItem.getHeight());
        nextSlotItem.animate().translationY(0).setDuration(animationDuration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setImage(currentSlotItem, oldValue % 8);
                        currentSlotItem.setTranslationY(0);
                        if (oldValue != rotateCount) {
                            setValueRandom(itemSlotIndex, rotateCount);
                            oldValue++;
                        } else {
                            oldValue = 0;
                            setImage(nextSlotItem, itemSlotIndex);
                            slotMachineItemSpinEnd.spinEnd();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    @SuppressLint("DiscouragedApi")
    private void setImage(ImageView slotView, int value) {
        slotView.setImageResource(getResources().getIdentifier("s" + value,
                "drawable", context.getPackageName()));
        slotView.setTag(value);
    }

    public void setSlotMachineItemSpinEnd(SlotMachineItemSpinEnd slotMachineItemSpinEnd) {
        this.slotMachineItemSpinEnd = slotMachineItemSpinEnd;
    }

    public int getValue() {
        return Integer.parseInt(nextSlotItem.getTag().toString());
    }

}