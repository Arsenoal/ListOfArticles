package com.arsen.listofarticles.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.rest.models.ArticleFieldBaseImpl;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.database.DbHelper;
import com.arsen.listofarticles.interfaces.OnArticlesLoadedCallback;
import com.arsen.listofarticles.interfaces.OnCompletedCallback;
import com.arsen.listofarticles.rest.services.ArticlesService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlesModel {
    @Inject
    DbHelper dbHelper;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    ArticlesService articlesService;

    public ArticlesModel(Context context) {
        ((App) context).getNetComponent().inject(this);
    }

    public void loadArticles(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void loadArticlesFromDB(OnArticlesLoadedCallback onArticlesLoadedCallback) {
        Observable.fromCallable(() -> {
            ArrayList<ArticleField> articleFields = new ArrayList<>();
            Cursor cursor
                    = dbHelper.getReadableDatabase().query(ArticlesTable.ARTICLES_TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                ArticleFieldBaseImpl article = new ArticleFieldBaseImpl();
                article.setId(String.valueOf(cursor.getLong(cursor.getColumnIndex(ArticlesTable.COLUMN.ID))));
                article.setArticleId(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.ARTICLE_ID)));
                article.setTitle(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.TITLE)));
                article.setCategory(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.CATEGORY)));
                article.setThumbnail(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.THUMBNAIL)));
                articleFields.add(article);
            }
            cursor.close();

            return articleFields;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        onArticlesLoadedCallback::onLoad,
                        Throwable::printStackTrace
                );
    }

    public void loadPinnedArticlesFromDB(OnArticlesLoadedCallback onArticlesLoadedCallback) {
        Observable.fromCallable(() -> {
            ArrayList<ArticleField> articleFields = new ArrayList<>();
            Cursor cursor
                    = dbHelper.getReadableDatabase().query(ArticlesTable.PINNED_ARTICLES_TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                ArticleFieldBaseImpl article = new ArticleFieldBaseImpl();
                article.setId(String.valueOf(cursor.getLong(cursor.getColumnIndex(ArticlesTable.COLUMN.ID))));
                article.setArticleId(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.ARTICLE_ID)));
                article.setTitle(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.TITLE)));
                article.setCategory(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.CATEGORY)));
                article.setThumbnail(cursor.getString(cursor.getColumnIndex(ArticlesTable.COLUMN.THUMBNAIL)));
                articleFields.add(article);
            }
            cursor.close();

            return articleFields;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        onArticlesLoadedCallback::onLoad,
                        Throwable::printStackTrace
                );
    }

    public void addArticleToDB(OnCompletedCallback onCompletedCallback, ContentValues cv) {
        if (!dbHelper.isItemPresent(ArticlesTable.ARTICLES_TABLE, cv.getAsString(ArticlesTable.COLUMN.TITLE))) {
            Observable.fromCallable(() -> {
                dbHelper.getWritableDatabase().insert(ArticlesTable.ARTICLES_TABLE, null, cv);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(
                            v -> onCompletedCallback.completed(),
                            Throwable::printStackTrace
                    );
        }
    }

    public void invalidate() {
        compositeDisposable.clear();
    }

    public ArticlesService getArticlesService() {
        return articlesService;
    }
}
