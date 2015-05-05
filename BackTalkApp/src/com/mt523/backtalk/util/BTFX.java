package com.mt523.backtalk.util;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

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

    public static void changeSetting(String setting, boolean value) {
        prefsEditor.putBoolean(setting, value).commit();
    }

    public static boolean getSetting(String setting) {
        boolean result = prefs.getBoolean(setting, true);
        return result;
    }

}
