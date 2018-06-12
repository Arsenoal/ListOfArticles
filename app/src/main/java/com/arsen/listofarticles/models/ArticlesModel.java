package com.arsen.listofarticles.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.common.model.ArticleBaseImpl;
import com.arsen.listofarticles.common.model.ArticleField;
import com.arsen.listofarticles.database.DbHelper;
import com.arsen.listofarticles.interfaces.LoadArticlesCallback;
import com.arsen.listofarticles.interfaces.OnCompletedCallback;
import com.arsen.listofarticles.rest.services.FilmsService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlesModel {

    private DbHelper dbHelper;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    FilmsService filmsService;

    public ArticlesModel(AppCompatActivity appCompatActivity, DbHelper dbHelper) {
        ((App) appCompatActivity.getApplication()).getNetComponent().inject(this);

        this.dbHelper = dbHelper;
    }

    public void loadArticles(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void loadArticlesFromDB(LoadArticlesCallback loadArticlesCallback) {
        Observable.fromCallable(() -> {
            ArrayList<ArticleField> articleFields = new ArrayList<>();
            Cursor cursor
                    = dbHelper.getReadableDatabase().query(ArticlesTable.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                ArticleBaseImpl article = new ArticleBaseImpl();
                article.setId(cursor.getLong(cursor.getColumnIndex(ArticlesTable.COLUMN.ID)));
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
                        loadArticlesCallback::onLoad,
                        Throwable::printStackTrace
                );
    }

    public void addArticleToDB(OnCompletedCallback onCompletedCallback, ContentValues cv) {
        if (!dbHelper.isItemPresent(cv.getAsString(ArticlesTable.COLUMN.TITLE))) {
            Observable.fromCallable(() -> {
                dbHelper.getWritableDatabase().insert(ArticlesTable.TABLE, null, cv);
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

    public FilmsService getFilmsService() {
        return filmsService;
    }
}
