package com.mt523.backtalk.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.jirbo.adcolony.AdColonyVideoAd;
import com.mt523.backtalk.DrawerActivity;
import com.mt523.backtalk.R;
import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.util.LevelUnlockAdListener;

public class UnlockDialog extends DialogFragment {

    private static final String TAG = UnlockDialog.class.getName();

    private DrawerActivity activity;
    private Card card;

    public UnlockDialog(DrawerActivity activity, Card card) {
        this.activity = activity;
        this.card = card;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                "This card is locked. View an ad from our sponsors to unlock?")
                .setPositiveButton("Sure!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d(TAG, "USER SAID SURE :D");
                                AdColonyVideoAd ad = new AdColonyVideoAd(
                                        getResources().getString(
                                                R.string.ZONE_ID))
                                        .withListener(new LevelUnlockAdListener(
                                                activity, card));
                                ad.show();
                            }
                        })
                .setNegativeButton("No thanks",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
