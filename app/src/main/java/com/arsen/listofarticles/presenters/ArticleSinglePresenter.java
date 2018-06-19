package com.arsen.listofarticles.presenters;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.interfaces.OnCompletedCallback;
import com.arsen.listofarticles.interfaces.view.ArticleSingleView;
import com.arsen.listofarticles.models.ArticleSingleModel;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.util.Constants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arsen.listofarticles.util.Constants.ARTICLE_ID_KEY;

public class ArticleSinglePresenter {

    @Inject
    ArticleSingleModel articleSingleModel;

    private ArticleSingleView articleSingleView;
    private ArticleField articleField;
    private AppCompatActivity appCompatActivity;
    private String id;

    public void attachView(ArticleSingleView articleSingleView) {
        this.articleSingleView = articleSingleView;
        this.appCompatActivity = (AppCompatActivity) articleSingleView.provideContext();

        ((App) appCompatActivity.getApplication()).getNetComponent().inject(this);
        initId();
    }

    private void initId() {
        Intent intent = appCompatActivity.getIntent();
        id = intent.getStringExtra(ARTICLE_ID_KEY);
    }

    public void detachView() {
        this.articleSingleView = null;
        this.articleSingleModel.invalidate();
    }

    public void loadData() {
        articleSingleModel.loadData(
                articleSingleModel.getArticlesService().
                        getArticle(
                                id,
                                Constants.API_KEY,
                                Constants.FIELDS).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        map(article -> article).
                        subscribe(
                                article -> {
                                    articleField = article.getResponse().getContent().getFields();
                                    articleSingleView.loadData(articleField);
                                },
                                Throwable::printStackTrace
                        )
        );
    }

    public void pinArticle() {
        if (articleField != null) {
            ContentValues cv;

            cv = new ContentValues(3);
            cv.put(ArticlesTable.COLUMN.TITLE, articleField.getTitle());
            cv.put(ArticlesTable.COLUMN.CATEGORY, articleField.getCategory());
            cv.put(ArticlesTable.COLUMN.THUMBNAIL, articleField.getThumbnail());

            articleSingleModel.addPinnedArticleToDB(
                    new OnCompletedCallback() {
                        @Override
                        public void completed() {
                            articleSingleView.successfullyPinned();
                        }

                        @Override
                        public void onError() {
                            articleSingleView.errorOnPin();
                        }
                    }, cv);
        }
    }
}
