package com.arsen.listofarticles.presenters;

import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.interfaces.ArticlesView;

public class ArticlesPresenter {
    private ArticlesView articlesView;

    public void attachView(ArticlesView articlesView) {
        this.articlesView = articlesView;

        ((App)(((AppCompatActivity)articlesView.getContext()).getApplication())).getNetComponent().inject(this);
    }
}
