package com.mt523.backtalk.util;

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
        Animator phase1 = (ObjectAnimator.ofFloat(v, "translationX", -25)
                .setDuration(25));
        Animator phase2 = (ObjectAnimator.ofFloat(v, "translationX", 25)
                .setDuration(30));
        Animator phase3 = (ObjectAnimator.ofFloat(v, "translationX", -20)
                .setDuration(35));
        Animator phase4 = (ObjectAnimator.ofFloat(v, "translationX", 20)
                .setDuration(40));
        Animator phase5 = (ObjectAnimator.ofFloat(v, "translationX", -15)
                .setDuration(45));
        Animator phase6 = (ObjectAnimator.ofFloat(v, "translationX", 15)
                .setDuration(50));
        Animator phase7 = (ObjectAnimator.ofFloat(v, "translationX", -10)
                .setDuration(55));
        Animator phase8 = (ObjectAnimator.ofFloat(v, "translationX", 5)
                .setDuration(60));
        Animator phase9 = (ObjectAnimator.ofFloat(v, "translationX", 0)
                .setDuration(65));
        set.playSequentially(phase1, phase2, phase3, phase4, phase5, phase6,
                phase7, phase8, phase9);
        // set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }
    
    public static ScaleAnimation pulse(int pDuration) {
        ScaleAnimation pulse = new ScaleAnimation(1f, 1.1f, 1f, 1.1f,
              Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        pulse.setRepeatCount(ScaleAnimation.INFINITE);
        pulse.setRepeatMode(ScaleAnimation.REVERSE);
        pulse.setDuration(pDuration);
        return pulse;
     }
}
