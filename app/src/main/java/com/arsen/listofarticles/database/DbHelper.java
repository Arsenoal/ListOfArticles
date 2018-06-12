package com.arsen.listofarticles.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arsen.listofarticles.common.db.ArticlesTable;

import java.util.Locale;

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
        SQLiteDatabase db = this.getReadableDatabase();
        String query
                = String.format(Locale.ENGLISH, "SELECT * FROM %s WHERE %s = \"%s\"", ArticlesTable.TABLE, ArticlesTable.COLUMN.TITLE, title);
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
