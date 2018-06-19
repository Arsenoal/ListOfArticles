package com.arsen.listofarticles.presenters;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.interfaces.OnCompletedCallback;
import com.arsen.listofarticles.interfaces.view.ArticleSingleView;
import com.arsen.listofarticles.models.ArticleSingleModel;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.util.Constants;
import com.arsen.listofarticles.util.helper.NetworkHelper;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arsen.listofarticles.util.Constants.ARTICLE_DB_ID_KEY;
import static com.arsen.listofarticles.util.Constants.ARTICLE_ID_KEY;
import static com.arsen.listofarticles.util.Constants.ARTICLE_TYPE_KEY;

public class ArticleSinglePresenter {

    @Inject
    ArticleSingleModel articleSingleModel;

    private ArticleSingleView articleSingleView;
    private ArticleField articleField;
    private AppCompatActivity appCompatActivity;
    private String ID;
    private String dbID;
    private String articleType;

    public void attachView(ArticleSingleView articleSingleView) {
        this.articleSingleView = articleSingleView;
        this.appCompatActivity = (AppCompatActivity) articleSingleView.provideContext();

        prepare();
        ((App) appCompatActivity.getApplication()).getNetComponent().inject(this);
    }

    private void prepare() {
        Intent intent = appCompatActivity.getIntent();
        ID = intent.getStringExtra(ARTICLE_ID_KEY);
        dbID = intent.getStringExtra(ARTICLE_DB_ID_KEY);
        articleType = intent.getStringExtra(ARTICLE_TYPE_KEY);
    }

    public void detachView() {
        this.articleSingleView = null;
        this.articleSingleModel.invalidate();
    }

    public void startLoading() {
        if (NetworkHelper.isNetworkAvailable(appCompatActivity))
            loadArticle();
        else {
            switch (articleType) {
                case Constants.LIST_ARTICLE:
                    loadArticleFromDB(ArticlesTable.ARTICLES_TABLE, dbID);
                    break;
                case Constants.PINNED_ARTICLE:
                    loadArticleFromDB(ArticlesTable.PINNED_ARTICLES_TABLE, dbID);
                    break;

            }
        }
    }

    private void loadArticle() {
        articleSingleModel.loadData(
                articleSingleModel.getArticlesService().
                        getArticle(
                                ID,
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

    private void loadArticleFromDB(String tableName, String dbID) {
        articleSingleModel.loadArticleFromDB(article -> {
            articleField = article;
            articleSingleView.loadData(articleField);
        }, tableName, dbID);
    }

    public void pinArticle() {
        if (articleField != null) {
            ContentValues cv;

            cv = new ContentValues(4);
            cv.put(ArticlesTable.COLUMN.ARTICLE_ID, ID);
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
