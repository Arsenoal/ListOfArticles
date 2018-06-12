package com.arsen.listofarticles.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arsen.listofarticles.common.db.ArticlesTable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String NAME = "listOfArticles";
    public static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArticlesTable.CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean isItemPresent(String title) {
        return false;
    }
}
