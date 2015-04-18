package com.mt523.backtalk.util;

import android.util.Log;

import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdListener;
import com.mt523.backtalk.DrawerActivity;
import com.mt523.backtalk.packets.client.Card;

public class LevelUnlockAdListener implements AdColonyAdListener {

    private static final String TAG = LevelUnlockAdListener.class.getName();

    private DrawerActivity activity;
    private Card card;

    public LevelUnlockAdListener(DrawerActivity activity, Card card) {
        this.card = card;
        this.activity = activity;
    }

    @Override
    public void onAdColonyAdAttemptFinished(AdColonyAd ad) {
        if (ad.shown()) {
            Log.d(TAG, "Ad successfully shown.");
            activity.unlockCard(card.getId());
        } else {
            Log.d(TAG, "Ad was not successfully shown.");
        }
    }

    @Override
    public void onAdColonyAdStarted(AdColonyAd ad) {
    }

}
