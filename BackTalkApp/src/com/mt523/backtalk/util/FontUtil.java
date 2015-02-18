package com.mt523.backtalk.util;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {

    private static FontUtil instance;
    private Typeface mainFont;

    private FontUtil(Context c) {
        mainFont = Typeface.createFromAsset(c.getAssets(),
                "fonts/font2.otf");
    }

    public static FontUtil instance(Context c) {
        if (instance == null) {
            instance = new FontUtil(c);
        }
        return instance;
    }

    public Typeface getMainFont() {
        return mainFont;
    }

}
