package com.mt523.backtalk.util;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;

import com.mt523.backtalk.R;

public class BTFX {

    private static Context context;

    private static final String TAG = BTFX.class.getName();

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefsEditor;

    /* This zMUST be called first */
    public static void prepare(Context pContext) {
        context = pContext;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = prefs.edit();
    }

    public static void vibrate(int time) {
        if (prefs.getBoolean("vibration", true)) {
            ((Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE))
                    .vibrate(time);
        }
    }

    public static void playSound(String name) {
        if (prefs.getBoolean("sounds", true)) {
            int sound = 0;
            switch (name) {
            case "correct":
                sound = R.raw.correct;
                break;
            case "incorrect":
                sound = R.raw.incorrect;
                break;
            case "done":
                sound = R.raw.done;
                break;
            }
            MediaPlayer mp = MediaPlayer.create(context, sound);
            mp.start();
        }
    }

    public static void shake(View v) {
        if (prefs.getBoolean("animations", true)) {
            BtAnimations.shake(v);
        }
    }

    public static void changeSetting(String setting, boolean value) {
        prefsEditor.putBoolean(setting, value).commit();
    }

    public static boolean getSetting(String setting) {
        boolean result = prefs.getBoolean(setting, true);
        return result;
    }

}
