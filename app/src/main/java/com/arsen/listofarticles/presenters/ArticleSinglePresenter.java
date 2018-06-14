package com.arsen.listofarticles.presenters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.interfaces.view.ArticleSingleView;
import com.arsen.listofarticles.models.ArticleSingleModel;
import com.arsen.listofarticles.rest.services.ArticlesService;
import com.arsen.listofarticles.util.Constants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arsen.listofarticles.util.Constants.ARTICLE_ID_KEY;

public class ArticleSinglePresenter {

    @Inject
    ArticleSingleModel articleSingleModel;

    @Inject
    ArticlesService articlesService;

    private ArticleSingleView articleSingleView;
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
    }

    public void loadData() {
        articleSingleModel.loadData(
                articlesService.
                        getArticle(
                                id,
                                Constants.API_KEY,
                                Constants.FIELDS).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        map(article -> article).
                        subscribe(
                                article -> articleSingleView.loadData(article.getResponse().getContent().getFields()),
                                error -> {
                                    //TODO implement UI presentation, for now just print stacktrace
                                    error.printStackTrace();
                                }
                        )
        );
    }
}
