package com.mt523.backtalk.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BackTalkDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_CARDS = "cards";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER = "answer";
    public static final String COLUMN_HINT = "hint";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SOLVED = "solved";

    private static final String DATABASE_NAME = "backtalk.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_CARDS
            + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_QUESTION
            + " TEXT NOT NULL, " + COLUMN_ANSWER + " TEXT NOT NULL, "
            + COLUMN_HINT + " TEXT NOT NULL, " + COLUMN_CATEGORY
            + " TEXT NOT NULL, " + COLUMN_SOLVED + " INTEGER DEFAULT 0);";

    public BackTalkDbHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }

}
