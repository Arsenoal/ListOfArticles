package com.arsen.listofarticles.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.rest.models.ArticleFieldBaseImpl;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

import java.util.Locale;

public class DbHelper extends SQLiteOpenHelper {
    private static final String NAME = "listOfArticles";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArticlesTable.CREATE_SCRIPT);
        db.execSQL(ArticlesTable.CREATE_PINNED_ARTICLES_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean isItemPresent(String table, String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query
                = String.format(Locale.ENGLISH, "SELECT * FROM %s WHERE %s = \"%s\"", table, ArticlesTable.COLUMN.TITLE, title);
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArticleField getArticleViaID(String tabelName, long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =
                String.format(Locale.ENGLISH, "SELECT * FROM %s WHERE %s = \"%s\"", tabelName, ArticlesTable.COLUMN.ID, id);

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        ArticleFieldBaseImpl article = new ArticleFieldBaseImpl();
        article.setId(String.valueOf(cursor.getLong(cursor.getColumnIndex(ArticlesTable.COLUMN.ID))));
        article.setArticleId(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.ARTICLE_ID)));
        article.setTitle(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.TITLE)));
        article.setCategory(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.CATEGORY)));
        article.setThumbnail(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.THUMBNAIL)));

        cursor.close();

        return article;
    }
}
