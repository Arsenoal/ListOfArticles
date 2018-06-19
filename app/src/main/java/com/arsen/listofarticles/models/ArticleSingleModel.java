package com.arsen.listofarticles.models;

import android.content.ContentValues;
import android.content.Context;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.database.DbHelper;
import com.arsen.listofarticles.interfaces.OnArticleLoadedCallback;
import com.arsen.listofarticles.interfaces.OnArticlesLoadedCallback;
import com.arsen.listofarticles.interfaces.OnCompletedCallback;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.rest.services.ArticlesService;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleSingleModel {

    @Inject
    DbHelper dbHelper;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    ArticlesService articlesService;

    public ArticleSingleModel(Context context) {
        ((App) context).getNetComponent().inject(this);
    }

    public void loadData(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void loadArticleFromDB(OnArticleLoadedCallback onArticleLoadedCallback, String tableName, String dbID) {
        Observable.fromCallable(() -> {
            long id = Long.valueOf(dbID);
            return dbHelper.getArticleViaID(tableName, id);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        onArticleLoadedCallback::onLoad,
                        Throwable::printStackTrace
                );
    }

    public ArticlesService getArticlesService() {
        return articlesService;
    }

    public void addPinnedArticleToDB(OnCompletedCallback onCompletedCallback, ContentValues cv) {
        if (!dbHelper.isItemPresent(ArticlesTable.PINNED_ARTICLES_TABLE, cv.getAsString(ArticlesTable.COLUMN.TITLE))) {
            Observable.fromCallable(() -> {
                dbHelper.getWritableDatabase().insert(ArticlesTable.PINNED_ARTICLES_TABLE, null, cv);
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

        } else
            onCompletedCallback.onError();
    }

    public void invalidate() {
        compositeDisposable.clear();
    }
}