package com.mt523.backtalk.util;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {

    private static FontUtil instance;
    private Typeface font;
    
    public static FontUtil instance(Context context) {
        if(instance == null) {
            instance = new FontUtil(context);
        }
        return instance;
    }
    
    private FontUtil(Context context) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/font2.otf");
    }
    
    public Typeface getFont() {
        return font;
    }
}
