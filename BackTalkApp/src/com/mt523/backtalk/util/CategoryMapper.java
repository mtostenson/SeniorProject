package com.mt523.backtalk.util;

import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mt523.backtalk.R;

public class CategoryMapper {

    public static Drawable getCategoryDrawable(Context c, String category) {
        int drawable = R.drawable.ic_launcher;
        switch (category.toLowerCase(Locale.ENGLISH)) {
        case "places":
            drawable = R.drawable.places;
            break;
        case "movies/tv":
            drawable = R.drawable.television;
            break;
        case "music":
            drawable = R.drawable.note;
            break;
        case "food/drink":
            drawable = R.drawable.cutlery;
            break;
        case "sports/leisure":
            drawable = R.drawable.soccer;
            break;
        }
        return c.getResources().getDrawable(drawable);
    }

    public static int getCategoryColor(Context c, String category) {
        int color = android.R.color.black;
        switch (category) {
        case "places":
            color = R.color.places;
            break;
        case "movies/tv":
            color = R.color.movies;
            break;
        case "music":
            color = R.color.music;
            break;
        case "food/drink":
            color = R.color.food;
            break;
        case "sports/leisure":
            color = R.color.sports;
            break;
        }
        return c.getResources().getColor(color);
    }

}
