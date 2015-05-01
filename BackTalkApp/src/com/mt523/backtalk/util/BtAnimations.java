package com.mt523.backtalk.util;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class BtAnimations {

    public static final String TAG = BtAnimations.class.getName();
    public static final BtAnimations instance = new BtAnimations();

    private BtAnimations() {
    }

    public static void shake(View v) {
        AnimatorSet set = new AnimatorSet();
        List<Animator> phases = new ArrayList<Animator>();
        int distance = -25;
        int duration = 25;
        for (int i = 0; i < 9; i++) {
            phases.add(ObjectAnimator.ofFloat(v, "translationX", distance)
                    .setDuration(duration));
            distance = (distance < 0) ? distance * -1 : (distance * -1) + 5;
            duration += 5;
        }
        set.playSequentially(phases);
        set.start();
    }

    public static ScaleAnimation pulse(int pDuration) {
        ScaleAnimation pulse = new ScaleAnimation(1f, 1.1f, 1f, 1.1f,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
                .5f);
        pulse.setRepeatCount(ScaleAnimation.INFINITE);
        pulse.setRepeatMode(ScaleAnimation.REVERSE);
        pulse.setDuration(pDuration);
        return pulse;
    }
}
