package com.arsen.listofarticles.presenters;

import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.interfaces.ArticlesView;
import com.arsen.listofarticles.models.ArticlesModel;

public class ArticlesPresenter {
    private ArticlesView articlesView;
    private ArticlesModel articlesModel;

    public ArticlesPresenter(ArticlesModel articlesModel) {
        this.articlesModel = articlesModel;
    }

    public void attachView(ArticlesView articlesView) {
        this.articlesView = articlesView;

        ((App)(((AppCompatActivity)articlesView.provideContext()).getApplication())).getNetComponent().inject(this);
    }

    public void detachView() {
        articlesView = null;
    }

    public void startLoading() {

    }

    private void loadArticles() {
        //TODO get articles from model and provide to view
    }
}
